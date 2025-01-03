package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import io.github.megadoxs.megabotany.common.entity.AuraFire;
import io.github.megadoxs.megabotany.common.entity.MegaBotanyEntities;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.network.C2SPacket.MagicAuraPacket;
import io.github.megadoxs.megabotany.common.network.MegaBotanyNetwork;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class JingweiFeather extends BaubleItem {
    private static final String TAG_COOLDOWN = "cooldown";

    public JingweiFeather(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && inCooldown(player))
            ItemNBTHelper.setInt(stack, TAG_COOLDOWN, ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 100) - 1);
    }

    @SubscribeEvent
    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (getStack(evt.getEntity()) != ItemStack.EMPTY && !inCooldown(evt.getEntity()) && ManaItemHandler.instance().requestManaExact(new ItemStack(Items.APPLE), evt.getEntity(), 300, true)) {
            MegaBotanyNetwork.sendToServer(new MagicAuraPacket());
            ItemNBTHelper.setInt(getStack(evt.getEntity()), TAG_COOLDOWN, 100);
        }
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent evt) {
        if (!evt.getEntity().level().isClientSide() && getStack(evt.getEntity()) != ItemStack.EMPTY && !inCooldown(evt.getEntity()) && ManaItemHandler.instance().requestManaExact(new ItemStack(Items.APPLE), evt.getEntity(), 300, true)) {
            trySpawnMagicAura(evt.getEntity());
            ItemNBTHelper.setInt(getStack(evt.getEntity()), TAG_COOLDOWN, 100);
        }
    }

    private boolean inCooldown(Player player) {
        return ItemNBTHelper.getInt(getStack(player), TAG_COOLDOWN, 0) > 0;
    }

    private ItemStack getStack(Player player) {
        if (CuriosApi.getCuriosInventory(player).resolve().isPresent())
            if (CuriosApi.getCuriosInventory(player).resolve().get().findFirstCurio(MegaBotanyItems.JINGWEI_FEATHER.get()).isPresent())
                return CuriosApi.getCuriosInventory(player).resolve().get().findFirstCurio(MegaBotanyItems.JINGWEI_FEATHER.get()).get().stack();
        return ItemStack.EMPTY;
    }

    public static void trySpawnMagicAura(Player player) {
        if (player.getInventory().getSelected().isEmpty()) {
            AuraFire aura = MegaBotanyEntities.AURA_FIRE.get().create(player.level());
            aura.setOwner(player);
            aura.setPos(player.getX(), player.getY() + 1.5, player.getZ());
            aura.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.8F, 1.0F);
            player.level().addFreshEntity(aura);
        }
    }
}

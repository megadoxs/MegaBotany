package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.relic.RelicBaubleItem;
import vazkii.botania.common.item.relic.RelicImpl;

@Mod.EventBusSubscriber(modid = "megabotany", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageNullification extends RelicBaubleItem {
    private static final String TAG_COOLDOWN = "cooldown";
    private static final int MAX_COOLDOWN = 1200;
    private static final int MANA_COST = 10000;

    public DamageNullification(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        super.onWornTick(stack, entity);

        if (!entity.level().isClientSide() && ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0) > 0) {
            ItemNBTHelper.setInt(stack, TAG_COOLDOWN, ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0) - 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void NegateDamage(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack stack = EquipmentHandler.findOrEmpty(MegaBotanyItems.ABSOLUTION_PENDANT.get(), player);
            if (!stack.isEmpty() && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, false) && ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0) == 0) {
                event.setCanceled(true);
                ItemNBTHelper.setInt(stack, TAG_COOLDOWN, MAX_COOLDOWN);
                ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, true);
            }
        }
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, new ResourceLocation(MegaBotany.MOD_ID, MegaBotanyItems.ABSOLUTION_PENDANT.getId().getPath()));
    }
}

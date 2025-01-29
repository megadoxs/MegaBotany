package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class SuperCrown extends BaubleItem {
    public SuperCrown(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent evt) {
        if (evt.getEntity() instanceof Player player) {
            if (CuriosApi.getCuriosInventory(player).resolve().get().isEquipped(MegaBotanyItems.SUPER_CROWN.get()) && hasArmorSet(player))
                evt.setAmount(Math.max(0, evt.getAmount() - 2)); // might change to be 10% damage reduction
        }
    }

    // I don't like the way he checks if you have the armor set
    public boolean hasArmorSet(Player player) {
        return hasArmorSetItem(player, 0) && hasArmorSetItem(player, 1) && hasArmorSetItem(player, 2) && hasArmorSetItem(player, 3);
    }

    public boolean hasArmorSetItem(Player player, int i) {
        if (player == null)
            return false;

        ItemStack stack = player.getInventory().armor.get(3 - i);
        if (stack.isEmpty())
            return false;

        return switch (i) {
            case 0 -> stack.getItem() == BotaniaItems.manasteelHelm;
            case 1 -> stack.getItem() == BotaniaItems.manasteelChest;
            case 2 -> stack.getItem() == BotaniaItems.manasteelLegs;
            case 3 -> stack.getItem() == BotaniaItems.manasteelBoots;
            default -> false;
        };

    }

}

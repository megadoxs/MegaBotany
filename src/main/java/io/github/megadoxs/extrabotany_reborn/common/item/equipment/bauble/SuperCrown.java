package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class SuperCrown extends BaubleItem {
    public SuperCrown(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent evt) {
        if(evt.getEntity() instanceof Player player){
            if(BaublesApi.isBaubleEquipped(player, ModItems.supercrown) != -1 && hasArmorSet(player))
                evt.setAmount(Math.max(0, evt.getAmount() - 2));
        }
        else {
            return;
        }
    }

    public boolean hasArmorSet(Player player) {
        return hasArmorSetItem(player, 0) && hasArmorSetItem(player, 1) && hasArmorSetItem(player, 2) && hasArmorSetItem(player, 3);
    }

    public boolean hasArmorSetItem(Player player, int i) {
        if(player == null || player.getInventory() == null || player.getInventory().armor == null)
            return false;

        ItemStack stack = player.getInventory().armor.get(3 - i);
        if(stack.isEmpty())
            return false;

        switch(i) {
            case 0: return stack.getItem() == ModItems.cosmhelm || stack.getItem() == ModItems.coshelmrevealing || stack.getItem() == ModItems.cmhelm || stack.getItem() == ModItems.cmhelmrevealing;
            case 1: return stack.getItem() == ModItems.cosmchest || stack.getItem() == ModItems.cmchest || stack.getItem() == ModItems.cmchestdarkened;
            case 2: return stack.getItem() == ModItems.cosmleg || stack.getItem() == ModItems.cmleg;
            case 3: return stack.getItem() == ModItems.cosmboot || stack.getItem() == ModItems.cmboot;
        }

        return false;
    }

}

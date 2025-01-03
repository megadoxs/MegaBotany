package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.BaubleBoxItem;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class ElvenKingRing extends BaubleItem {
    public ElvenKingRing(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player)
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() instanceof BaubleBoxItem) {
                    SimpleContainer boxInventory = BaubleBoxItem.getInventory(player.getInventory().getItem(i));
                    for (int j = 0; j < getSize(); j++) {
                        if (boxInventory.getItem(j).getItem() instanceof BaubleItem item && !(boxInventory.getItem(j).getItem() instanceof ElvenKingRing))
                            item.onWornTick(boxInventory.getItem(j), entity);
                    }
                    break;
                }
            }
    }

    public int getSize() {
        return 3;
    }
}

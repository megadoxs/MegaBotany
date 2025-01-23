package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import vazkii.botania.common.item.equipment.bauble.GreaterBandOfManaItem;

public class MasterBandOfMana extends GreaterBandOfManaItem {
    private static final int MAX_MANA = 2000000000;

    public MasterBandOfMana(Properties props) {
        super(props);
    }

    @Override
    public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
        output.accept(this);

        ItemStack full = new ItemStack(this);
        setMana(full, MAX_MANA);
        output.accept(full);
    }

    public static class MasterManaImpl extends BandOfManaItem.ManaItemImpl {
        public MasterManaImpl(ItemStack stack) {
            super(stack);
        }

        @Override
        public int getMaxMana() {
            return MAX_MANA * stack.getCount();
        }
    }

}

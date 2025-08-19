package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import io.github.megadoxs.megabotany.common.MegaBotany;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.helper.PlayerHelper;
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

    @Override
    public void onEquipped(ItemStack stack, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("mana") && tag.getInt("mana") == MAX_MANA) {
                PlayerHelper.grantCriterion(player, new ResourceLocation(MegaBotany.MOD_ID, "mana_master"), "code_triggered");
            }
        }
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

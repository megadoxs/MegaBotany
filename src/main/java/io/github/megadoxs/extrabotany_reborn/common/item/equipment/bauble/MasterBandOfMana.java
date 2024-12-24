package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.xplat.XplatAbstractions;

import javax.annotation.Nullable;
import java.util.Optional;

public class MasterBandOfMana extends BaubleItem {

    private static final String TAG_MANA = "mana";
    private static final int MAX_MANA = 2000000000;

    public MasterBandOfMana(Properties props) {
        super(props);
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(ManaBarTooltip.fromManaItem(stack));
    }

    protected static void setMana(ItemStack stack, int mana) {
        if (mana > 0) {
            ItemNBTHelper.setInt(stack, TAG_MANA, mana);
        } else {
            ItemNBTHelper.removeEntry(stack, TAG_MANA);
        }

    }

    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public int getBarWidth(ItemStack stack) {
        ManaItem manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
        return Math.round(13.0F * ManaBarTooltip.getFractionForDisplay(manaItem));
    }

    public int getBarColor(ItemStack stack) {
        ManaItem manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
        return Mth.hsvToRgb(ManaBarTooltip.getFractionForDisplay(manaItem) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ManaItemProvider(stack);
    }


    public static class ManaItemImpl implements ManaItem {
        protected final ItemStack stack;

        public ManaItemImpl(ItemStack stack) {
            this.stack = stack;
        }

        public int getMana() {
            return ItemNBTHelper.getInt(this.stack, TAG_MANA, 0) * this.stack.getCount();
        }

        public int getMaxMana() {
            return MAX_MANA * this.stack.getCount();
        }

        public void addMana(int mana) {
            MasterBandOfMana.setMana(this.stack, Math.min(this.getMana() + mana, this.getMaxMana()) / this.stack.getCount());
        }

        public boolean canReceiveManaFromPool(BlockEntity pool) {
            return true;
        }

        public boolean canReceiveManaFromItem(ItemStack otherStack) {
            return true;
        }

        public boolean canExportManaToPool(BlockEntity pool) {
            return true;
        }

        public boolean canExportManaToItem(ItemStack otherStack) {
            return true;
        }

        public boolean isNoExport() {
            return false;
        }
    }

    private static class ManaItemProvider implements ICapabilityProvider {

        private final LazyOptional<ManaItem> manaItemOptional;

        public ManaItemProvider(ItemStack stack) {
            this.manaItemOptional = LazyOptional.of(() -> new ManaItemImpl(stack));
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            return BotaniaForgeCapabilities.MANA_ITEM.orEmpty(capability, manaItemOptional);
        }
    }

}

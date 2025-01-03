package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.handler.BotaniaSounds;

public class OmnivioletBlockEntity extends GeneratingFlowerBlockEntity {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final String TAG_MANA_GENERATION = "manaGeneration";
    private static final int RANGE = 1;
    int burnTime = 0;
    int manaGeneration = 0;
    float efficiency;

    public OmnivioletBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.OMNIVIOLET, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (burnTime > 0) {
            burnTime--;
            addMana(manaGeneration);
            sync();
        }

        if (burnTime == 0 && getMana() < getMaxMana()) {
            for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                if (EnchantmentHelper.getEnchantments(item.getItem()).isEmpty())
                    continue;

                if (burnTime != 0) {
                    break;
                }

                burnTime = 600;

                efficiency = 1;
                for (BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
                    if (EnchantmentTableBlock.isValidBookShelf(getLevel(), getBlockPos(), blockpos)) {
                        efficiency += 0.5F * getLevel().getBlockState(getBlockPos().offset(blockpos)).getEnchantPowerBonus(getLevel(), getBlockPos().offset(blockpos));
                    }
                }
                // needs balancing
                manaGeneration = EnchantmentHelper.getEnchantments(item.getItem()).values().stream().mapToInt(integer -> (int) (integer * efficiency)).sum() / 2;
                item.getItem().shrink(1);

                getLevel().playSound(null, getEffectivePos(), BotaniaSounds.endoflame, SoundSource.BLOCKS, 0.2F, 1F);
                setChanged();
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 5000;
    }

    @Override
    public int getColor() {
        return 0xEE82EE;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putInt(TAG_BURN_TIME, burnTime);
        cmp.putInt(TAG_MANA_GENERATION, manaGeneration);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(TAG_BURN_TIME);
        manaGeneration = cmp.getInt(TAG_MANA_GENERATION);
    }
}

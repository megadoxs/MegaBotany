package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.flower.generating.HydroangeasBlockEntity;

public class BellFlowerBlockEntity extends GeneratingFlowerBlockEntity { //TODO make this flower generate mana from sound and transfer its current ability to a new flower
    public static final int RANGE = 5;

    private int passiveDecayTicks;

    public BellFlowerBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.BELL_FLOWER, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!getLevel().isClientSide) {
            if (ticksExisted % 5 == 0 && getMana() < getMaxMana() && getEffectivePos().getY() > level.getSeaLevel() && shouldProduce()) {
                if (getLevel().isThundering())
                    addMana((int) Math.pow(2, (double) getEffectivePos().getY() / 100));
                else
                    addMana((int) Math.pow(2, (double) getEffectivePos().getY() / 100) * 2);
                sync();
            }

            if (++passiveDecayTicks > HydroangeasBlockEntity.DECAY_TIME) {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
            setChanged();
        }
    }

    public boolean shouldProduce() {
        for (BlockPos blockpos : BlockPos.betweenClosed(getEffectivePos().offset(-RANGE, 0, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1))) {
            if (!level.getBlockState(blockpos).isAir()) {
                if (!blockpos.equals(getBlockPos()))
                    return false;
            }
        }
        return true;
    }

    @Override
    public int getMaxMana() {
        return 1000;
    }

    @Override
    public int getColor() {
        return 0xFFFF99;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return null;
    }
}

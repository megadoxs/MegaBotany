package io.github.megadoxs.extrabotany_reborn.common.block.flower.generating;

import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class SunshineLilyBlockEntity extends GeneratingFlowerBlockEntity {
    public static final String TAG_PASSIVE_DECAY_TICKS = "passiveDecayTicks";

    public static final int DECAY_TIME = 72000;

    //TODO checking for this every tick seems bad
    private boolean hasSkyVision;
    private int passiveDecayTicks;

    public SunshineLilyBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.SUNSHINE_LILY, pos, state);
    }

    @Override
    public int getMaxMana() {
        return 200;
    }

    @Override
    public int getColor() {
        return 0xFFA500;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return null;
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!getLevel().isClientSide && getLevel().isDay() && hasSkyVision(getLevel(), getBlockPos()) &&!getLevel().isRainingAt(getBlockPos()) && getMana() < getMaxMana()) {
            if (passiveDecayTicks % 5 == 0)
                addMana(1);

            if (++passiveDecayTicks > DECAY_TIME) {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
        }
    }

    private boolean hasSkyVision(Level level, BlockPos pos) {
        int maxHeight = level.getHeight();

        for (int y = pos.getY() + 1; y < maxHeight; y++) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState blockState = level.getBlockState(checkPos);

            if (!blockState.isAir() && blockState.getLightBlock(level, checkPos) > 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);
        passiveDecayTicks = cmp.getInt(TAG_PASSIVE_DECAY_TICKS);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);
        cmp.putInt(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);
    }


}

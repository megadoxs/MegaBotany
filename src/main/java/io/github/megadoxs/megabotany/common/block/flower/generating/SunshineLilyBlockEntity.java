package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.flower.generating.HydroangeasBlockEntity;

public class SunshineLilyBlockEntity extends GeneratingFlowerBlockEntity {
    private int passiveDecayTicks;

    public SunshineLilyBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.SUNSHINE_LILY, pos, state);
    }

    public SunshineLilyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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

        if (!getLevel().isClientSide) {
            if (getMana() < getMaxMana() && shouldProduce() && passiveDecayTicks % 5 == 0) {
                addMana(1);
                sync();
            }

            if (++passiveDecayTicks > HydroangeasBlockEntity.DECAY_TIME) {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
        }
    }

    public boolean shouldProduce() {
        return getLevel().isDay() && getLevel().canSeeSky(getBlockPos()) && !getLevel().isRaining() && !getLevel().isThundering();
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);
        passiveDecayTicks = cmp.getInt(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);
        cmp.putInt(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);
    }
}
package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.GeminiOrchidSourceRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.flower.generating.HydroangeasBlockEntity;

import java.util.Optional;

public class GeminiOrchidBlockEntity extends GeneratingFlowerBlockEntity {
    private static final BlockPos[] OFFSETS = {new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, -1, 0)};

    private int passiveDecayTicks;

    public GeminiOrchidBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.GEMINI_ORCHID, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!getLevel().isClientSide) {
            if (++passiveDecayTicks > HydroangeasBlockEntity.DECAY_TIME) {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                    return;
                }
            }

            Integer maxTemp = null;
            Integer minTemp = null;
            for (BlockPos offset : OFFSETS) {
                BlockState blockState = getLevel().getBlockState(getBlockPos().offset(offset));
                FluidState fluidState = getLevel().getFluidState(getBlockPos().offset(offset));
                if (!fluidState.isEmpty()) {
                    maxTemp = maxTemp != null ? Math.max(maxTemp, fluidState.getFluidType().getTemperature()) : fluidState.getFluidType().getTemperature();
                    minTemp = minTemp != null ? Math.min(minTemp, fluidState.getFluidType().getTemperature()) : fluidState.getFluidType().getTemperature();
                } else if (!blockState.isAir()) {
                    SimpleContainer container = new SimpleContainer(1);
                    container.setItem(0, blockState.getBlock().asItem().getDefaultInstance());

                    Optional<GeminiOrchidSourceRecipe> recipe = getLevel().getRecipeManager().getRecipeFor(GeminiOrchidSourceRecipe.Type.INSTANCE, container, getLevel());
                    if (recipe.isPresent()) {
                        maxTemp = maxTemp != null ? Math.max(maxTemp, recipe.get().getTemperature()) : recipe.get().getTemperature();
                        minTemp = minTemp != null ? Math.min(minTemp, recipe.get().getTemperature()) : recipe.get().getTemperature();
                    }
                }
            }

            if (maxTemp != null) {
                addMana(maxTemp - minTemp);
                sync();
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 250;
    }

    @Override
    public int getColor() {
        return 0xFFFFE0;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return null;
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
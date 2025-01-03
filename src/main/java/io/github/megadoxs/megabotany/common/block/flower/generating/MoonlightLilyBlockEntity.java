package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MoonlightLilyBlockEntity extends SunshineLilyBlockEntity {
    public MoonlightLilyBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.MOONLIGHT_LILY, pos, state);
    }

    @Override
    public int getColor() {
        return 13776605;
    }

    @Override
    public boolean shouldProduce() {
        return getLevel().isNight() && getLevel().canSeeSky(getBlockPos()) && !getLevel().isRaining() && !getLevel().isThundering();
    }
}

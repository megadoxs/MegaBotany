package io.github.megadoxs.megabotany.common.block.block_entity;

import io.github.megadoxs.megabotany.common.block.MegaBotanyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaBlock;

public class SpiritPortalBlock extends BotaniaBlock implements EntityBlock {
    public SpiritPortalBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BotaniaStateProperties.ALFPORTAL_STATE);
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SpiritPortalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MegaBotanyBlockEntities.SPIRIT_PORTAL.get(), SpiritPortalBlockEntity::commonTick);
    }
}

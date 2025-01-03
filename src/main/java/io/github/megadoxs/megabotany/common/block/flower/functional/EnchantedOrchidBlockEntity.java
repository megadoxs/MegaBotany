package io.github.megadoxs.megabotany.common.block.flower.functional;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BlackLotusItem;

public class EnchantedOrchidBlockEntity extends FunctionalFlowerBlockEntity {
    private static final int MANA_COST = 10000;

    public EnchantedOrchidBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.ENCHANTED_ORCHID, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!getLevel().isClientSide && getMana() >= MANA_COST) {
            BlockPos pos = getEffectivePos().below();
            BlockState state = getLevel().getBlockState(pos);

            if (state.is(Blocks.GRASS_BLOCK)) {
                boolean fuel = false;

                for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos()))) {
                    if (item.getItem().getItem() instanceof BlackLotusItem) {
                        fuel = true;
                        item.getItem().shrink(1);
                        break;
                    }
                }

                if (!fuel)
                    return;

                getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
                getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                getLevel().setBlockAndUpdate(pos, BotaniaBlocks.enchantedSoil.defaultBlockState());
                addMana(-MANA_COST);
                sync();
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 10000;
    }

    @Override
    public int getColor() {
        return 0x4B0082;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), 0);
    }
}

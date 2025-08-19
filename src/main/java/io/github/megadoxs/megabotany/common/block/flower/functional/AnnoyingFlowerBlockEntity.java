package io.github.megadoxs.megabotany.common.block.flower.functional;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.List;
import java.util.stream.StreamSupport;

public class AnnoyingFlowerBlockEntity extends FunctionalFlowerBlockEntity {
    public static final int RANGE = 5;
    public static final int MANA_COST = 2500;

    public AnnoyingFlowerBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.ANNOYING_FLOWER, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (getLevel() instanceof ServerLevel serverLevel && redstoneSignal == 0 && getMana() >= MANA_COST) {
            boolean isRaining = serverLevel.isRainingAt(getEffectivePos());
            if (((!isRaining && ticksExisted % 200 == 0) || (isRaining && ticksExisted % 150 == 0)) && StreamSupport.stream(BlockPos.betweenClosed(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE, RANGE, RANGE)).spliterator(), false).anyMatch(pos -> getLevel().getBlockState(pos).is(Blocks.WATER))) {
                for (ItemStack stack : serverLevel.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING).getRandomItems(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY))) {
                    Vec3 pos = getEffectivePos().getCenter();
                    ItemEntity itemEntity = new ItemEntity(serverLevel, pos.x, pos.y + 0.25, pos.z, stack);
                    itemEntity.setDeltaMovement(0, 0.1, 0);
                    serverLevel.addFreshEntity(itemEntity);
                }
                addMana(-MANA_COST);
                sync();
            }
        }

    }

    @Override
    public int getMaxMana() {
        return MANA_COST;
    }

    @Override
    public int getColor() {
        return 0x274A00;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }
}

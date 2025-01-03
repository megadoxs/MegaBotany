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

public class AnnoyingFlowerBlockEntity extends FunctionalFlowerBlockEntity {
    public static final String TAG_FRIED_CHICKEN = "friedChicken";

    public static final int RANGE = 5;
    public static final int MANA_COST = 1000;

    private boolean friedChicken = false;

    public AnnoyingFlowerBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.ANNOYING_FLOWER, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!friedChicken) {
            for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos()))) {
                if (item.getItem().is(MegaBotanyItems.FRIED_CHICKEN.get())) {
                    friedChicken = true;
                    setChanged();
                    item.getItem().shrink(1);
                    break;
                }
            }
        }

        if (getMana() >= MANA_COST) {
            boolean water = false;
            for (BlockPos blockpos : BlockPos.betweenClosed(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1))) {
                if (getLevel().getBlockState(blockpos).is(Blocks.WATER)) {
                    water = true;
                    break;
                }
            }

            if (water && getLevel() instanceof ServerLevel serverLevel && ticksExisted % 200 == 0) {
                LootParams ctx = new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY);
                List<ItemStack> loot;
                if (friedChicken) {
                    loot = serverLevel.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING_TREASURE).getRandomItems(ctx);
                    friedChicken = false;
                } else
                    loot = serverLevel.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING).getRandomItems(ctx);
                for (ItemStack stack : loot) {
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
        return 2000;
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
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putBoolean(TAG_FRIED_CHICKEN, friedChicken);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        friedChicken = cmp.getBoolean(TAG_FRIED_CHICKEN);
    }
}

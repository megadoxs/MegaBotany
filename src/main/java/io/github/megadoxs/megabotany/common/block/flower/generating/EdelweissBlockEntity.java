package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class EdelweissBlockEntity extends GeneratingFlowerBlockEntity {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final int RANGE = 1;

    private int burnTime = 0;

    public EdelweissBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.EDELWEISS, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (burnTime > 0) {
            burnTime--;
            addMana(10);
            sync();
        }

        if (getMana() < getMaxMana() && burnTime == 0) {
            for (SnowGolem snowman : getLevel().getEntitiesOfClass(SnowGolem.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                if (burnTime != 0) {
                    break;
                }

                snowman.kill();
                burnTime = 160;
                setChanged();
            }
        }

    }

    @Override
    public int getMaxMana() {
        return 25000;
    }

    @Override
    public int getColor() {
        return 0X4169E1;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putInt(TAG_BURN_TIME, burnTime);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(TAG_BURN_TIME);
    }
}

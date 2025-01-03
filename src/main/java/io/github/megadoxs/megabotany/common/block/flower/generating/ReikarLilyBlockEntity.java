package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class ReikarLilyBlockEntity extends GeneratingFlowerBlockEntity {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final String TAG_COOLDOWN = "cooldown";

    private int burnTime = 0, cooldown = 0;

    public ReikarLilyBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.REIKAR_LILY, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (cooldown > 0) {
            cooldown--;
            setChanged();
        }

        if (burnTime > 0) {
            burnTime--;
            addMana(burnTime);
            sync();
        }
    }

    public void onLightningStrike() {
        cooldown = 2000;
        burnTime = 1000;
        addMana(10000);
        sync();
    }

    public boolean isInCooldown() {
        return cooldown > 0;
    }

    @Override
    public int getMaxMana() {
        return 12000;
    }

    @Override
    public int getColor() {
        return 0x87CEFA;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return null;
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putInt(TAG_BURN_TIME, burnTime);
        cmp.putInt(TAG_COOLDOWN, cooldown);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(TAG_BURN_TIME);
        cooldown = cmp.getInt(TAG_COOLDOWN);
    }
}

package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.StonesiaRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.Optional;

public class StonesiaBlockEntity extends GeneratingFlowerBlockEntity {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final String TAG_MANA_GENERATION = "manaGeneration";
    private static final int RANGE = 0;

    private int burnTime = 0;
    private int manaGeneration = 0;

    public StonesiaBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.STONESIA, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (burnTime > 0) {
            burnTime--;
            addMana(manaGeneration);
            sync();
        }

        if (!getLevel().isClientSide) {
            for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                if (burnTime != 0)
                    break;

                SimpleContainer container = new SimpleContainer(1);
                container.setItem(0, item.getItem());

                Optional<StonesiaRecipe> recipe = this.level.getRecipeManager().getRecipeFor(StonesiaRecipe.Type.INSTANCE, container, level);
                if (recipe.isPresent()) {
                    manaGeneration = recipe.get().getMana();
                    burnTime = recipe.get().getBurnTime();
                    setChanged();
                    item.getItem().shrink(1);
                }
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 800;
    }

    @Override
    public int getColor() {
        return 0x778899;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putInt(TAG_BURN_TIME, burnTime);
        cmp.putInt(TAG_MANA_GENERATION, manaGeneration);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(TAG_BURN_TIME);
        manaGeneration = cmp.getInt(TAG_MANA_GENERATION);
    }
}

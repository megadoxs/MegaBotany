package io.github.megadoxs.megabotany.common.item.lens;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.item.lens.KindleLens;

import java.util.Optional;

public class SmeltLens extends KindleLens {
    private final int manaCost = 1; //TODO set a real cost per smelting tick

    @Override
    public void updateBurst(ManaBurst burst, ItemStack stack) {
        super.updateBurst(burst, stack);
        ThrowableProjectile entity = burst.entity();
        if (entity.level().isClientSide) {
            return;
        }

        for (ItemEntity itemEntity : entity.level().getEntitiesOfClass(ItemEntity.class, new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1))) {
            Optional<SmeltingRecipe> recipe = burst.entity().level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemEntity.getItem()), burst.entity().level());

            recipe.ifPresent(smeltingRecipe -> {
                ItemStack itemStack = smeltingRecipe.getResultItem(RegistryAccess.EMPTY);
                int items = 0;
                int cookingTime = smeltingRecipe.getCookingTime();

                for (int i = 0; i < itemEntity.getItem().getCount(); i++) {
                    if (burst.getMana() - cookingTime * manaCost >= 0) {
                        burst.setMana(burst.getMana() - cookingTime * manaCost);
                        items++;
                    }
                }

                if (!burst.isFake() && items != 0) {
                    itemStack.setCount(items);
                    burst.entity().level().addFreshEntity(new ItemEntity(burst.entity().level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemStack));
                    itemEntity.getItem().shrink(items);
                }
            });
        }
    }
}

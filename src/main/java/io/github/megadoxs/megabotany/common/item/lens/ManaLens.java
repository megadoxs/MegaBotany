package io.github.megadoxs.megabotany.common.item.lens;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.item.lens.Lens;
import vazkii.botania.xplat.XplatAbstractions;

public class ManaLens extends Lens {
    @Override
    public void updateBurst(ManaBurst burst, ItemStack stack) {
        ThrowableProjectile entity = burst.entity();
        if (entity.level().isClientSide) {
            return;
        }

        for (ItemEntity itemEntity : entity.level().getEntitiesOfClass(ItemEntity.class, new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1))) {
            for (var recipe : BotaniaRecipeTypes.getRecipes(entity.level(), BotaniaRecipeTypes.MANA_INFUSION_TYPE).values()) {
                if (recipe.matches(itemEntity.getItem()) && (recipe.getRecipeCatalyst() == null || recipe.getRecipeCatalyst().test(burst.entity().level().getBlockState(burst.getBurstSourceBlockPos().below())))) {
                    int items = 0;
                    int manaCost = recipe.getManaToConsume();

                    for (int i = 0; i < itemEntity.getItem().getCount(); i++) {
                        if (burst.getMana() - manaCost >= 0) {
                            burst.setMana(burst.getMana() - manaCost);
                            items++;
                        }
                    }

                    if (!burst.isFake() && items != 0) {
                        ItemStack result = recipe.getResultItem(RegistryAccess.EMPTY);
                        result.setCount(items);
                        burst.entity().level().addFreshEntity(new ItemEntity(burst.entity().level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), result));
                        itemEntity.getItem().shrink(items);
                    }
                    break;
                }
            }

            var manaItem = XplatAbstractions.INSTANCE.findManaItem(itemEntity.getItem());
            if (manaItem != null) {
                int mana = Math.min(burst.getMana(), manaItem.getMaxMana() - manaItem.getMana());
                burst.setMana(burst.getMana() - mana);
                if (!burst.isFake())
                    manaItem.addMana(mana);
            }
        }
    }
}

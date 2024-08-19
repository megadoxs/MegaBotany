package io.github.megadoxs.extrabotany_reborn.common.datagen.botania;

import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.data.recipes.ManaInfusionProvider;

import java.util.function.Consumer;

public class ManaInfusionRecipeGenerator extends ManaInfusionProvider {
    public ManaInfusionRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe(this.id("nightmare_fuel"), new ItemStack(ModItems.NIGHTMARE_FUEL.get()), Ingredient.of(Items.COAL), 2000));
    }
}

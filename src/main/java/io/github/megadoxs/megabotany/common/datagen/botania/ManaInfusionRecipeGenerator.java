package io.github.megadoxs.megabotany.common.datagen.botania;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
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
        consumer.accept(new FinishedRecipe(this.id("nightmare_fuel"), new ItemStack(MegaBotanyItems.NIGHTMARE_FUEL.get()), Ingredient.of(Items.COAL), 2000));
    }
}

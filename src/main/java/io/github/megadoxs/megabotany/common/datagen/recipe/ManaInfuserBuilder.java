package io.github.megadoxs.megabotany.common.datagen.recipe;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ManaInfuserBuilder implements RecipeBuilder {

    private final ItemStack result;

    private final Ingredient ingredient;

    private final int mana;

    public ManaInfuserBuilder(ItemStack output, Ingredient input, int mana) {
        this.result = output;
        this.ingredient = input;
        this.mana = mana;
    }

    public static ManaInfuserBuilder recipe(ItemStack output, Ingredient input, int mana) {
        return new ManaInfuserBuilder(output, input, mana);
    }

    @Override
    public ManaInfuserBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
        return null;
    }

    @Override
    public ManaInfuserBuilder group(@Nullable String s) {
        return null;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation pRecipeId) {
        //consumer.accept(new (pRecipeId, this.result, this.ingredient, this.mana, null, null));
    }
}

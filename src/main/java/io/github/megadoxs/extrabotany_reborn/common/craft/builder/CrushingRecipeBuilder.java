package io.github.megadoxs.extrabotany_reborn.common.craft.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static io.github.megadoxs.extrabotany_reborn.common.craft.ModRecipes.CRUSHING_SERIALIZER;

public class CrushingRecipeBuilder implements RecipeBuilder {

    private final List<Ingredient> ingredients = Lists.newArrayList();;
    private final ItemStack result;
    private int strikes;

    private CrushingRecipeBuilder(ItemStack results){
        this.result = results;
    }

    public static CrushingRecipeBuilder crushing(ItemStack result){
        return new CrushingRecipeBuilder(result);
    }

    public static CrushingRecipeBuilder crushing(ItemLike item){
        return new CrushingRecipeBuilder(new ItemStack(item));
    }

    public static CrushingRecipeBuilder crushing(ItemLike item, int count){
        return new CrushingRecipeBuilder(new ItemStack(item, count));
    }

    public CrushingRecipeBuilder addIngredients(Ingredient ...ingredients){
        this.ingredients.addAll(List.of(ingredients));
        return this;
    }

    public CrushingRecipeBuilder addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
        return this;
    }

    public CrushingRecipeBuilder addIngredient(ItemLike item){
        this.ingredients.add(Ingredient.of(item));
        return this;
    }

    public CrushingRecipeBuilder strikes(int strikes){
        this.strikes = strikes;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String s, CriterionTriggerInstance criterionTriggerInstance) {
        return null;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        return null;
    }

    //only used for the name of the recipe
    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer) {
        this.save(consumer, getDefaultRecipeId(this.getResult()));
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation resourceLocation) {
        this.isValid();
        consumer.accept(new CrushingRecipeBuilder.Result(this.result, this.ingredients, Math.max(this.strikes, 0), resourceLocation));
    }

    public void isValid(){
        if (this.ingredients.isEmpty())
            throw new IllegalStateException("ingredients list for the crafting recipe cannot be empty");
    }

    private static ResourceLocation getDefaultRecipeId(ItemLike item){
        return new ResourceLocation("extrabotany_reborn", "crushing/" + item.asItem());
    }

    protected static class Result implements FinishedRecipe {

        private final ItemStack result;
        private final List<Ingredient> ingredients;
        private final int strikes;
        private final ResourceLocation id;

        protected Result(ItemStack result, List<Ingredient> ingredients, int strikes, ResourceLocation id) {
            this.result = result;
            this.ingredients = ingredients;
            this.strikes = strikes;
            this.id = id;
        }

        public void serializeRecipeData(JsonObject pJson) {
            JsonArray $$1 = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                $$1.add(ingredient.toJson());
            }
            pJson.add("ingredients", $$1);

            if (this.strikes > 0)
                pJson.addProperty("strikes", this.strikes);

            JsonObject $$2 = new JsonObject();
            $$2.addProperty("item", String.valueOf(ForgeRegistries.ITEMS.getKey(result.getItem())));
            $$2.addProperty("count", result.getCount());
            pJson.add("result", $$2);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CRUSHING_SERIALIZER.get();
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

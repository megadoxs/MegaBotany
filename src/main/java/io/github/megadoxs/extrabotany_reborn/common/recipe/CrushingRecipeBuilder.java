package io.github.megadoxs.extrabotany_reborn.common.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.megadoxs.extrabotany_reborn.common.util.ByProduct;
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static io.github.megadoxs.extrabotany_reborn.common.recipe.ModRecipes.CRUSHING_SERIALIZER;

public class CrushingRecipeBuilder implements RecipeBuilder {

    private final List<Ingredient> ingredients = Lists.newArrayList();;
    private final ItemStack result;
    private final List<ByProduct> byProducts = Lists.newArrayList();
    private int hits;
    private int mana;

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

    public CrushingRecipeBuilder addByProducts(ItemStack... itemStacks){
        for (ItemStack itemStack : itemStacks) {
            this.byProducts.add(new ByProduct(itemStack, 1f));
        }
        return this;
    }

    public CrushingRecipeBuilder addByProducts(ByProduct... byProducts){
        this.byProducts.addAll(Arrays.asList(byProducts));
        return this;
    }

    public CrushingRecipeBuilder addByProduct(ItemLike item){
        this.byProducts.add(new ByProduct(new ItemStack(item, 1), 1f));
        return this;
    }

    public CrushingRecipeBuilder addByProduct(ItemLike item, float chance){
        this.byProducts.add(new ByProduct(new ItemStack(item, 1), chance));
        return this;
    }

    public CrushingRecipeBuilder addByProduct(ItemLike item, int count){
        this.byProducts.add(new ByProduct(new ItemStack(item, count), 1f));
        return this;
    }

    public CrushingRecipeBuilder addByProduct(ItemLike item, int count, float chance){
        this.byProducts.add(new ByProduct(new ItemStack(item, count), chance));
        return this;
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

    public CrushingRecipeBuilder hits(int hits){
        this.hits = hits;
        return this;
    }

    public CrushingRecipeBuilder mana(int mana){
        this.mana = mana;
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
        consumer.accept(new CrushingRecipeBuilder.Result(this.result, this.byProducts, this.ingredients, Math.max(this.hits, 0), Math.max(this.mana, 0), resourceLocation));
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
        private final List<ByProduct> byProducts;
        private final List<Ingredient> ingredients;
        private final int hits;
        private final int mana;
        private final ResourceLocation id;

        protected Result(ItemStack result, List<ByProduct> byProducts, List<Ingredient> ingredients, int hits, int mana, ResourceLocation id) {
            this.result = result;
            this.byProducts = byProducts;
            this.ingredients = ingredients;
            this.hits = hits;
            this.mana = mana;
            this.id = id;
        }

        // currently doesn't support giving nbt to item completely...
        public void serializeRecipeData(JsonObject pJson) {
            JsonArray $$1 = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                $$1.add(ingredient.toJson());
            }
            pJson.add("ingredients", $$1);

            if (this.hits > 0)
                pJson.addProperty("hits", this.hits);
            if (this.mana > 0)
                pJson.addProperty("hits", this.mana);

            JsonObject $$2 = new JsonObject();
            $$2.addProperty("item", String.valueOf(ForgeRegistries.ITEMS.getKey(result.getItem())));
            $$2.addProperty("count", result.getCount());
            pJson.add("result", $$2);

            if(!byProducts.isEmpty()){
                JsonArray $$3 = new JsonArray();
                this.byProducts.forEach(byProduct -> {
                    JsonObject $$4 = new JsonObject();
                    $$4.addProperty("item", String.valueOf(ForgeRegistries.ITEMS.getKey(byProduct.itemStack().getItem())));
                    $$4.addProperty("count", byProduct.itemStack().getCount());
                    $$4.addProperty("chance", byProduct.chance());
                    $$3.add($$4);
                });
                pJson.add("by_products", $$3);
            }
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CRUSHING_SERIALIZER.get();
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        // don't really need them now, do I?
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

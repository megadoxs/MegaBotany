package io.github.megadoxs.megabotany.common.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.data.recipes.RunicAltarProvider;

import java.util.function.Consumer;

public class RunicAltarRecipeGenerator extends RunicAltarProvider {
    public RunicAltarRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    protected ResourceLocation id(String s) {
        return ResourceLocationHelper.prefix("runic_altar/" + s);
    }

    @Override
    public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {

    }

    protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack output;
        private final int mana;
        private final Ingredient[] inputs;

        protected FinishedRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
            this.id = id;
            this.output = output;
            this.mana = mana;
            this.inputs = inputs;
        }

        public void serializeRecipeData(JsonObject json) {
            json.add("output", ItemNBTHelper.serializeStack(this.output));
            JsonArray ingredients = new JsonArray();
            Ingredient[] var3 = this.inputs;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Ingredient ingr = var3[var5];
                ingredients.add(ingr.toJson());
            }

            json.addProperty("mana", this.mana);
            json.add("ingredients", ingredients);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return BotaniaRecipeTypes.RUNE_SERIALIZER;
        }

        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

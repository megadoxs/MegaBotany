package io.github.megadoxs.megabotany.common.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.megadoxs.megabotany.common.crafting.recipe.SpiritTradeRecipe;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class SpiritTradeRecipeGenerator extends BotaniaRecipeProvider {
    public SpiritTradeRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe(id("fuel"), new ItemStack(MegaBotanyItems.SPIRIT_FUEL.get()), new ItemStack(MegaBotanyItems.NIGHTMARE_FUEL.get()), Ingredient.of(Items.COAL)));
    }

    @Override
    public String getName() {
        return "MegaBotany spirit trade recipes";
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation("megabotany:spirit_trade/" + path);
    }

    protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
        private final ResourceLocation id;
        private final List<Ingredient> inputs;
        private final List<ItemStack> dayOutputs;
        private final List<ItemStack> nightOutputs;

        public FinishedRecipe(ResourceLocation id, ItemStack dayOutput, ItemStack nightOutput, Ingredient... inputs) {
            this(id, Arrays.asList(inputs), Collections.singletonList(dayOutput), Collections.singletonList(nightOutput));
        }

        protected FinishedRecipe(ResourceLocation id, List<Ingredient> inputs, List<ItemStack> dayOutputs, List<ItemStack> nightOutputs) {
            this.id = id;
            this.inputs = inputs;
            this.dayOutputs = dayOutputs;
            this.nightOutputs = nightOutputs;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray in = new JsonArray();
            for (Ingredient ingr : inputs) {
                in.add(ingr.toJson());
            }

            JsonArray dayOutputs = new JsonArray();
            for (ItemStack s : this.dayOutputs) {
                dayOutputs.add(ItemNBTHelper.serializeStack(s));
            }

            JsonArray nightOutputs = new JsonArray();
            for (ItemStack s : this.nightOutputs) {
                nightOutputs.add(ItemNBTHelper.serializeStack(s));
            }

            json.add("ingredients", in);
            json.add("day_output", dayOutputs);
            json.add("night_output", nightOutputs);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SpiritTradeRecipe.Serializer.INSTANCE;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

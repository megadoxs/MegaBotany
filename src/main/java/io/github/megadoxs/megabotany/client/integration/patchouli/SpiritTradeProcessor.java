package io.github.megadoxs.megabotany.client.integration.patchouli;

import com.google.common.collect.ImmutableList;
import io.github.megadoxs.megabotany.common.crafting.recipe.SpiritTradeRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;
import java.util.stream.Collectors;

public class SpiritTradeProcessor implements IComponentProcessor {
    private List<SpiritTradeRecipe> recipes;
    private int longestIngredientSize, mostInputs, mostOutputs;
    private boolean isDay;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        ImmutableList.Builder<SpiritTradeRecipe> builder = ImmutableList.builder();
        isDay = variables.get("is_day").asBoolean();
        for (IVariable s : variables.get("recipes").asListOrSingleton()) {
            SpiritTradeRecipe recipe = PatchouliUtils.getRecipe(level, SpiritTradeRecipe.Type.INSTANCE, new ResourceLocation(s.asString()));
            if (recipe != null) {
                builder.add(recipe);
            }
        }
        recipes = builder.build();
        for (SpiritTradeRecipe recipe : recipes) {
            List<Ingredient> inputs = recipe.getIngredients();
            for (Ingredient ingredient : inputs) {
                int length = ingredient.getItems().length;
                if (length > longestIngredientSize) {
                    longestIngredientSize = length;
                }
            }
            if (inputs.size() > mostInputs) {
                mostInputs = inputs.size();
            }
            if (recipe.getOutputs(isDay).size() > mostOutputs) {
                mostOutputs = recipe.getOutputs(isDay).size();
            }
        }
    }

    @Override
    public IVariable process(Level level, String key) {
        if (recipes.isEmpty()) {
            return null;
        }
        switch (key) {
            case "heading":
                return IVariable.from(recipes.get(0).getOutputs(isDay).get(0).getHoverName());
            case "icon":
                return IVariable.wrap(isDay ? "megabotany:textures/gui/sun.png" : "megabotany:textures/gui/moon.png");
            case "portal":
                return IVariable.wrap(isDay ? "megabotany:textures/block/spirit_portal_swirl.png" : "megabotany:textures/block/spirit_portal_swirl_night.png");
            case "tooltip":
                return IVariable.wrap(isDay ? "This trade can only be done during the day." : "This trade can only be done during the night.");
        }
        if (key.startsWith("input")) {
            int index = Integer.parseInt(key.substring(5)) - 1;
            if (index < mostInputs) {
                return interweaveIngredients(index);
            } else {
                return null;
            }
        }
        if (key.startsWith("output")) {
            int index = Integer.parseInt(key.substring(6)) - 1;
            if (index < mostOutputs) {
                return IVariable.wrapList(recipes.stream().map(r -> r.getOutputs(isDay))
                        .map(l -> index < l.size() ? l.get(index) : ItemStack.EMPTY)
                        .map(IVariable::from)
                        .collect(Collectors.toList()));
            }
        }
        return null;
    }

    private IVariable interweaveIngredients(int inputIndex) {
        List<Ingredient> recipes = this.recipes.stream().map(SpiritTradeRecipe::getIngredients).map(ingredients -> {
            if (inputIndex < ingredients.size()) {
                return ingredients.get(inputIndex);
            } else {
                return Ingredient.EMPTY;
            }
        }).collect(Collectors.toList());
        return PatchouliUtils.interweaveIngredients(recipes, longestIngredientSize);
    }
}

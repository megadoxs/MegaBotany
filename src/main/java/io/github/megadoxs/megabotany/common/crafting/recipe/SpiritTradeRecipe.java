package io.github.megadoxs.megabotany.common.crafting.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpiritTradeRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> inputs;
    private final ImmutableList<ItemStack> dayOutputs;
    private final ImmutableList<ItemStack> nightOutputs;

    public SpiritTradeRecipe(ResourceLocation id, ItemStack[] dayOutputs, ItemStack[] nightOutputs, Ingredient... inputs) {
        this.id = id;
        this.dayOutputs = ImmutableList.copyOf(dayOutputs);
        this.nightOutputs = ImmutableList.copyOf(nightOutputs);
        this.inputs = NonNullList.create();
        this.inputs.addAll(Arrays.asList(inputs));
    }

    @Override
    public boolean matches(Container container, Level level) {
        List<Ingredient> inputsMissing = new ArrayList<>(inputs);
        List<ItemStack> stacksToRemove = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (inputsMissing.isEmpty()) {
                break;
            }

            int stackIndex = -1;

            for (int j = 0; j < inputsMissing.size(); j++) {
                Ingredient ingr = inputsMissing.get(j);
                if (ingr.test(stack)) {
                    if (!stacksToRemove.contains(stack)) {
                        stacksToRemove.add(stack);
                    }
                    stackIndex = j;
                    break;
                }
            }

            if (stackIndex != -1) {
                inputsMissing.remove(stackIndex);
            }
        }

        return inputsMissing.isEmpty();
    }

    public List<ItemStack> getOutputs(boolean isDaytime) {
        if (isDaytime) {
            return dayOutputs;
        }
        else {
            return nightOutputs;
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritTradeRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritTradeRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<SpiritTradeRecipe> {
        public static final SpiritTradeRecipe.Type INSTANCE = new SpiritTradeRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<SpiritTradeRecipe> {
        public static final SpiritTradeRecipe.Serializer INSTANCE = new SpiritTradeRecipe.Serializer();

        @Override
        public SpiritTradeRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonElement dayOutput = json.get("day_output");
            JsonElement nightOutput = json.get("night_output");
            List<ItemStack> dayOutputStacks = new ArrayList<>();
            List<ItemStack> nightOutputStacks = new ArrayList<>();
            if (dayOutput.isJsonArray()) {
                for (JsonElement e : dayOutput.getAsJsonArray()) {
                    JsonObject o = GsonHelper.convertToJsonObject(e, "day output stack");
                    dayOutputStacks.add(ShapedRecipe.itemStackFromJson(o));
                }
            } else {
                JsonObject o = GsonHelper.convertToJsonObject(dayOutput, "day output stack");
                dayOutputStacks.add(ShapedRecipe.itemStackFromJson(o));
            }

            if (nightOutput.isJsonArray()) {
                for (JsonElement e : nightOutput.getAsJsonArray()) {
                    JsonObject o = GsonHelper.convertToJsonObject(e, "night output stack");
                    nightOutputStacks.add(ShapedRecipe.itemStackFromJson(o));
                }
            } else {
                JsonObject o = GsonHelper.convertToJsonObject(nightOutput, "night output stack");
                nightOutputStacks.add(ShapedRecipe.itemStackFromJson(o));
            }

            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : GsonHelper.getAsJsonArray(json, "ingredients")) {
                Ingredient ing = Ingredient.fromJson(e);
                if (!ing.isEmpty()) {
                    inputs.add(ing);
                }
            }

            return new SpiritTradeRecipe(id, dayOutputStacks.toArray(new ItemStack[0]), nightOutputStacks.toArray(new ItemStack[0]), inputs.toArray(new Ingredient[0]));
        }

        @Override
        public @Nullable SpiritTradeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient[] inputs = new Ingredient[buf.readVarInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.fromNetwork(buf);
            }
            ItemStack[] dayOutputs = new ItemStack[buf.readVarInt()];
            for (int i = 0; i < dayOutputs.length; i++) {
                dayOutputs[i] = buf.readItem();
            }
            ItemStack[] nightOutputs = new ItemStack[buf.readVarInt()];
            for (int i = 0; i < nightOutputs.length; i++) {
                nightOutputs[i] = buf.readItem();
            }
            return new SpiritTradeRecipe(id, dayOutputs, nightOutputs, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SpiritTradeRecipe recipe) {
            buf.writeVarInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients()) {
                input.toNetwork(buf);
            }
            buf.writeVarInt(recipe.dayOutputs.size());
            for (ItemStack output : recipe.dayOutputs) {
                buf.writeItem(output);
            }
            buf.writeVarInt(recipe.nightOutputs.size());
            for (ItemStack output : recipe.nightOutputs) {
                buf.writeItem(output);
            }
        }
    }
}

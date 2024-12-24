package io.github.megadoxs.extrabotany_reborn.common.craft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CrushingRecipe implements Recipe<Container> {
    private final NonNullList<Ingredient> input;
    private final ItemStack output;
    private final int strikes;
    private final ResourceLocation id;

    public CrushingRecipe(NonNullList<Ingredient> input, ItemStack output, int strikes, ResourceLocation id) {
        this.input = input;
        this.output = output;
        this.strikes = strikes;
        this.id = id;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (level.isClientSide())
            return false;

        ArrayList<ItemStack> containerItems = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            containerItems.add(container.getItem(i));
        }
        containerItems.remove(ItemStack.EMPTY);

        for (Ingredient ingredient : this.input) {
            boolean matches = false;
            for (int j = 0; j < containerItems.size(); j++) {
                if (ingredient.test(containerItems.get(j))) {
                    containerItems.remove(j);
                    matches = true;
                    break;
                }
            }
            if (!matches)
                return false;
        }

        return containerItems.isEmpty();
    }

    // not going to be used
    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public int getStrikes() {
        return strikes;
    }


    public static class Type implements RecipeType<CrushingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "crushing";
    }

    public static class Serializer implements RecipeSerializer<CrushingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ExtraBotany_Reborn.MOD_ID, "crushing");


        @Override
        public CrushingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true, true);

            int strikes = GsonHelper.getAsInt(jsonObject, "strikes");

            return new CrushingRecipe(inputs, result, strikes, resourceLocation);
        }

        @Override
        public @Nullable CrushingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(friendlyByteBuf));
            }

            ItemStack result = friendlyByteBuf.readItem();

            int strikes = friendlyByteBuf.readInt();

            return new CrushingRecipe(inputs, result, strikes, resourceLocation);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, CrushingRecipe crushingRecipe) {
            friendlyByteBuf.writeInt(crushingRecipe.input.size());
            for (Ingredient ingredient : crushingRecipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(crushingRecipe.getResultItem(null));

            friendlyByteBuf.writeInt(crushingRecipe.strikes);
        }
    }
}

package io.github.megadoxs.extrabotany_reborn.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.util.ByProduct;
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
import java.util.List;
import java.util.Random;

public class CrushingRecipe implements Recipe<Container> {
    private final NonNullList<Ingredient> input;
    private final ItemStack output;

    private final List<ByProduct> byProducts;
    private final int hits;
    private final int mana;
    private final ResourceLocation id;

    public CrushingRecipe(NonNullList<Ingredient> input, ItemStack output, List<ByProduct> byProducts, int hits, int mana, ResourceLocation id) {
        this.input = input;
        this.output = output;
        this.byProducts = byProducts;
        this.hits = hits;
        this.mana = mana;
        this.id = id;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if(level.isClientSide())
            return false;

        ArrayList<ItemStack> containerItems = new ArrayList<>();
        for(int i = 0; i < container.getContainerSize(); i++){
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

    public List<ByProduct> getByProducts(){
        return this.byProducts;
    }

    // used to get the result of all recipes
    public List<ItemStack> getOutputs(){
        ArrayList<ItemStack> output = new ArrayList<>();
        Random random = new Random();
        output.add(this.output.copy());
        this.byProducts.forEach( byProduct -> {
            if(random.nextFloat() <= byProduct.chance())
                output.add(byProduct.itemStack().copy());
        });
        return output;
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

    public int getHits(){
        return hits;
    }

    public int getMana(){
        return mana;
    }

    public boolean recipeHasHits(){
        return hits >= 0;
    }

    public boolean recipeHasMana(){
        return mana >= 0;
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
            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true, true);

            JsonArray side_result = GsonHelper.getAsJsonArray(jsonObject, "by_products", new JsonArray());
            List<ByProduct> byProducts = new ArrayList<>();
            for(int i = 0; i < side_result.size(); i++){
                byProducts.add(ByProduct.fromJson(side_result.get(i).getAsJsonObject()));
            }

            int hits = GsonHelper.getAsInt(jsonObject, "hits", 0);

            int mana = GsonHelper.getAsInt(jsonObject, "mana", 0);

            return new CrushingRecipe(inputs, result, byProducts, hits, mana, resourceLocation);
        }

        @Override
        public @Nullable CrushingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(friendlyByteBuf));
            }

            ItemStack result = friendlyByteBuf.readItem();

            List<ByProduct> byProducts = new ArrayList<>();
            int byProductsSize = friendlyByteBuf.readInt();
            for(int i = 0; i < byProductsSize; i++){
                byProducts.add(new ByProduct(friendlyByteBuf.readItem(), friendlyByteBuf.readFloat()));
            }

            int hits = friendlyByteBuf.readInt();

            int mana = friendlyByteBuf.readInt();

            return new CrushingRecipe(inputs, result, byProducts, hits, mana, resourceLocation);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, CrushingRecipe crushingRecipe) {
            friendlyByteBuf.writeInt(crushingRecipe.input.size());
            for(Ingredient ingredient : crushingRecipe.getIngredients()){
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(crushingRecipe.getResultItem(null));

            friendlyByteBuf.writeInt(crushingRecipe.byProducts.size());
            for(ByProduct byProduct : crushingRecipe.getByProducts()){
                friendlyByteBuf.writeItem(byProduct.itemStack());
                friendlyByteBuf.writeFloat(byProduct.chance());
            }

            friendlyByteBuf.writeInt(crushingRecipe.hits);
            friendlyByteBuf.writeInt(crushingRecipe.mana);
        }
    }
}

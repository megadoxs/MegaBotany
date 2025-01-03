package io.github.megadoxs.megabotany.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StonesiaRecipe implements Recipe<Container> {
    private final List<Item> items;
    private final int mana;
    private final int burnTime;
    private final ResourceLocation id;

    public StonesiaRecipe(List<Item> items, int mana, int burnTime, ResourceLocation id) {
        this.items = items;
        this.mana = mana;
        this.burnTime = burnTime;
        this.id = id;
    }

    public int getMana() {
        return mana;
    }

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return items.contains(pContainer.getItem(0).getItem());
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
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<StonesiaRecipe> {
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<StonesiaRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public StonesiaRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            int mana = GsonHelper.getAsInt(jsonObject, "mana");
            int burnTime = GsonHelper.getAsInt(jsonObject, "burnTime");

            ArrayList<Item> items = new ArrayList<>();

            String itemId = GsonHelper.getAsString(jsonObject, "item", "");
            String itemTag = GsonHelper.getAsString(jsonObject, "tag", "");
            if (!itemId.isEmpty())
                items.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)));
                //TODO FIX LOL, return an empty list every time
            else if (!itemTag.isEmpty()) {
                TagKey<Item> blockTagKey = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(itemTag));

                @NotNull ITag<Item> optional = ForgeRegistries.ITEMS.tags().getTag(blockTagKey);
                items.addAll(optional.stream().toList());
            }

            return new StonesiaRecipe(items, mana, burnTime, resourceLocation);
        }

        @Override
        public @Nullable StonesiaRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf pBuffer) {
            ArrayList<Item> items = new ArrayList<>();
            int size = pBuffer.readInt();

            for (int i = 0; i < size; i++) {
                items.add(pBuffer.readItem().getItem());
            }

            int mana = pBuffer.readInt();
            int burnTime = pBuffer.readInt();

            return new StonesiaRecipe(items, mana, burnTime, resourceLocation);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, StonesiaRecipe recipe) {
            pBuffer.writeInt(recipe.items.size());

            for (Item item : recipe.items) {
                pBuffer.writeItem(item.getDefaultInstance());
            }

            pBuffer.writeInt(recipe.mana);
            pBuffer.writeInt(recipe.burnTime);
        }
    }

}

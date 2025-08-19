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
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GeminiOrchidSourceRecipe implements Recipe<Container> {
    private final List<Item> blocks;
    private final int temperature;
    private final ResourceLocation id;

    public GeminiOrchidSourceRecipe(List<Item> blocks, int temperature, ResourceLocation id) {
        this.blocks = blocks;
        this.temperature = temperature;
        this.id = id;
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return blocks.contains(pContainer.getItem(0).getItem());
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

    public static class Type implements RecipeType<GeminiOrchidSourceRecipe> {
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<GeminiOrchidSourceRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public GeminiOrchidSourceRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            int temperature = GsonHelper.getAsInt(pSerializedRecipe, "temperature");

            ArrayList<Item> blocks = new ArrayList<>();

            String blockId = GsonHelper.getAsString(pSerializedRecipe, "block", "");
            String blockTag = GsonHelper.getAsString(pSerializedRecipe, "blockTag", "");
            if (!blockId.isEmpty())
                blocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId)).asItem());
            else if (!blockTag.isEmpty()) {
                TagKey<Block> blockTagKey = TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), new ResourceLocation(blockTag));
                ITag<Block> optional = ForgeRegistries.BLOCKS.tags().getTag(blockTagKey);
                blocks.addAll(optional.stream().map(Block::asItem).toList());
            }

            return new GeminiOrchidSourceRecipe(blocks, temperature, pRecipeId);
        }

        @Override
        public @Nullable GeminiOrchidSourceRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ArrayList<Item> blocks = new ArrayList<>();
            int size = pBuffer.readInt();

            for (int i = 0; i < size; i++) {
                blocks.add(pBuffer.readItem().getItem());
            }

            int temperature = pBuffer.readInt();

            return new GeminiOrchidSourceRecipe(blocks, temperature, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, GeminiOrchidSourceRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.blocks.size());

            for (Item item : pRecipe.blocks) {
                pBuffer.writeItem(item.getDefaultInstance());
            }

            pBuffer.writeInt(pRecipe.temperature);
        }
    }
}
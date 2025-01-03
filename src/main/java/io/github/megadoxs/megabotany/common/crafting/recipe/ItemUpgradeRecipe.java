package io.github.megadoxs.megabotany.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class ItemUpgradeRecipe extends ShapedRecipe {
    public ItemUpgradeRecipe(ShapedRecipe compose) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getWidth(), compose.getHeight(),
                compose.getIngredients(),
                // XXX: Hacky, but compose should always be a vanilla shaped recipe which doesn't do anything with the
                // RegistryAccess
                compose.getResultItem(RegistryAccess.EMPTY));
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registries) {
        ItemStack out = super.assemble(inv, registries);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.hasTag()) {
                out.setTag(stack.getTag());
                break;
            }
        }
        return out;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static final RecipeSerializer<ItemUpgradeRecipe> SERIALIZER = new ItemUpgradeRecipe.Serializer();

    private static class Serializer implements RecipeSerializer<ItemUpgradeRecipe> {
        @Override
        public ItemUpgradeRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            return new ItemUpgradeRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
        }

        @Override
        public ItemUpgradeRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            return new ItemUpgradeRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ItemUpgradeRecipe recipe) {
            SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

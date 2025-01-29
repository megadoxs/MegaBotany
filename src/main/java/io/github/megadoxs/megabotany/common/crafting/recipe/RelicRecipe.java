package io.github.megadoxs.megabotany.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

public class RelicRecipe extends ShapelessRecipe {
    public RelicRecipe(ShapelessRecipe compose) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getResultItem(RegistryAccess.EMPTY), compose.getIngredients());
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registries) {
        ItemStack out = super.assemble(inv, registries);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic instanceof RelicImpl) {
                //tag is copy from RelicImpl TAG_SOULBIND_UUID
                //Should always have a value when used in crafting
                if(stack.getTag() != null && stack.getTag().contains("soulbindUUID")){
                    CompoundTag tag = new CompoundTag();
                    tag.put("soulbindUUID", stack.getTag().get("soulbindUUID"));
                    out.setTag(tag);
                }
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

    public static final RecipeSerializer<RelicRecipe> SERIALIZER = new RelicRecipe.Serializer();

    private static class Serializer implements RecipeSerializer<RelicRecipe> {
        @Override
        public RelicRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            return new RelicRecipe(SHAPELESS_RECIPE.fromJson(recipeId, json));
        }

        @Override
        public RelicRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            return new RelicRecipe(SHAPELESS_RECIPE.fromNetwork(recipeId, buffer));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RelicRecipe recipe) {
            SHAPELESS_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

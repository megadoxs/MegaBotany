package io.github.megadoxs.megabotany.client.integration.jei;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.SpiritTradeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIMegaBotanyPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(MegaBotany.MOD_ID, "main");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new DaySpiritTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new NightSpiritTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        registry.addRecipes(DaySpiritTradeRecipeCategory.TYPE, sortRecipes(SpiritTradeRecipe.Type.INSTANCE));
        registry.addRecipes(NightSpiritTradeRecipeCategory.TYPE, sortRecipes(SpiritTradeRecipe.Type.INSTANCE));
    }

    private static <T extends Recipe<C>, C extends Container> List<T> sortRecipes(RecipeType<T> type) { //sorts everything by id, for other sorting ways look at JEIBotaniaPlugin
        Collection<T> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(type);
        List<T> list = new ArrayList<>(recipes);
        list.sort(Comparator.comparing(Recipe::getId));
        return list;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(MegaBotanyBlocks.SPIRIT_PORTAL.get()), DaySpiritTradeRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(MegaBotanyBlocks.SPIRIT_PORTAL.get()), NightSpiritTradeRecipeCategory.TYPE);
    }
}

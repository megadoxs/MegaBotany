package io.github.megadoxs.megabotany.client.integration.jei;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.SpiritTradeRecipe;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NightSpiritTradeRecipeCategory extends DaySpiritTradeRecipeCategory {

    public static final RecipeType<SpiritTradeRecipe> TYPE = RecipeType.create(MegaBotany.MOD_ID, "night_spirit_trade", SpiritTradeRecipe.class);

    public NightSpiritTradeRecipeCategory(IGuiHelper guiHelper) {
        icon = new DrawableStack(guiHelper.createDrawableItemStack(new ItemStack(MegaBotanyBlocks.SPIRIT_PORTAL.get())), new ScaledDrawableResource(new ResourceLocation(MegaBotany.MOD_ID, "textures/gui/moon.png"), 0, 0, 8, 8, 8, 0, 8, 0, 323, 317));
        background = new ColorDrawable(145, 95, 32, 0, 80, 255);
        overlay = guiHelper.createDrawable(new ResourceLocation(MegaBotany.MOD_ID, "textures/gui/spirit_trade_overlay.png"), 0, 15, 140, 90);
        tooltipIcon = new ScaledDrawableResource(new ResourceLocation(MegaBotany.MOD_ID, "textures/gui/moon.png"), 0, 0, 16, 16, 0, 0, 0, 0, 470, 470);
        sprite = new ResourceLocation(MegaBotany.MOD_ID, "block/spirit_portal_swirl_night");
    }

    @Override
    public List<Component> getTooltipStrings(SpiritTradeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if(mouseX >= 93 && mouseX <= 109 && mouseY >= 10 && mouseY <= 26){
            return List.of(Component.literal("This recipe must be done at night when the portal is purple.")); //TODO make into a translatable
        }
        else
            return List.of();
    }

    @NotNull
    @Override
    public RecipeType<SpiritTradeRecipe> getRecipeType() {
        return TYPE;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return Component.translatable("megabotany.jei.spirit_trade_night");
    }

    @Override
    public List<ItemStack> getOutputs(SpiritTradeRecipe recipe) {
        return recipe.getOutputs(false);
    }
}

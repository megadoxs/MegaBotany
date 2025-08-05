package io.github.megadoxs.megabotany.client.integration.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.SpiritTradeRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class DaySpiritTradeRecipeCategory implements IRecipeCategory<SpiritTradeRecipe> {

    public static final RecipeType<SpiritTradeRecipe> TYPE = RecipeType.create(MegaBotany.MOD_ID, "day_spirit_trade", SpiritTradeRecipe.class);

    protected IDrawable icon;
    protected IDrawable background;
    protected IDrawable overlay;
    protected IDrawable tooltipIcon;
    protected ResourceLocation sprite;

    public DaySpiritTradeRecipeCategory(IGuiHelper guiHelper) {
        icon = new DrawableStack(guiHelper.createDrawableItemStack(new ItemStack(MegaBotanyBlocks.SPIRIT_PORTAL.get())), new ScaledDrawableResource(new ResourceLocation(MegaBotany.MOD_ID, "textures/gui/sun.png"), 0, 0, 8, 8, 8, 0, 8, 0, 470, 470));
        background = new ColorDrawable(145, 95, 255, 255, 0, 255);
        overlay = guiHelper.createDrawable(new ResourceLocation(MegaBotany.MOD_ID, "textures/gui/spirit_trade_overlay.png"), 0, 15, 140, 90);
        tooltipIcon = new ScaledDrawableResource(new ResourceLocation(MegaBotany.MOD_ID, "textures/gui/sun.png"), 0, 0, 16, 16, 0, 0, 0, 0, 470, 470);
        sprite = new ResourceLocation(MegaBotany.MOD_ID, "block/spirit_portal_swirl");
    }

    public DaySpiritTradeRecipeCategory(){}

    @NotNull
    @Override
    public RecipeType<SpiritTradeRecipe> getRecipeType() {
        return TYPE;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return Component.translatable("megabotany.jei.spirit_trade_day");
    }

    @NotNull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @NotNull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override // TODO add moon / sun icon in every crafting recipe and hover text to tell when the item should be thrown
    public void draw(@NotNull SpiritTradeRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
        PoseStack matrices = gui.pose();
        RenderSystem.enableBlend();
        overlay.draw(gui, 0, 4);
        tooltipIcon.draw(gui, 93, 10);
        RenderSystem.disableBlend();

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(this.sprite);
        MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer v = immediate.getBuffer(RenderType.solid());
        int startX = 22;
        int startY = 25;
        int stopX = 70;
        int stopY = 73;
        Matrix4f mat = matrices.last().pose();
        Matrix3f n = matrices.last().normal();
        v.vertex(mat, startX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU0(), sprite.getV0()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        v.vertex(mat, startX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU0(), sprite.getV1()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        v.vertex(mat, stopX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU1(), sprite.getV1()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        v.vertex(mat, stopX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU1(), sprite.getV0()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        immediate.endBatch();
    }

    @Override
    public List<Component> getTooltipStrings(SpiritTradeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if(mouseX >= 93 && mouseX <= 109 && mouseY >= 10 && mouseY <= 26){
            return List.of(Component.literal("This recipe must be done during the day when the portal is pink.")); //TODO make into a translatable
        }
        else
            return List.of();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SpiritTradeRecipe recipe, @NotNull IFocusGroup focusGroup) {
        int posX = 42;
        for (var ingr : recipe.getIngredients()) {
            builder.addSlot(RecipeIngredientRole.INPUT, posX, 0)
                    .addIngredients(ingr);
            posX += 18;
        }

        int outIdx = 0;
        for (var stack : getOutputs(recipe)) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 93 + outIdx % 2 * 20, 41 + outIdx / 2 * 20)
                    .addItemStack(stack);
            outIdx++;
        }
    }

    public List<ItemStack> getOutputs(SpiritTradeRecipe recipe) {
        return recipe.getOutputs(true);
    }
}
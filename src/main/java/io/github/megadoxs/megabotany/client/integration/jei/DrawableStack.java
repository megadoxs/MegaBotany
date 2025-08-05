package io.github.megadoxs.megabotany.client.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.Stack;

public class DrawableStack implements IDrawable {

    private final Stack<IDrawable> drawables = new Stack<>();

    public DrawableStack(IDrawable... drawables) {
        this.drawables.addAll(List.of(drawables));
    }

    @Override
    public int getWidth() {
        int width = 0;
        for (IDrawable drawable : drawables) {
            width = Math.max(width, drawable.getWidth());
        }
        return width;
    }

    @Override
    public int getHeight() {
        int height = 0;
        for (IDrawable drawable : drawables) {
            height = Math.max(height, drawable.getHeight());
        }
        return height;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        for (IDrawable drawable : drawables) {
            drawable.draw(guiGraphics, xOffset, yOffset);
        }
    }
}

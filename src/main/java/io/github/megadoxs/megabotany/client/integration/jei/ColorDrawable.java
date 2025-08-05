package io.github.megadoxs.megabotany.client.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class ColorDrawable implements IDrawable {
    private final int width;
    private final int height;

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public ColorDrawable(int width, int height, int red, int green, int blue, int alpha) {
        this.width = width;
        this.height = height;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        guiGraphics.fill(xOffset, yOffset, xOffset + width, yOffset + height, (alpha << 24) | (red << 16) | (green << 8) | blue);
    }
}

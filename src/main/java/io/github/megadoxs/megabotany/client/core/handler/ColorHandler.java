package io.github.megadoxs.megabotany.client.core.handler;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewItem;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.ColorHandler.ItemHandlerConsumer;

public class ColorHandler {
    public static void submitItems(ItemHandlerConsumer items) {
        items.register((s, t) -> {
            if (t != 1) {
                return -1;
            }

            Brew brew = ((BrewItem) s.getItem()).getBrew(s);

            int color = brew.getColor(s);
            int add = (int) (Math.sin(ClientTickHandler.ticksInGame * 0.1) * 24);

            int r = Math.max(0, Math.min(255, (color >> 16 & 0xFF) + add));
            int g = Math.max(0, Math.min(255, (color >> 8 & 0xFF) + add));
            int b = Math.max(0, Math.min(255, (color & 0xFF) + add));

            return r << 16 | g << 8 | b;
        }, MegaBotanyItems.INFINITE_BREW.get(), MegaBotanyItems.INFINITE_SPLASH_BREW.get(), MegaBotanyItems.INFINITE_LINGERING_BREW.get());
    }
}

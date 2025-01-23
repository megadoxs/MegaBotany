package io.github.megadoxs.megabotany.common.event;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MegaBotanyClientEvent {
    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().getItem() == MegaBotanyItems.FAILNAUGHT.get()) {
            float fovModifier = 1f;
            int tickUsingItem = event.getPlayer().getTicksUsingItem();
            float delatTicks = (float) tickUsingItem / 20;
            if (delatTicks > 1f)
                delatTicks = 1f;
            else
                delatTicks *= delatTicks;
            fovModifier *= 1f - delatTicks * 0.15f;
            event.setNewFovModifier(fovModifier);
        }
    }
}

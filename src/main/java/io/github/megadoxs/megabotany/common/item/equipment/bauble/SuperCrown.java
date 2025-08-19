package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class SuperCrown extends BaubleItem { //TODO in version past 1.20.5, it will also make the player generic.scale be 10% bigger
    public SuperCrown(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent evt) {
        if (evt.getEntity() instanceof Player player) {
            if (CuriosApi.getCuriosInventory(player).resolve().get().isEquipped(MegaBotanyItems.SUPER_CROWN.get()))
                evt.setAmount(Math.max(0, evt.getAmount() * 0.9f));
        }
    }
}

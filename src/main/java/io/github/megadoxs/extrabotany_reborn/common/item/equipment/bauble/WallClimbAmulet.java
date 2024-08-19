package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;

public class WallClimbAmulet extends BaubleItem {
    public WallClimbAmulet(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        Proxy.INSTANCE.runOnClient(() -> () -> {
            if (entity instanceof LocalPlayer player && entity == Minecraft.getInstance().player) {
                if (entity.horizontalCollision) {
                    if (player.input.jumping)
                        player.setDeltaMovement(player.getDeltaMovement().x, Math.max(player.getDeltaMovement().y, 0.11F), player.getDeltaMovement().z);
                    else if (player.input.shiftKeyDown)
                        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                }
            }
        });
    }
}

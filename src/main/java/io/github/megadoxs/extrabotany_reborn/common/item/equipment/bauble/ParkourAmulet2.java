package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.network.serverbound.JumpPacket;
import vazkii.botania.xplat.ClientXplatAbstractions;

public class ParkourAmulet2 extends BaubleItem {

    private static int timesJumped;
    private static boolean jumpDown;

    public ParkourAmulet2(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        Proxy.INSTANCE.runOnClient(() -> () -> {
            if (entity == Minecraft.getInstance().player) {
                LocalPlayer playerSp = (LocalPlayer) entity;
                if (playerSp.onGround())
                    timesJumped = 0;
                if (entity.horizontalCollision) {
                    if (playerSp.input.jumping)
                        playerSp.setDeltaMovement(playerSp.getDeltaMovement().x, Math.max(playerSp.getDeltaMovement().y, 0.11F), playerSp.getDeltaMovement().z);
                    else if (playerSp.input.shiftKeyDown)
                        playerSp.setDeltaMovement(playerSp.getDeltaMovement().x, 0, playerSp.getDeltaMovement().z);
                } else if (timesJumped < 3) {
                    if (timesJumped == 0) {
                        timesJumped = 1;
                        jumpDown = true;
                    }
                    if (playerSp.input.jumping) {
                        if (!jumpDown) {
                            playerSp.jumpFromGround();
                            ClientXplatAbstractions.INSTANCE.sendToServer(JumpPacket.INSTANCE);
                            timesJumped++;
                        }
                        jumpDown = true;
                    } else {
                        jumpDown = false;
                    }
                }
            }
        });
    }
}
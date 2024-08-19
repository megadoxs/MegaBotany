package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.network.serverbound.JumpPacket;
import vazkii.botania.xplat.ClientXplatAbstractions;

public class ParkourAmulet extends BaubleItem {

    private static int timesJumped;
    private static boolean jumpDown;
    private static boolean wallCollide;

    public ParkourAmulet(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        Proxy.INSTANCE.runOnClient(() -> () -> {
            if (entity == Minecraft.getInstance().player) {
                LocalPlayer playerSp = (LocalPlayer) entity;
                if(wallCollide)
                    wallCollide = playerSp.horizontalCollision;
                if (playerSp.onGround()) {
                    timesJumped = 0;
                    wallCollide = false;
                }
                else if(timesJumped < 3){
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
                if (playerSp.input.jumping && entity.horizontalCollision && !wallCollide) {
                    playerSp.jumpFromGround();
                    ClientXplatAbstractions.INSTANCE.sendToServer(JumpPacket.INSTANCE);
                    timesJumped++;
                    if(timesJumped > 1)
                        wallCollide = true;
                }
            }
        });
    }
}

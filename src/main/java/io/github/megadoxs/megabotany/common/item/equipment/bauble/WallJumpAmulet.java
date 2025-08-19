package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.network.serverbound.JumpPacket;
import vazkii.botania.xplat.ClientXplatAbstractions;

public class WallJumpAmulet extends BaubleItem { //TODO rework or remove, player can spam left to jump infinitely

    private static int timesJumped;
    private static boolean wallCollide;

    public WallJumpAmulet(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        Proxy.INSTANCE.runOnClient(() -> () -> {
            if (entity == Minecraft.getInstance().player) {
                LocalPlayer playerSp = (LocalPlayer) entity;
                if (wallCollide)
                    wallCollide = playerSp.horizontalCollision;
                if (playerSp.onGround()) {
                    timesJumped = 0;
                    wallCollide = false;
                }
                if (playerSp.input.jumping && playerSp.horizontalCollision && !playerSp.minorHorizontalCollision && !wallCollide) {
                    playerSp.jumpFromGround();
                    timesJumped++;
                    if (timesJumped > 1)
                        wallCollide = true;
                }
            }
        });
    }
}

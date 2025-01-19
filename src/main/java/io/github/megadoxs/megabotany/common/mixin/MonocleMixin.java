package io.github.megadoxs.megabotany.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;

import java.text.NumberFormat;

@Mixin(ManaseerMonocleItem.Hud.class)
public abstract class MonocleMixin {
    @Inject(method = "render", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void manaRenderer(GuiGraphics gui, Player player, CallbackInfo ci, Minecraft mc, HitResult ray, BlockPos pos, BlockState state) {
        BlockEntity be = player.level().getBlockEntity(pos);
        if (be != null && be.getUpdateTag().contains("mana"))
            gui.drawString(mc.font, "Mana: " + NumberFormat.getInstance().format(be.getUpdateTag().getInt("mana")), 5, 5, 0xFFFFFF);
    }
}

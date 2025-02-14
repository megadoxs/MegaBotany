package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.api.RedstoneSpreader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;

@Mixin(ManaSpreaderBlockEntity.WandHud.class)
public class ManaSpeaderBlockEntityWandHudMixin {

    @Final
    @Shadow(remap = false)
    private ManaSpreaderBlockEntity spreader;

    @ModifyVariable(method = "renderHUD", name = "color", at = @At(value = "STORE", ordinal = 0), remap = false)
    private int redstoneBurst(int color) {
        if (spreader.getBlockState().getBlock() instanceof RedstoneSpreader redstoneSpreader && redstoneSpreader.isRedstone())
            color = 0xFF0000;
        return color;
    }
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.PylonBlock;

@Mixin(PylonBlock.Variant.class)
public abstract class PylonBlockVariantMixin {
    @Inject(method = "getTargetBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void getTargetBlock(CallbackInfoReturnable<Block> cir) {
        PylonBlock.Variant variant = (PylonBlock.Variant) (Object) this;

        if (variant == PylonBlock.Variant.GAIA)
            cir.setReturnValue(MegaBotanyBlocks.SPIRIT_PORTAL.get());
    }
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.api.RedstoneSpreader;
import io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.client.render.block_entity.ManaSpreaderBlockEntityRenderer;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;

@Mixin(ManaSpreaderBlockEntityRenderer.class)
public class ManaSpreaderBlockEntityRendererMixin {
    @Inject(method = "getCoreModel", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCoreModel(ManaSpreaderBlockEntity tile, CallbackInfoReturnable<BakedModel> cir) {
        Block block = tile.getBlockState().getBlock();

        if (block instanceof RedstoneSpreader spreader && spreader.isRedstone()) {
            if (block == MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get())
                cir.setReturnValue(MiscellaneousModels.INSTANCE.redstoneElvenSpreaderCore);
            else if (block == MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get())
                cir.setReturnValue(MiscellaneousModels.INSTANCE.redstoneGaiaSpreaderCore);
        }
    }
}

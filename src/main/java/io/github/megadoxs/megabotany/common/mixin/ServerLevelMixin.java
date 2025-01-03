package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.MegaBotanyPOITypes;
import io.github.megadoxs.megabotany.common.block.flower.generating.ReikarLilyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(method = "findLightningRod", at = @At("HEAD"), cancellable = true)
    private void FindLightningRod(BlockPos pPos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        ServerLevel self = (ServerLevel) (Object) this;

        Optional<BlockPos> optional = self.getPoiManager().findClosest(
                (holder) -> holder.get() == MegaBotanyPOITypes.REIKAR_LILY.get(),
                (blockPos) -> blockPos.getY() == self.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) - 1 && !((ReikarLilyBlockEntity) self.getBlockEntity(blockPos)).isInCooldown(),
                pPos,
                128,
                PoiManager.Occupancy.ANY
        );

        if (optional.isPresent()) {
            cir.setReturnValue(optional.map(pos -> pos.above(1)));
            cir.cancel();
        }
    }
}

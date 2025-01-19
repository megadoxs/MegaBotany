package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.block.flower.generating.ReikarLilyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {

    @Shadow
    protected abstract BlockPos getStrikePosition();

    @Inject(method = "powerLightningRod", at = @At("HEAD"))
    private void powerLightningRod(CallbackInfo ci) {
        LightningBolt self = (LightningBolt) (Object) this;

        BlockPos blockpos = getStrikePosition();
        BlockEntity blockEntity = self.level().getBlockEntity(blockpos);
        if (blockEntity != null && blockEntity.getType() == MegaBotanyFlowerBlocks.REIKAR_LILY) {
            ((ReikarLilyBlockEntity) blockEntity).onLightningStrike();
        }
    }
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.PylonBlock;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Random;

@Mixin(PylonBlockEntity.class)
public abstract class PylonBlockEntityMixin {

    @Shadow(remap = false)
    BlockPos centerPos;

    @Inject(method = "commonTick", at = @At(value = "FIELD", target = "Lvazkii/botania/common/block/block_entity/PylonBlockEntity;ticks:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER, ordinal = 0), remap = false)
    private static void commonTick(Level level, BlockPos worldPosition, BlockState state, PylonBlockEntity self, CallbackInfo ci) {
        PylonBlock.Variant variant = ((PylonBlock) state.getBlock()).variant;
        if (variant == PylonBlock.Variant.GAIA && self instanceof PylonBlockEntityAccessor pylon && pylon.isActivated() && level.isClientSide && level.getBlockState(pylon.getCenterPos()).is(variant.getTargetBlock())) {
            if (pylon.callPortalOff() || !(level.getBlockState(worldPosition.below()).getBlock() instanceof ManaPoolBlock)) {
                pylon.setActivated(false);
                return;
            }

            Vec3 centerBlock = new Vec3(pylon.getCenterPos().getX() + 0.5, pylon.getCenterPos().getY() + 0.75 + (Math.random() - 0.5 * 0.25), pylon.getCenterPos().getZ() + 0.5);

            if (BotaniaConfig.client().elfPortalParticlesEnabled()) {
                double worldTime = pylon.getTicks();
                worldTime += new Random(worldPosition.hashCode()).nextInt(1000);
                worldTime /= 5;

                float r = 0.75F + (float) Math.random() * 0.05F;
                double x = worldPosition.getX() + 0.5 + Math.cos(worldTime) * r;
                double z = worldPosition.getZ() + 0.5 + Math.sin(worldTime) * r;

                Vec3 ourCoords = new Vec3(x, worldPosition.getY() + 0.25, z);
                centerBlock = centerBlock.subtract(0, 0.5, 0);
                Vec3 movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

                WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.5F + (float) Math.random() * 0.5F, 1);
                level.addParticle(data, x, worldPosition.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
                if (level.random.nextInt(3) == 0) {
                    WispParticleData data1 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.5F + (float) Math.random() * 0.5F);
                    level.addParticle(data1, x, worldPosition.getY() + 0.25, z, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
                }
            }
        }
    }

    @Inject(method = "portalOff", at = @At("HEAD"), remap = false, cancellable = true)
    private void portalOff(CallbackInfoReturnable<Boolean> cir) {
        Level level = ((PylonBlockEntity) (Object) this).getLevel();

        if (level.getBlockState(centerPos).is(MegaBotanyBlocks.SPIRIT_PORTAL.get()))
            cir.setReturnValue(level.getBlockState(centerPos).getValue(BotaniaStateProperties.ALFPORTAL_STATE) == AlfheimPortalState.OFF);
    }
}

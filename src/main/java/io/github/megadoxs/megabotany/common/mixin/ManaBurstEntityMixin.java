package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.block.flower.functional.ManalinkiumBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.xplat.XplatAbstractions;

@Mixin(ManaBurstEntity.class)
public abstract class ManaBurstEntityMixin {
    @Shadow
    protected abstract void onHit(HitResult hitResult);

    @Shadow(remap = false)
    protected abstract boolean onReceiverImpact(ManaReceiver receiver);

    @Shadow(remap = false)
    private BlockPos lastCollision;

    @Shadow(remap = false)
    private ManaReceiver collidedTile;

    @Shadow(remap = false)
    private boolean noParticles;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ManaBurstEntity self = (ManaBurstEntity) (Object) this;

        Vec3 currentPosition = self.position();
        BlockPos blockPos = self.blockPosition();

        BlockState blockState = self.level().getBlockState(blockPos);
        VoxelShape voxelShape = blockState.getShape(self.level(), blockPos);


        if (!voxelShape.isEmpty() && voxelShape.bounds().move(blockPos).contains(currentPosition) && self.level().getBlockEntity(blockPos) instanceof ManalinkiumBlockEntity manalinkium && manalinkium.isValid()) {
            HitResult hitResult = new BlockHitResult(currentPosition, self.getDirection(), blockPos, false);
            onHit(hitResult);
        }
    }

    //TODO this can probably be simplified
    @Inject(method = "onHitBlock", at = @At("HEAD"), cancellable = true)
    private void onHitBlock(BlockHitResult hit, CallbackInfo ci) {
        ManaBurstEntity self = (ManaBurstEntity) (Object) this;

        BlockPos collidePos = hit.getBlockPos();
        if (collidePos.equals(lastCollision)) {
            return;
        }

        BlockEntity tile = self.level().getBlockEntity(collidePos);
        BlockState state = self.level().getBlockState(collidePos);

        var ghost = XplatAbstractions.INSTANCE.findManaGhost(self.level(), collidePos, state, tile);
        var ghostBehaviour = ghost != null ? ghost.getGhostBehaviour() : ManaCollisionGhost.Behaviour.RUN_ALL;

        if (ghostBehaviour == ManaCollisionGhost.Behaviour.SKIP_ALL || !state.is(MegaBotanyFlowerBlocks.manalinkium)) {
            return;
        }

        BlockPos sourcePos = self.getBurstSourceBlockPos();
        if (!self.hasLeftSource() && collidePos.equals(sourcePos)) {
            return;
        }

        var receiver = XplatAbstractions.INSTANCE.findManaReceiver(self.level(), collidePos, state, tile, hit.getDirection());
        collidedTile = receiver;

        self.setCollidedAt(collidePos);
        ci.cancel();

        if (!self.isFake() && !noParticles && !self.level().isClientSide) {
            if (receiver != null && receiver.canReceiveManaFromBursts() && onReceiverImpact(receiver)) {
                if (tile instanceof ThrottledPacket throttledPacket) {
                    throttledPacket.markDispatchable();
                } else if (tile != null) {
                    VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
                }
            }
        }

        self.discard();
    }
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.api.RedstoneSpreader;
import io.github.megadoxs.megabotany.common.block.flower.functional.ManalinkiumBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.KeyLocked;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ManaSpreaderBlockEntity.class)
public abstract class ManaSpreaderBlockEntityMixin {
    @Unique
    private final Map<BlockPos, Boolean> manalinkiums = new HashMap<>();

    @Shadow(remap = false)
    private List<ManaBurstEntity.PositionProperties> lastTentativeBurst;

    @Shadow(remap = false)
    private boolean invalidTentativeBurst;

    @Inject(method = "needsNewBurstSimulation", at = @At(value = "RETURN", ordinal = 3), cancellable = true, remap = false)
    private void needsNewBurstSimulation(CallbackInfoReturnable<Boolean> cir) {
        ManaSpreaderBlockEntity self = (ManaSpreaderBlockEntity) (Object) this;

        for (ManaBurstEntity.PositionProperties props : lastTentativeBurst) {
            if (self.getLevel().getBlockEntity(props.coords()) instanceof ManalinkiumBlockEntity manalinkium) {
                Boolean state = manalinkiums.get(props.coords());
                boolean newState = manalinkium.isValid();
                manalinkiums.put(props.coords(), newState);
                if (state != null && state != newState) {
                    invalidTentativeBurst = false;
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

    @ModifyVariable(method = "tryShootBurst", at = @At(value = "STORE", ordinal = 0), remap = false)
    private boolean tryShootBurst(boolean redstone) {
        ManaSpreaderBlockEntity self = (ManaSpreaderBlockEntity) (Object) this;
        return changeIsRedstone(self, redstone);
    }

    @Unique
    private static boolean changeIsRedstone(ManaSpreaderBlockEntity self, boolean redstone) {
        if (self.getBlockState().getBlock() instanceof RedstoneSpreader redstoneSpreader && redstoneSpreader.isRedstone())
            return !redstone;
        return redstone;
    }

    @Inject(method = "commonTick", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/block/block_entity/mana/ManaSpreaderBlockEntity;needsNewBurstSimulation()Z"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void commonTick(Level level, BlockPos worldPosition, BlockState state, ManaSpreaderBlockEntity self, CallbackInfo ci, boolean inNetwork, boolean wasInNetwork, boolean powered) {
        for (Direction dir : Direction.values()) {
            var relPos = worldPosition.relative(dir);
            if (level.hasChunkAt(relPos)) {
                var receiverAt = XplatAbstractions.INSTANCE.findManaReceiver(level, relPos, dir.getOpposite());
                if (receiverAt instanceof ManaPool pool) {
                    if (wasInNetwork && self.getBlockState().getBlock() instanceof RedstoneSpreader redstoneSpreader && redstoneSpreader.isRedstone()) {
                        if (pool instanceof KeyLocked locked && !locked.getOutputKey().equals(self.getInputKey())) {
                            continue;
                        }

                        int manaInPool = pool.getCurrentMana();
                        if (manaInPool > 0 && !self.isFull()) {
                            int manaMissing = self.getMaxMana() - self.getCurrentMana();
                            int manaToRemove = Math.min(manaInPool, manaMissing);
                            pool.receiveMana(-manaToRemove);
                            self.receiveMana(manaToRemove);
                        }
                    }
                }
            }
        }
    }

    //slice = @Slice(from = @At(value = "INVOKE", target = "Lvazkii/botania/common/block/block_entity/mana/ManaSpreaderBlockEntity;getVariant()Lvazkii/botania/common/block/mana/ManaSpreaderBlock$Variant;", ordinal = 1))
    @ModifyVariable(method = "commonTick", name = "redstoneSpreader", at = @At(value = "STORE", ordinal = 0), remap = false)
    private static boolean redstoneSpreader(boolean redstone, Level level, BlockPos worldPosition, BlockState state, ManaSpreaderBlockEntity self) {
        return changeIsRedstone(self, redstone);
    }

    @ModifyVariable(method = "getBurst", name = "props", at = @At(value = "STORE", ordinal = 0), remap = false)
    private BurstProperties redstoneBurst(BurstProperties value) {
        ManaSpreaderBlockEntity self = (ManaSpreaderBlockEntity) (Object) this;
        if (self.getBlockState().getBlock() instanceof RedstoneSpreader redstoneSpreader && redstoneSpreader.isRedstone())
            value.color = 0xFF0000;
        return value;
    }
}

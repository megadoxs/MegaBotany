package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.flower.functional.ManalinkiumBlockEntity;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ManaSpreaderBlockEntity.class)
public abstract class ManaSpreaderBlockEntityMixin {
    @Unique
    private Map<BlockPos, Boolean> manalinkiums = new HashMap<>();

    @Shadow(remap = false)
    private List<ManaBurstEntity.PositionProperties> lastTentativeBurst;

    @Shadow(remap = false)
    private boolean invalidTentativeBurst;

    @Inject(method = "needsNewBurstSimulation", at = @At(value = "RETURN", ordinal = 3), cancellable = true, remap = false)
    private void needsNewBurstSimulation2(CallbackInfoReturnable<Boolean> cir) {
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
}

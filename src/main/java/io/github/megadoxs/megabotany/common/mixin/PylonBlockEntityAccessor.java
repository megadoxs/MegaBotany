package io.github.megadoxs.megabotany.common.mixin;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

@Mixin(PylonBlockEntity.class)
public interface PylonBlockEntityAccessor {
    @Accessor(remap = false)
    void setCenterPos(BlockPos pos);

    @Accessor(remap = false)
    BlockPos getCenterPos();

    @Accessor(remap = false)
    void setActivated(boolean activated);

    @Accessor(remap = false)
    boolean isActivated();

    @Accessor(remap = false)
    int getTicks();

    @Invoker(value = "portalOff", remap = false)
    boolean callPortalOff();
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.api.RedstoneSpreader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;

@Mixin(ManaSpreaderBlock.class)
public abstract class ManaSpreaderBlockMixin implements RedstoneSpreader {
    @Unique
    public boolean redstone = false;

    @Unique
    public void setRedstone(boolean redstone) {
        this.redstone = redstone;
    }

    @Unique
    public boolean isRedstone() {
        return redstone;
    }
}

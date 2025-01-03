package io.github.megadoxs.megabotany.api.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface DimensionalFlowerWandBindable {
    boolean bindTo(Player player, ItemStack wand, BlockPos pos, String dimensionId, Direction side);
}

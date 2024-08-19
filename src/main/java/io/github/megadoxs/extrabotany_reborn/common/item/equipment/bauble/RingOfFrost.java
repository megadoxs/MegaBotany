package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

import java.util.Iterator;

public class RingOfFrost extends BaubleItem {

    public static final int RADIUS = 6;
    public RingOfFrost(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity.onGround() && entity instanceof Player player) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            Iterator var7 = BlockPos.betweenClosed(entity.blockPosition().offset(-RADIUS, -1, -RADIUS), entity.blockPosition().offset(RADIUS, -1, RADIUS)).iterator();

            while(var7.hasNext()) {
                BlockPos blockpos = (BlockPos)var7.next();
                if (blockpos.closerToCenterThan(entity.position(), RADIUS)) {
                    blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = entity.level().getBlockState(blockpos$mutableblockpos);
                    if (blockstate1.isAir()) {
                        BlockState blockstate2 = entity.level().getBlockState(blockpos);
                        if (blockstate2 == FrostedIceBlock.meltsInto() && entity.level().isUnobstructed(Blocks.ICE.defaultBlockState(), blockpos, CollisionContext.empty()) && !ForgeEventFactory.onBlockPlace(entity, BlockSnapshot.create(entity.level().dimension(), entity.level(), blockpos), Direction.UP) && ManaItemHandler.instance().requestManaExact(stack, player, 5, false)) {
                            entity.level().setBlockAndUpdate(blockpos, Blocks.ICE.defaultBlockState());
                            ManaItemHandler.instance().requestManaExact(stack, player, 5, true);
                        }
                        else if(blockstate2 == Blocks.LAVA.defaultBlockState() && entity.level().isUnobstructed(Blocks.OBSIDIAN.defaultBlockState(), blockpos, CollisionContext.empty()) && !ForgeEventFactory.onBlockPlace(entity, BlockSnapshot.create(entity.level().dimension(), entity.level(), blockpos), Direction.UP) && ManaItemHandler.instance().requestManaExact(stack, player, 10, false)){
                            entity.level().setBlockAndUpdate(blockpos, Blocks.OBSIDIAN.defaultBlockState());
                            ManaItemHandler.instance().requestManaExact(stack, player, 10, true);
                        }
                    }
                }
            }
        }
    }
}

package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

import java.util.List;

public class BottledFlame extends BaubleItem { //TODO display either in chat or in action bar the new mode when switching
    public static String TAG_MODE = "mode";
    public static String TAG_SIDE = "side";

    public BottledFlame(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            Level world = entity.level();
            BlockPos pos = entity.blockPosition();

            if (world.getMaxLocalRawBrightness(pos) < 4) {
                ItemStack stackAt = ItemStack.EMPTY;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    if (player.getInventory().getItem(i).getDescriptionId().contains("torch")) {
                        stackAt = player.getInventory().getItem(i);
                        break;
                    }
                }

                // right clockwise, left counterClockWise
                // 0 = right-left, 1 = right, 2 = left, 3 bottom, 4 = any
                // note only 3 and 4 will work if the player is less then a block or on the belly
                int mode = ItemNBTHelper.getInt(stack, TAG_MODE, 3);
                String side = ItemNBTHelper.getString(stack, TAG_SIDE, "Right");
                Direction direction;
                switch (mode) {
                    case 0:
                        direction = "Right".equals(side) ? player.getDirection().getClockWise() : "Left".equals(side) ? player.getDirection().getCounterClockWise() : null;
                        assert direction != null; // there is no reason for it to be null
                        pos = pos.above().relative(direction, 1);

                        if (world.getBlockState(pos).isFaceSturdy(world, pos, direction.getOpposite())) {
                            stackAt.useOn(new UseOnContext(world, player, InteractionHand.OFF_HAND, stackAt, new BlockHitResult(Vec3.atCenterOf(pos), direction.getOpposite(), pos, false)));
                            ItemNBTHelper.setString(stack, TAG_SIDE, side.equals("Right") ? "Left" : "Right");
                        }
                        break;
                    case 1:
                        direction = player.getDirection().getClockWise();
                        pos = pos.above().relative(direction, 1);
                        if (world.getBlockState(pos).isFaceSturdy(world, pos, direction.getOpposite()))
                            stackAt.useOn(new UseOnContext(world, player, InteractionHand.OFF_HAND, stackAt, new BlockHitResult(Vec3.atCenterOf(pos), direction.getOpposite(), pos, false)));
                        break;
                    case 2:
                        direction = player.getDirection().getCounterClockWise();
                        pos = pos.above().relative(direction, 1);
                        if (world.getBlockState(pos).isFaceSturdy(world, pos, direction.getOpposite()))
                            stackAt.useOn(new UseOnContext(world, player, InteractionHand.OFF_HAND, stackAt, new BlockHitResult(Vec3.atCenterOf(pos), direction.getOpposite(), pos, false)));
                        break;
                    case 3:
                        if (world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP) && world.getBlockState(pos).isAir())
                            stackAt.useOn(new UseOnContext(world, player, InteractionHand.OFF_HAND, stackAt, new BlockHitResult(Vec3.atCenterOf(pos.below()), Direction.UP, pos.below(), false)));
                        break;
                    case 4:
                        if (world.getBlockState(pos.above().relative(player.getDirection().getClockWise(), 1)).isFaceSturdy(world, pos.above().relative(player.getDirection().getClockWise(), 1), player.getDirection().getCounterClockWise()) || world.getBlockState(pos.above().relative(player.getDirection().getCounterClockWise(), 1)).isFaceSturdy(world, pos.above().relative(player.getDirection().getCounterClockWise(), 1), player.getDirection().getClockWise())) {
                            direction = "Right".equals(side) ? player.getDirection().getCounterClockWise() : "Left".equals(side) ? player.getDirection().getClockWise() : null; // they are inverted because I invert in the for loop
                            assert direction != null; // there is no reason for it to be null
                            for (int i = 0; i < 2; i++) {
                                direction = direction.getOpposite();
                                pos = pos.above().relative(direction, 1);
                                if (world.getBlockState(pos).isFaceSturdy(world, pos, direction.getOpposite())) {
                                    stackAt.useOn(new UseOnContext(world, player, InteractionHand.OFF_HAND, stackAt, new BlockHitResult(Vec3.atCenterOf(pos), direction.getOpposite(), pos, false)));
                                    ItemNBTHelper.setString(stack, TAG_SIDE, side.equals("Right") ? "Left" : "Right");
                                    break;
                                }
                            }
                        } else if (world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP) && world.getBlockState(pos).isAir())
                            stackAt.useOn(new UseOnContext(world, player, InteractionHand.OFF_HAND, stackAt, new BlockHitResult(Vec3.atCenterOf(pos.below()), Direction.UP, pos.below(), false)));
                        break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onSneakRightClick(PlayerInteractEvent.RightClickItem evt) {
        if (evt.getItemStack().getItem() == MegaBotanyItems.BOTTLED_FLAME.get() && evt.getEntity().isCrouching()) {
            ItemStack stack = evt.getItemStack();
            evt.setCancellationResult(InteractionResult.CONSUME);

            if (ItemNBTHelper.getInt(stack, TAG_MODE, 0) < 4)
                ItemNBTHelper.setInt(stack, TAG_MODE, ItemNBTHelper.getInt(stack, TAG_MODE, 0) + 1);
            else
                ItemNBTHelper.setInt(stack, TAG_MODE, 0);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player)
            return !player.isCrouching();
        else
            return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltip, flags);
        tooltip.add(Component.translatable("misc.megabotany.mode").append(": ").append(Component.translatable("misc.megabotany.bottled_flame.mode." + ItemNBTHelper.getInt(stack, TAG_MODE, 0))));
    }
}

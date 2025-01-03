package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.api.block_entity.DimensionalFlowerWandBindable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.block.Bound;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.WandOfTheForestItem;

import java.util.Optional;

@Mixin(WandOfTheForestItem.class)
public abstract class WandOfTheForestItemMixin {
    @Unique
    private static final String TAG_DIMENSION_ID = "dimensionId";

    @Unique
    private static boolean tryCompleteBinding(BlockPos src, String dimensionId, ItemStack stack, UseOnContext ctx) {
        BlockPos dest = ctx.getClickedPos();
        if (!dest.equals(src)) {
            ItemNBTHelper.setString(stack, TAG_DIMENSION_ID, "");

            BlockEntity tile = ctx.getLevel().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionId))).getBlockEntity(src);
            if (tile instanceof DimensionalFlowerWandBindable && ctx.getLevel().getBlockEntity(dest) instanceof DimensionalFlowerWandBindable bindable) {
                bindable.bindTo(ctx.getPlayer(), stack, src, tile.getLevel().dimension().location().toString(), ctx.getClickedFace());
                WandOfTheForestItem.setBindingAttempt(stack, Bound.UNBOUND_POS);
                return true;
            }
        }
        return false;
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void useOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = ctx.getItemInHand();
        Level world = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        Optional<BlockPos> boundPos = WandOfTheForestItem.getBindingAttempt(stack);
        String dimensionId = ItemNBTHelper.getString(stack, TAG_DIMENSION_ID, "");

        if (player == null) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        if (!dimensionId.isEmpty() && player.isSecondaryUseActive() && !world.isClientSide && boundPos.filter(loc -> tryCompleteBinding(loc, dimensionId, stack, ctx)).isPresent()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }


        BlockEntity tile = world.getBlockEntity(pos);
        if (WandOfTheForestItem.getBindMode(stack) && WandOfTheForestItem.getBindingAttempt(stack).isEmpty() && !world.isClientSide && tile instanceof DimensionalFlowerWandBindable && player.isShiftKeyDown()) {
            if (boundPos.filter(pos::equals).isPresent()) {
                WandOfTheForestItem.setBindingAttempt(stack, Bound.UNBOUND_POS);
            } else {
                WandOfTheForestItem.setBindingAttempt(stack, pos);
                ItemNBTHelper.setString(stack, TAG_DIMENSION_ID, world.dimension().location().toString());
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "setBindingAttempt", at = @At("HEAD"), remap = false)
    private static void setBindingAttempt(ItemStack stack, BlockPos pos, CallbackInfo ci) {
        ItemNBTHelper.setString(stack, TAG_DIMENSION_ID, "");
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (!ItemNBTHelper.getString(stack, TAG_DIMENSION_ID, "").isEmpty())
            ci.cancel();
    }
}

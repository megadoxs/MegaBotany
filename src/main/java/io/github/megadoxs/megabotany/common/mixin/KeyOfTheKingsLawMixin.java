package io.github.megadoxs.megabotany.common.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.item.relic.KeyOfTheKingsLawItem;
import vazkii.botania.xplat.XplatAbstractions;

@Mixin(KeyOfTheKingsLawItem.class)
public class KeyOfTheKingsLawMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
    private void use(Level world, Player player, @NotNull InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        var relic = XplatAbstractions.INSTANCE.findRelic(player.getItemInHand(hand));
        if (relic == null || !relic.isRightPlayer(player))
            cir.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(hand)));
    }
}

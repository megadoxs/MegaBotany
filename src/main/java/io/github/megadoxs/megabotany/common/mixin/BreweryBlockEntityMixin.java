package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.brew.BrewContainer;
import vazkii.botania.api.brew.BrewItem;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BreweryBlockEntity;
import vazkii.botania.common.crafting.BotanicalBreweryRecipe;

@Mixin(BreweryBlockEntity.class)
public abstract class BreweryBlockEntityMixin {

    @Shadow(remap = false)
    protected abstract void findRecipe();

    @Inject(method = "addItem", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void addItem(Player player, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof BrewContainer) {
            BreweryBlockEntity self = (BreweryBlockEntity) (Object) this;
            boolean did = false;

            for (int i = 0; i < self.inventorySize(); i++) {
                if (self.getItemHandler().getItem(i).isEmpty()) {
                    did = true;
                    ItemStack stackToAdd = stack.copyWithCount(1);
                    self.getItemHandler().setItem(i, stackToAdd);

                    if (player == null || !player.getAbilities().instabuild) {
                        stack.shrink(1);
                        if (stack.isEmpty() && player != null) {
                            player.setItemInHand(hand, ItemStack.EMPTY);
                        }
                    }

                    break;
                }
            }

            if (did) {
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
                findRecipe();
            }

            cir.setReturnValue(true);
        }
    }

    @Inject(method = "findRecipe", at = @At("HEAD"), cancellable = true, remap = false)
    private void findRecipe(CallbackInfo ci) {
        BreweryBlockEntity self = (BreweryBlockEntity) (Object) this;
        ItemStack stack = self.getItemHandler().getItem(0);

        if (stack.getItem() instanceof BrewContainer && stack.getItem() instanceof BrewItem brewItem) {

            BotanicalBreweryRecipe splash = new BotanicalBreweryRecipe(null, brewItem.getBrew(stack), Ingredient.of(Items.GUNPOWDER));
            BotanicalBreweryRecipe lingering = new BotanicalBreweryRecipe(null, brewItem.getBrew(stack), Ingredient.of(Items.DRAGON_BREATH));

            if (stack.is(MegaBotanyItems.INFINITE_BREW.get()) && splash.matches(self.getItemHandler(), self.getLevel()))
                self.recipe = splash;
            else if (stack.is(MegaBotanyItems.INFINITE_SPLASH_BREW.get()) && lingering.matches(self.getItemHandler(), self.getLevel()))
                self.recipe = lingering;

            if (self.recipe != null)
                self.getLevel().setBlockAndUpdate(self.getBlockPos(), BotaniaBlocks.brewery.defaultBlockState().setValue(BlockStateProperties.POWERED, true));
            ci.cancel();
        }
    }
}

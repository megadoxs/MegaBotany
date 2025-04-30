package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.item.relic.AFORing;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BaubleBoxItem;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(EquipmentHandler.class)
public abstract class EquipmentHandlerMixin {
    @Inject(method = "findOrEmpty(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectFindOrEmptyItem(Item item, LivingEntity living, CallbackInfoReturnable<ItemStack> cir) {
        if (living instanceof Player player) {
            Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(player).resolve().flatMap(handler -> handler.findFirstCurio(curio -> curio.getItem() instanceof AFORing));

            if (slotResult.isEmpty() || !(slotResult.get().stack().getItem() instanceof AFORing ring)) {
                return;
            }

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() instanceof BaubleBoxItem) {
                    SimpleContainer boxInventory = BaubleBoxItem.getInventory(player.getInventory().getItem(i));
                    for (int j = 0; j < ring.SIZE; j++) {
                        if (boxInventory.getItem(j).is(item)) {
                            cir.setReturnValue(boxInventory.getItem(j));
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "findOrEmpty(Ljava/util/function/Predicate;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectFindOrEmptyPredicate(Predicate<ItemStack> pred, LivingEntity living, CallbackInfoReturnable<ItemStack> cir) {
        if (living instanceof Player player) {
            Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(player).resolve().flatMap(handler -> handler.findFirstCurio(curio -> curio.getItem() instanceof AFORing));

            if (slotResult.isEmpty() || !(slotResult.get().stack().getItem() instanceof AFORing ring)) {
                return;
            }

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() instanceof BaubleBoxItem) {
                    SimpleContainer boxInventory = BaubleBoxItem.getInventory(player.getInventory().getItem(i));
                    for (int j = 0; j < ring.SIZE; j++) {
                        if (pred.test(boxInventory.getItem(j))) {
                            cir.setReturnValue(boxInventory.getItem(j));
                            return;
                        }
                    }
                }
            }
        }
    }
}

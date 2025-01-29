package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.client.model.MegaBotanyModelLayer;
import io.github.megadoxs.megabotany.common.item.equipment.armor.OrichalcosArmorItem;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.client.model.armor.ArmorModel;
import vazkii.botania.client.model.armor.ArmorModels;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

@Mixin(ArmorModels.class)
public abstract class ArmorModelsMixin {

    @Unique
    private static Map<EquipmentSlot, ArmorModel> orichalcos_female = Collections.emptyMap();

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At("HEAD"), remap = false)
    private static void init(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        try {
            Method makeMethod = ArmorModels.class.getDeclaredMethod("make", EntityRendererProvider.Context.class, ModelLayerLocation.class, ModelLayerLocation.class);
            makeMethod.setAccessible(true);

            orichalcos_female = (Map<EquipmentSlot, ArmorModel>) makeMethod.invoke(null, ctx, MegaBotanyModelLayer.ORICHALCOS_FEMALE_INNER_ARMOR, MegaBotanyModelLayer.ORICHALCOS_FEMALE_OUTER_ARMOR);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Inject(method = "get", at = @At("HEAD"), cancellable = true, remap = false)
    private static void get(ItemStack stack, CallbackInfoReturnable<ArmorModel> cir) {
        Item item = stack.getItem();
        if (item instanceof OrichalcosArmorItem armor) {
            cir.setReturnValue(orichalcos_female.get(armor.getEquipmentSlot()));
            cir.cancel();
        }
    }
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.item.equipment.armor.orichalcos.OrichalcosHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.photonium.PhotoniumHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.shadowium.ShadowiumHelmetItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.lang.reflect.Field;

@Deprecated(
        since = "1.21.+"
)
@ApiStatus.ScheduledForRemoval(
        inVersion = "1.21.+"
)

// will be removed in 1.21.+ when my PR about AncientWillContainer is merged

@Mixin(value = Player.class, priority = 999)
//loads before botania to make sure the value of terraWillCritTarget doesn't get set to null
public abstract class PlayerMixin {
    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private DamageSource onDamageTarget(DamageSource source, float amount) {
        try {
            Field terraWillCritTargetField = Player.class.getDeclaredField("terraWillCritTarget");
            terraWillCritTargetField.setAccessible(true);
            LivingEntity terraWillCritTarget = (LivingEntity) terraWillCritTargetField.get(this);

            if (terraWillCritTargetField.get(this) != null && source.getEntity() instanceof Player player) {
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof OrichalcosHelmetItem) {
                    DamageSource newSource = OrichalcosHelmetItem.onEntityAttacked(source, amount, player, terraWillCritTarget);
                    terraWillCritTargetField.set(this, null);
                    return newSource;
                } else if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ShadowiumHelmetItem) {
                    DamageSource newSource = ShadowiumHelmetItem.onEntityAttacked(source, amount, player, terraWillCritTarget);
                    terraWillCritTargetField.set(this, null);
                    return newSource;
                } else if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof PhotoniumHelmetItem) {
                    DamageSource newSource = PhotoniumHelmetItem.onEntityAttacked(source, amount, player, terraWillCritTarget);
                    terraWillCritTargetField.set(this, null);
                    return newSource;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return source;
    }
}

package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.item.relic.AchilledShield;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Shadow
    protected abstract ItemStack getPickupItem();

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), cancellable = true)
    private void onHitEntity(EntityHitResult pResult, CallbackInfo ci) {
        Entity entity = pResult.getEntity();
        AbstractArrow self = (AbstractArrow) (Object) this;

        //replace by capture local?
        Entity entity1 = self.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = self.damageSources().arrow(self, self);
        } else {
            damagesource = self.damageSources().arrow(self, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity) entity1).setLastHurtMob(entity);
            }
        }

        if (entity instanceof LivingEntity living && living.getUseItem().getItem() instanceof AchilledShield && living.isDamageSourceBlocked(damagesource) && living.getUseItem().getUseDuration() - living.getUseItemRemainingTicks() >= 5) {
            if (!self.level().isClientSide && self.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (self.pickup == AbstractArrow.Pickup.ALLOWED) {
                    self.spawnAtLocation(getPickupItem(), 0.1F);
                }

                self.discard();
            }
            ci.cancel();
        }
    }
}

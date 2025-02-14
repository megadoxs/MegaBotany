package io.github.megadoxs.megabotany.common.entity;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ThrownBrew extends ThrowableItemProjectile {
    public ThrownBrew(EntityType<? extends ThrownBrew> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownBrew(Level pLevel, LivingEntity pShooter) {
        super(MegaBotanyEntities.THROWN_BREW.get(), pShooter, pLevel);
    }

    public ThrownBrew(Level pLevel) {
        super(MegaBotanyEntities.THROWN_BREW.get(), pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            ItemStack itemstack = getItem();
            Brew brew = ((BrewItem) itemstack.getItem()).getBrew(itemstack);
            List<MobEffectInstance> list = new ArrayList<>();

            boolean hasInstantEffects = false;

            for (MobEffectInstance effect : brew.getPotionEffects(itemstack)) {
                MobEffectInstance newEffect = new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), true, true);
                if (effect.getEffect().isInstantenous()) {
                    hasInstantEffects = true;
                }
                list.add(newEffect);
            }

            if (isLingeringBrew(itemstack)) {
                this.makeAreaOfEffectCloud(itemstack, brew);
            } else {
                applySplash(list, pResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) pResult).getEntity() : null);
            }

            int i = hasInstantEffects ? 2007 : 2002;
            level().levelEvent(i, blockPosition(), brew.getColor(itemstack));
            discard();
        }
    }

    private static boolean isLingeringBrew(ItemStack stack) {
        return stack.is(MegaBotanyItems.INFINITE_LINGERING_BREW.get());
    }

    //Vanilla ThrownPotion.applySplash() Copy
    private void applySplash(List<MobEffectInstance> pEffectInstances, @Nullable Entity pTarget) {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            Entity entity = this.getEffectSource();

            for (LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = this.distanceToSqr(livingentity);
                    if (d0 < 16.0D) {
                        double d1;
                        if (livingentity == pTarget) {
                            d1 = 1.0D;
                        } else {
                            d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        }

                        for (MobEffectInstance mobeffectinstance : pEffectInstances) {
                            MobEffect mobeffect = mobeffectinstance.getEffect();
                            if (mobeffect.isInstantenous()) {
                                mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), d1);
                            } else {
                                int i = mobeffectinstance.mapDuration((p_267930_) -> (int) (d1 * (double) p_267930_ + 0.5D));
                                MobEffectInstance mobeffectinstance1 = new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible());
                                if (!mobeffectinstance1.endsWithin(20)) {
                                    livingentity.addEffect(mobeffectinstance1, entity);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //should work
    private void makeAreaOfEffectCloud(ItemStack pStack, Brew brew) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity) entity);
        }

        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(10);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.setFixedColor(brew.getColor(pStack));

        for (MobEffectInstance mobeffectinstance : brew.getPotionEffects(pStack)) {
            areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
        }

        this.level().addFreshEntity(areaeffectcloud);
    }
}

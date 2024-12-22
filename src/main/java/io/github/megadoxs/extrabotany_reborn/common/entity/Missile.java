package io.github.megadoxs.extrabotany_reborn.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.helper.VecHelper;

import java.util.List;
import java.util.function.Predicate;

public class Missile extends ThrowableProjectile {
    private static final String TAG_TIME = "time";
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(Missile.class, EntityDataSerializers.INT);

    double lockX, lockY = Integer.MIN_VALUE, lockZ;
    int time = 0;

    public Missile(EntityType<? extends Missile> type, Level world) {
        super(type, world);
    }

    public Missile(LivingEntity owner, int type) {
        super(ModEntities.EXPLOSIVE_MISSILE.get(), owner, owner.level());
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(TARGET, 0);
    }

    public void setTarget(LivingEntity e) {
        entityData.set(TARGET, e == null ? -1 : e.getId());
    }

    public LivingEntity getTargetEntity() {
        int id = entityData.get(TARGET);
        Entity e = level().getEntity(id);
        if (e instanceof LivingEntity le) {
            return le;
        }

        return null;
    }

    @Override
    public void tick() {
        double lastTickPosX = this.xOld;
        double lastTickPosY = this.yOld;
        double lastTickPosZ = this.zOld;

        super.tick();

        if (!level().isClientSide && (!findTarget() || time > 40)) {
            discard();
            return;
        }

        Vec3 thisVec = VecHelper.fromEntityCenter(this);
        Vec3 oldPos = new Vec3(lastTickPosX, lastTickPosY, lastTickPosZ);
        Vec3 diff = thisVec.subtract(oldPos);
        Vec3 step = diff.normalize().scale(0.05);
        int steps = (int) (diff.length() / step.length());
        Vec3 particlePos = oldPos;

        SparkleParticleData data = SparkleParticleData.corrupt(0.8F, 1F, 0.0F, 1F, 2);
        for (int i = 0; i < steps; i++) {
            level().addParticle(data, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);

            if (level().random.nextInt(steps) <= 1) {
                level().addParticle(data, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 0, 0, 0);
            }

            particlePos = particlePos.add(step);
        }

        LivingEntity target = getTargetEntity();
        if (target != null) {
            if (lockY == Integer.MIN_VALUE) {
                lockX = target.getX();
                lockY = target.getY();
                lockZ = target.getZ();
            }

            Vec3 targetVec = new Vec3(lockX, lockY, lockZ);
            Vec3 diffVec = targetVec.subtract(thisVec);
            Vec3 motionVec = diffVec.normalize().scale(0.5);
            setDeltaMovement(motionVec);
            if (time < 10) {
                setDeltaMovement(getDeltaMovement().x(), Math.abs(getDeltaMovement().y()), getDeltaMovement().z());
            }

            List<LivingEntity> targetList = level().getEntitiesOfClass(LivingEntity.class, new AABB(getX() - 0.5, getY() - 0.5, getZ() - 0.5, getX() + 0.5, getY() + 0.5, getZ() + 0.5));
            if (targetList.contains(target)) {

                target.hurt(this.getDamageSource(), 12);
                discard();
            }

            if (diffVec.length() < 1) {
                discard();
            }
        }

        time++;
    }

    private DamageSource getDamageSource() {
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity livingOwner) {
            return damageSources().mobAttack(livingOwner);
        } else {
            return damageSources().generic();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
        cmp.putInt(TAG_TIME, time);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
        time = cmp.getInt(TAG_TIME);
    }

    // make it only target entity that are in the range of the arena
    public boolean findTarget() {
        LivingEntity target = getTargetEntity();
        if (target != null) {
            if (target.isAlive()) {
                return true;
            } else {
                target = null;
                setTarget(null);
            }
        }

        double range = 12;
        AABB bounds = new AABB(getX() - range, getY() - range, getZ() - range, getX() + range, getY() + range, getZ() + range);
        DamageSource source = this.getDamageSource();
        Predicate<Entity> vulnerableTo = e -> !e.isInvulnerableTo(source);
        List<? extends LivingEntity> entities;
        entities = level().getEntitiesOfClass(Player.class, bounds, EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(vulnerableTo));

        if (!entities.isEmpty()) {
            target = entities.get(level().random.nextInt(entities.size()));
            setTarget(target);
        }

        return target != null;
    }

    public static Predicate<Entity> targetPredicate(Entity owner) {
        return target -> target instanceof LivingEntity living && shouldTarget(owner, living);
    }

    public static boolean shouldTarget(Entity owner, LivingEntity e) {
        // don't target tamed creatures...
        if (e instanceof TamableAnimal animal && animal.isTame() || e instanceof AbstractHorse horse && horse.isTamed()) {
            return false;
        }

        // ...but other mobs die
        return e instanceof Enemy;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hit) {
        super.onHitBlock(hit);
        BlockState state = level().getBlockState(hit.getBlockPos());
        if (!level().isClientSide
                && !(state.getBlock() instanceof BushBlock)
                && !state.is(BlockTags.LEAVES)) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide && hit.getEntity() == getTargetEntity()) {
            discard();
        }
    }
}

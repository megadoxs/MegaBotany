package io.github.megadoxs.megabotany.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

public class LandMine extends Entity {
    public GaiaGuardianIII summoner;
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(LandMine.class, EntityDataSerializers.INT);

    public LandMine(EntityType<LandMine> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        setDeltaMovement(Vec3.ZERO);
        super.tick();

        float range = getBbWidth() / 2;

        float r = 0;
        float g = 0;
        float b = 0;

        switch (getMineType()) {
            case 0 -> {
                r = 0.2F;
                g = 0F;
                b = 0.2F;
            }
            case 1 -> r = 1;
            case 2 -> {
                r = 1;
                g = 1;
            }
            case 3 -> {
                r = 0.6F;
                g = 0.8F;
                b = 1F;
            }
        }

        for (int i = 0; i < 6; i++) {
            WispParticleData data = WispParticleData.wisp(0.4F, r, g, b, (float) 1);
            level().addParticle(data, getX() - range + Math.random() * range * 2, getY(), getZ() - range + Math.random() * range * 2, 0, - -0.015F, 0);
        }

        if (tickCount >= 55) {
            level().playSound(null, getX(), getY(), getZ(), BotaniaSounds.gaiaTrap, SoundSource.NEUTRAL, 1F, 1F);

            float m = 0.35F;
            g = 0.4F;
            for (int i = 0; i < 25; i++) {
                WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
                level().addParticle(data, getX(), getY() + 1, getZ(), (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);
            }

            if (!level().isClientSide) {
                List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox());
                for (Player player : players) {
                    player.hurt(this.damageSources().indirectMagic(this, summoner), 10);
                    switch (getMineType()) {
                        case 0 -> { // all effects should have their curative list cleared
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 25, 0));
                            MobEffectInstance wither = new MobEffectInstance(MobEffects.WITHER, 120, 2);
                            wither.getCurativeItems().clear();
                            player.addEffect(wither);
                        }
                        case 1 -> {
                            player.hurt(this.damageSources().indirectMagic(this, summoner), 10);
                            Vec3 direction = player.position().subtract(position()).normalize();
                            player.setDeltaMovement(new Vec3(direction.x * 1.5, 0.5, direction.z * 1.5));
                            player.setSecondsOnFire(10);
                        }
                        case 2 -> {
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 50, 0));
                            player.getCooldowns().addCooldown(player.getUseItem().getItem(), 100);
                            player.stopUsingItem();
                            if (player.getUseItem() != player.getMainHandItem())
                                player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 100);
                        }
                        case 3 -> {
                            player.clearFire();
                            if (player.canFreeze())
                                player.setTicksFrozen(player.getTicksFrozen() + 200);
                        }
                    }
                }
            }

            discard();
        }
    }

    public int getMineType() {
        return entityData.get(TYPE);
    }

    public void setMineType(int i) {
        entityData.set(TYPE, i);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(TYPE, 0);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag var1) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag var1) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}

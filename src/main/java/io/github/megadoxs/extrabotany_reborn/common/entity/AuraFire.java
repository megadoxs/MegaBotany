package io.github.megadoxs.extrabotany_reborn.common.entity;

import io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble.JingweiFeather;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;

public class AuraFire extends ThrowableProjectile {

    public AuraFire(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 80)
            this.kill();
        if (this.level().isClientSide())
            for (int i = 0; i < 5; i++)
                this.level().addParticle(ParticleTypes.FLAME, this.getX() + Math.random() * 0.4F - 0.2F, this.getY() + Math.random() * 0.4F - 0.2F, this.getZ() + Math.random() * 0.4F - 0.2F, 0, 0, 0);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(getOwner() instanceof Player player){
            float dmg = (float) (4 + player.getAttributeValue(Attributes.ATTACK_DAMAGE));
            result.getEntity().hurt(player.damageSources().playerAttack(player), dmg);
            player.setAbsorptionAmount(Math.min(10, player.getAbsorptionAmount() + 1F));
            ManaItemHandler.instance().requestManaExact(new ItemStack(Items.APPLE), player, 300, true);
            this.kill();
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        if(pTarget == getOwner())
            return false;
        else
            return super.canHitEntity(pTarget);
    }

    @Override
    protected float getGravity() {
        return 0f;
    }

    @Override
    protected void defineSynchedData() {}
}

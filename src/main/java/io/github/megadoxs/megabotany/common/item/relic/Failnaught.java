package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.common.MegaBotany;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.LensEffectItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.Consumer;

public class Failnaught extends BowItem implements LensEffectItem {
    public static final int manaCost = 50;

    public Failnaught(Properties props) {
        super(props);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 144000;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, ManaItemHandler.instance().requestManaExactForTool(pStack, player, manaCost, false));
            if (i < 0) {
                return;
            }

            if (ManaItemHandler.instance().requestManaExactForTool(pStack, player, manaCost, false)) {
                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    if (!pLevel.isClientSide) {
                        ManaBurstEntity projectile = new ManaBurstEntity(player);

                        projectile.setColor(0x20FF20);
                        projectile.setMana(300);
                        projectile.setStartingMana(300);
                        projectile.setMinManaLoss(40);
                        projectile.setManaLossPerTick(4);
                        projectile.setGravity(0);
                        // speed should depend on how long to bow was charged for
                        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(5));
                        projectile.setSourceLens(player.getMainHandItem());

                        pLevel.addFreshEntity(projectile);
                    }

                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, new ResourceLocation(MegaBotany.MOD_ID, "challenge/failnaught"));
    }

    @Override
    public void apply(ItemStack stack, BurstProperties props, Level level) {}

    @Override
    public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
        return shouldKill;
    }

    @Override
    public void updateBurst(ManaBurst burst, ItemStack stack) {
        ThrowableProjectile projectile = burst.entity();
        if (projectile.level().isClientSide) {
            return;
        }

        AABB axis = new AABB(projectile.getX(), projectile.getY(), projectile.getZ(), projectile.xOld, projectile.yOld, projectile.zOld).inflate(1);
        List<LivingEntity> entities = projectile.level().getEntitiesOfClass(LivingEntity.class, axis);
        for (LivingEntity living : entities) {
            if(projectile.getOwner() != null && living instanceof Player player && player.is(projectile.getOwner()))
                continue;

            if (living.hurtTime == 0) {
                if (!burst.isFake()){
                    double damage = 10D;

                    int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                    if (j > 0) {
                        damage += ((double)j * (double)2.5F + (double)0.5F);
                    }

                    int knockback = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);

                    living.hurt(projectile.getOwner() != null ? projectile.damageSources().indirectMagic(projectile, projectile.getOwner()) : projectile.damageSources().magic(), (float) damage);

                    if (knockback > 0) {
                        double d0 = Math.max(0.0D, 1.0D - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        Vec3 vec3 = projectile.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)knockback * 0.6D * d0);
                        if (vec3.lengthSqr() > 0.0D) {
                            living.push(vec3.x, 0.1D, vec3.z);
                        }
                    }

                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0)
                        living.setSecondsOnFire(5);
                }
                break;
            }
        }
    }

    @Override
    public boolean doParticles(ManaBurst burst, ItemStack stack) {
        return true;
    }

    @Override
    public int getManaToTransfer(ManaBurst burst, ItemStack stack, ManaReceiver receiver) {
        return 0;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }
}

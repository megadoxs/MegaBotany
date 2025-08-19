package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AchillesShield extends ShieldItem {
    public AchillesShield(Properties pProperties) {
        super(pProperties);
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

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, new ResourceLocation(MegaBotany.MOD_ID, MegaBotanyItems.ACHILLES_SHIELD.getId().getPath()));
    }

    @SubscribeEvent
    public static void ShieldBlockEvent(ShieldBlockEvent event) {
        if (event.getEntity().getUseItem().getItem() instanceof AchillesShield && event.getEntity().getUseItem().getUseDuration() - event.getEntity().getUseItemRemainingTicks() >= 5) {
            if (event.getDamageSource().getDirectEntity() instanceof Projectile projectile && projectile.getOwner() != null) {
                projectile.setDeltaMovement(projectile.getOwner().position().subtract(projectile.position()).normalize().scale(3));
                projectile.setYRot(projectile.getYRot() + 180.0F);
                projectile.setOwner(event.getEntity());
            } else if (event.getDamageSource().getDirectEntity() instanceof LivingEntity living) {
                living.knockback(Math.max(0, 1 - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)), event.getEntity().getX() - living.getX(), event.getEntity().getZ() - living.getZ());
                // return partial maybe 20-25% of damage to player
                living.hurt(event.getEntity().level().damageSources().thorns(event.getEntity()), (float) (event.getBlockedDamage() * 0.2));
            }

            event.setShieldTakesDamage(false);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var relic = XplatAbstractions.INSTANCE.findRelic(pPlayer.getItemInHand(pUsedHand));
        if (relic != null && relic.isRightPlayer(pPlayer))
            return super.use(pLevel, pPlayer, pUsedHand);
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }
}

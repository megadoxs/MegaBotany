package io.github.megadoxs.extrabotany_reborn.common.item.equipment.armor;

import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.api.mana.ManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.List;
import java.util.Locale;

public class OrichalcosHelmetItem extends OrichalcosArmorItem implements ManaDiscountArmor, AncientWillContainer {

    public static final String TAG_ANCIENT_WILL = "AncientWill";

    public OrichalcosHelmetItem(Type type, Version version, Properties props) {
        super(type, version, props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
        return this.hasArmorSet(player) ? 0.25F : 0.0F;
    }

    public void addAncientWill(ItemStack stack, AncientWillContainer.AncientWillType will) {
        ItemNBTHelper.setBoolean(stack, "AncientWill_" + will.name().toLowerCase(Locale.ROOT), true);
    }

    public boolean hasAncientWill(ItemStack stack, AncientWillContainer.AncientWillType will) {
        return hasAncientWill_(stack, will);
    }

    private static boolean hasAncientWill_(ItemStack stack, AncientWillContainer.AncientWillType will) {
        return ItemNBTHelper.getBoolean(stack, "AncientWill_" + will.name().toLowerCase(Locale.ROOT), false);
    }

    public static boolean hasOrichalcosArmorSet(Player player) {
        return ((OrichalcosHelmetItem) ModItems.ORICHALCOS_HELMET_FEMALE.get()).hasArmorSet(player) || ((OrichalcosHelmetItem) ModItems.ORICHALCOS_HELMET_FEMALE.get()).hasArmorSet(player); //will be the male version here
    }

    public static float getCritDamageMult(Player player) {
        if (hasOrichalcosArmorSet(player)) {
            ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
            if (!stack.isEmpty() && stack.getItem() instanceof OrichalcosHelmetItem && hasAncientWill_(stack, AncientWillType.DHAROK)) {
                return 1.0F + (1.0F - player.getHealth() / player.getMaxHealth()) * 0.5F;
            }
        }

        return 1.0F;
    }

    @Override
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        super.addArmorSetDescription(stack, list);
        AncientWillContainer.AncientWillType[] var3 = AncientWillType.values();

        for (AncientWillType type : var3) {
            if (hasAncientWill_(stack, type)) {
                String var10001 = type.name();
                list.add(Component.translatable("botania.armorset.will_" + var10001.toLowerCase(Locale.ROOT) + ".desc").withStyle(ChatFormatting.GRAY));
            }
        }

    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClientSide && entity instanceof Player player) {
            if (player.getInventory().armor.contains(stack) && this.hasArmorSet(player)) {
                int food = player.getFoodData().getFoodLevel();
                if (food > 0 && food < 18 && player.isHurt() && player.tickCount % 80 == 0) {
                    player.heal(1.0F);
                }

                if (player.tickCount % 10 == 0) {
                    ManaItemHandler.instance().dispatchManaExact(stack, player, 20, true);
                }
            }
        }

    }

    public static DamageSource onEntityAttacked(DamageSource source, float amount, Player player, LivingEntity entity) {
        if (hasOrichalcosArmorSet(player)) {
            ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
            if (!stack.isEmpty() && stack.getItem() instanceof OrichalcosHelmetItem) {
                if (hasAncientWill_(stack, AncientWillType.AHRIM)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1));
                }

                if (hasAncientWill_(stack, AncientWillType.GUTHAN)) {
                    player.heal(amount * 0.25F);
                }

                if (hasAncientWill_(stack, AncientWillType.TORAG)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
                }

                if (hasAncientWill_(stack, AncientWillType.VERAC)) {
                    source = BotaniaDamageTypes.Sources.playerAttackArmorPiercing(player.level().registryAccess(), player);
                }

                if (hasAncientWill_(stack, AncientWillType.KARIL)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
                }
            }
        }

        return source;
    }

    @SubscribeEvent // if you wear the full armor set you won't be affected by removable effects. This won't remove already applied effect to the entity
    public void effectImmune(MobEffectEvent.Applicable event){ // will make it work for all entity that could wear the armor (zombie, skeleton, etc)
        if(event.getEntity() instanceof Player player && hasArmorSet(player) && !event.getEffectInstance().getEffect().isBeneficial() && !event.getEffectInstance().getEffect().getCurativeItems().isEmpty())
            event.setResult(Event.Result.DENY);
    }
}

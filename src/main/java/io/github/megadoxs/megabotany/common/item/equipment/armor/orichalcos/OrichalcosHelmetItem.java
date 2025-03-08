package io.github.megadoxs.megabotany.common.item.equipment.armor.orichalcos;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.api.mana.ManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID)
public class OrichalcosHelmetItem extends OrichalcosArmorItem implements ManaDiscountArmor, AncientWillContainer {
    public OrichalcosHelmetItem(Type type, Version version, Properties props) {
        super(type, version, props);
    }

    @Override
    public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
        return this.hasArmorSet(player) ? 0.4F : 0.0F;
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
        return ((OrichalcosHelmetItem) MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get()).hasArmorSet(player) || ((OrichalcosHelmetItem) MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get()).hasArmorSet(player); //will be the male version here
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

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event){
        if(event.getEntity() instanceof ServerPlayer player && event.getSlot().getType() == EquipmentSlot.Type.ARMOR && (event.getFrom().getItem() instanceof OrichalcosArmorItem || event.getTo().getItem() instanceof OrichalcosArmorItem)){
            AttributeMap attributes = player.getAttributes();
            Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
            if(OrichalcosHelmetItem.hasOrichalcosArmorSet(player)){
                if (!attributes.hasModifier(Attributes.ARMOR, BOOST_UUID))
                    attributeModifiers.put(Attributes.ARMOR, ARMOR_BOOST);
                if (!attributes.hasModifier(Attributes.ATTACK_DAMAGE, BOOST_UUID))
                    attributeModifiers.put(Attributes.ATTACK_DAMAGE, ATTACK_BOOST);
                if (!attributes.hasModifier(Attributes.MOVEMENT_SPEED, BOOST_UUID))
                    attributeModifiers.put(Attributes.MOVEMENT_SPEED, SPEED_BOOST);
                if (!attributes.hasModifier(Attributes.MAX_HEALTH, BOOST_UUID))
                    attributeModifiers.put(Attributes.MAX_HEALTH, HEALTH_BOOST);

                if(!attributeModifiers.isEmpty()){
                    attributes.addTransientAttributeModifiers(attributeModifiers);
                    player.heal(20);
                    player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
                }
            }
            else {
                if (attributes.hasModifier(Attributes.ARMOR, BOOST_UUID))
                    attributeModifiers.put(Attributes.ARMOR, ARMOR_BOOST);
                if (attributes.hasModifier(Attributes.ATTACK_DAMAGE, BOOST_UUID))
                    attributeModifiers.put(Attributes.ATTACK_DAMAGE, ATTACK_BOOST);
                if (attributes.hasModifier(Attributes.MOVEMENT_SPEED, BOOST_UUID))
                    attributeModifiers.put(Attributes.MOVEMENT_SPEED, SPEED_BOOST);
                if (attributes.hasModifier(Attributes.MAX_HEALTH, BOOST_UUID))
                    attributeModifiers.put(Attributes.MAX_HEALTH, HEALTH_BOOST);

                if (!attributeModifiers.isEmpty()) {
                    float health = player.getHealth() / player.getMaxHealth();
                    attributes.removeAttributeModifiers(attributeModifiers);
                    player.setHealth(health * player.getMaxHealth());
                    player.connection.send(new ClientboundSetHealthPacket(health * player.getMaxHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
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

    @SubscribeEvent
    public void effectImmune(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player && hasArmorSet(player) && !event.getEffectInstance().getEffect().isBeneficial() && !event.getEffectInstance().getEffect().getCurativeItems().isEmpty())
            event.setResult(Event.Result.DENY);
    }
}

package io.github.megadoxs.megabotany.common.item.equipment.armor.shadowium;

import com.google.common.base.Suppliers;
import io.github.megadoxs.megabotany.api.item.MegaBotanyArmorMaterial;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ShadowiumArmorItem extends ManasteelArmorItem {
    private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[]{new ItemStack(MegaBotanyItems.SHADOWIUM_HELMET.get()), new ItemStack(MegaBotanyItems.SHADOWIUM_CHESTPLATE.get()), new ItemStack(MegaBotanyItems.SHADOWIUM_LEGGINGS.get()), new ItemStack(MegaBotanyItems.SHADOWIUM_BOOTS.get())});
    protected static final UUID BOOST_UUID = UUID.fromString("fb7c537c-6ac8-4a93-b107-09e0a0b0b9ee");
    protected static final AttributeModifier NIGHT_ARMOR_BOOST = new AttributeModifier(BOOST_UUID, "Night Armor Boost", 4.0, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier NIGHT_ATTACK_BOOST = new AttributeModifier(BOOST_UUID, "Night Attack Boost", 2, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier NIGHT_SPEED_BOOST = new AttributeModifier(BOOST_UUID, "Night Speed Boost", 0.1, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier NIGHT_HEALTH_BOOST = new AttributeModifier(BOOST_UUID, "Night Health Boost", 20, AttributeModifier.Operation.ADDITION);

    public ShadowiumArmorItem(Type type, Properties props) {
        super(type, MegaBotanyArmorMaterial.SHADOWIUM, props);
    }

    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return "megabotany:textures/model/armor_shadowium.png";
    }

    @Override
    public ItemStack[] getArmorSetStacks() {
        return armorSet.get();
    }

    @Override
    public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
        if (player == null) {
            return false;
        } else {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                return false;
            } else {
                return switch (slot) {
                    case HEAD -> stack.is(MegaBotanyItems.SHADOWIUM_HELMET.get());
                    case CHEST -> stack.is(MegaBotanyItems.SHADOWIUM_CHESTPLATE.get());
                    case LEGS -> stack.is(MegaBotanyItems.SHADOWIUM_LEGGINGS.get());
                    case FEET -> stack.is(MegaBotanyItems.SHADOWIUM_BOOTS.get());
                    default -> false;
                };
            }
        }
    }

    @Override
    public MutableComponent getArmorSetName() {
        return Component.translatable("armor.megabotany.shadowium.name");
    }

    @Override
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("armor.megabotany.shadowium.desc0").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("armor.megabotany.shadowium.desc1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        super.appendHoverText(stack, world, list, flags);
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("armor.megabotany.shadowium.set_bonus").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("attribute.modifier.plus." + NIGHT_ARMOR_BOOST.getOperation().toValue(), ((int) NIGHT_ARMOR_BOOST.getAmount() / 4), Component.translatable(Attributes.ARMOR.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + NIGHT_ATTACK_BOOST.getOperation().toValue(), NIGHT_ATTACK_BOOST.getAmount() / 4, Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + NIGHT_SPEED_BOOST.getOperation().toValue(), NIGHT_SPEED_BOOST.getAmount() / 4, Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + NIGHT_HEALTH_BOOST.getOperation().toValue(), ((int) NIGHT_HEALTH_BOOST.getAmount() / 4), Component.translatable(Attributes.MAX_HEALTH.getDescriptionId())).withStyle(ChatFormatting.BLUE));
    }
}

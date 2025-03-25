package io.github.megadoxs.megabotany.common.item.equipment.armor.photonium;

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

public class PhotoniumArmorItem extends ManasteelArmorItem {
    private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[]{new ItemStack(MegaBotanyItems.PHOTONIUM_HELMET.get()), new ItemStack(MegaBotanyItems.PHOTONIUM_CHESTPLATE.get()), new ItemStack(MegaBotanyItems.PHOTONIUM_LEGGINGS.get()), new ItemStack(MegaBotanyItems.PHOTONIUM_BOOTS.get())});
    protected static final UUID BOOST_UUID = UUID.fromString("90319673-3e5c-4fb4-bcaa-01d5c8d510e3");
    protected static final AttributeModifier DAY_ARMOR_BOOST = new AttributeModifier(BOOST_UUID, "Day Armor Boost", 4.0, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier DAY_ATTACK_BOOST = new AttributeModifier(BOOST_UUID, "Day Attack Boost", 2, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier DAY_SPEED_BOOST = new AttributeModifier(BOOST_UUID, "Day Speed Boost", 0.1, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier DAY_HEALTH_BOOST = new AttributeModifier(BOOST_UUID, "Day Health Boost", 20, AttributeModifier.Operation.ADDITION);

    public PhotoniumArmorItem(Type type, Properties props) {
        super(type, MegaBotanyArmorMaterial.PHOTONIUM, props);
    }

    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return "megabotany:textures/model/armor_photonium.png";
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
                    case HEAD -> stack.is(MegaBotanyItems.PHOTONIUM_HELMET.get());
                    case CHEST -> stack.is(MegaBotanyItems.PHOTONIUM_CHESTPLATE.get());
                    case LEGS -> stack.is(MegaBotanyItems.PHOTONIUM_LEGGINGS.get());
                    case FEET -> stack.is(MegaBotanyItems.PHOTONIUM_BOOTS.get());
                    default -> false;
                };
            }
        }
    }

    @Override
    public MutableComponent getArmorSetName() {
        return Component.translatable("armor.megabotany.photonium.name");
    }

    @Override
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("armor.megabotany.photonium.desc0").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("armor.megabotany.photonium.desc1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        super.appendHoverText(stack, world, list, flags);
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("armor.megabotany.photonium.set_bonus").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("attribute.modifier.plus." + DAY_ARMOR_BOOST.getOperation().toValue(), ((int) DAY_ARMOR_BOOST.getAmount() / 4), Component.translatable(Attributes.ARMOR.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + DAY_ATTACK_BOOST.getOperation().toValue(), DAY_ATTACK_BOOST.getAmount() / 4, Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + DAY_SPEED_BOOST.getOperation().toValue(), DAY_SPEED_BOOST.getAmount() / 4, Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + DAY_HEALTH_BOOST.getOperation().toValue(), ((int) DAY_HEALTH_BOOST.getAmount() / 4), Component.translatable(Attributes.MAX_HEALTH.getDescriptionId())).withStyle(ChatFormatting.BLUE));
    }
}

package io.github.megadoxs.megabotany.common.item.equipment.armor.orichalcos;

import com.google.common.base.Suppliers;
import io.github.megadoxs.megabotany.api.item.MegaBotanyArmorMaterial;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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

public class OrichalcosArmorItem extends ManasteelArmorItem {
    private final Version version;
    private static final Supplier<ItemStack[]> femaleArmorSet = Suppliers.memoize(() -> new ItemStack[]{new ItemStack(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get()), new ItemStack(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE.get()), new ItemStack(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE.get()), new ItemStack(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE.get())});
    protected static final UUID BOOST_UUID = UUID.fromString("36d1d9c2-df19-4abf-9b8f-1ad903db0e6e");
    protected static final AttributeModifier ARMOR_BOOST = new AttributeModifier(BOOST_UUID, "Armor Boost", 4.0, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier ATTACK_BOOST = new AttributeModifier(BOOST_UUID, "Attack Boost", 2, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier SPEED_BOOST = new AttributeModifier(BOOST_UUID, "Speed Boost", 0.1, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier HEALTH_BOOST = new AttributeModifier(BOOST_UUID, "Health Boost", 20, AttributeModifier.Operation.ADDITION);

    public OrichalcosArmorItem(Type type, Version version, Properties props) {
        super(type, MegaBotanyArmorMaterial.ORICHALCOS, props);
        this.version = version;
    }

    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return "megabotany:textures/model/armor_orichalcos_" + version.toString().toLowerCase() + ".png";
    }

    @Override
    public ItemStack[] getArmorSetStacks() {
        return switch (version) {
            case FEMALE -> femaleArmorSet.get();
            case MALE -> null; // will be replaced with maleArmorSet.get();
        };
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
                return switch (version) {
                    case FEMALE -> switch (slot) {
                        case HEAD -> stack.is(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get());
                        case CHEST -> stack.is(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE.get());
                        case LEGS -> stack.is(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE.get());
                        case FEET -> stack.is(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE.get());
                        default -> false;
                    };
                    case MALE -> false; //will be the same as female switch
                };
            }
        }
    }

    @Override
    public MutableComponent getArmorSetName() {
        return Component.translatable("armor.megabotany.orichalcos_" + version.toString().toLowerCase() + ".name");
    }

    @Override
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("armor.megabotany.orichalcos.desc0").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("armor.megabotany.orichalcos.desc1").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("armor.megabotany.orichalcos.desc2").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        super.appendHoverText(stack, world, list, flags);
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("armor.megabotany.orichalcos.set_bonus").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("attribute.modifier.plus." + ARMOR_BOOST.getOperation().toValue(), ((int) ARMOR_BOOST.getAmount() / 4), Component.translatable(Attributes.ARMOR.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + ATTACK_BOOST.getOperation().toValue(), ATTACK_BOOST.getAmount() / 4, Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + SPEED_BOOST.getOperation().toValue(), SPEED_BOOST.getAmount() / 4, Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId())).withStyle(ChatFormatting.BLUE));
        list.add(Component.translatable("attribute.modifier.plus." + HEALTH_BOOST.getOperation().toValue(), ((int) HEALTH_BOOST.getAmount() / 4), Component.translatable(Attributes.MAX_HEALTH.getDescriptionId())).withStyle(ChatFormatting.BLUE));
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    public enum Version {
        FEMALE,
        MALE
    }
}

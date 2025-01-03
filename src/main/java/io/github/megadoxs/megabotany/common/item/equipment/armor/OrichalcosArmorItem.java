package io.github.megadoxs.megabotany.common.item.equipment.armor;

import com.google.common.base.Suppliers;
import io.github.megadoxs.megabotany.api.item.ModArmorMaterials;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import java.util.List;
import java.util.function.Supplier;

public class OrichalcosArmorItem extends ManasteelArmorItem {
    private final Version version;
    private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[]{new ItemStack(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get()), new ItemStack(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE.get()), new ItemStack(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE.get()), new ItemStack(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE.get())});

    public OrichalcosArmorItem(Type type, Version version, Properties props) {
        super(type, ModArmorMaterials.ORICHALCOS, props);
        this.version = version;
    }

    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return "megabotany:textures/model/armor_orichalcos_" + version.toString().toLowerCase() + ".png";
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
                if (version == Version.FEMALE) {
                    return switch (slot) {
                        case HEAD -> stack.is(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get());
                        case CHEST -> stack.is(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE.get());
                        case LEGS -> stack.is(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE.get());
                        case FEET -> stack.is(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE.get());
                        default -> false;
                    };
                } else {
                    // will contain the logic for the male version
                    return false;
                }
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

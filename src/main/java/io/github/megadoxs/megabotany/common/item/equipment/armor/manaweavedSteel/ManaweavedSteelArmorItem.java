package io.github.megadoxs.megabotany.common.item.equipment.armor.manaweavedSteel;

import com.google.common.base.Suppliers;
import io.github.megadoxs.megabotany.api.item.MegaBotanyArmorMaterials;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import java.util.List;
import java.util.function.Supplier;

public class ManaweavedSteelArmorItem extends ManasteelArmorItem {
    private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[]{new ItemStack(MegaBotanyItems.MANAWEAVEDSTEEL_HELMET.get()), new ItemStack(MegaBotanyItems.MANAWEAVEDSTEEL_CHESTPLATE.get()), new ItemStack(MegaBotanyItems.MANAWEAVEDSTEEL_LEGGINGS.get()), new ItemStack(MegaBotanyItems.MANAWEAVEDSTEEL_BOOTS.get())});

    public ManaweavedSteelArmorItem(Type type, Properties props) {
        super(type, MegaBotanyArmorMaterials.MANAWEAVEDSTEEL, props);
    }

    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return "megabotany:textures/model/armor_manaweavedsteel.png";
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
                    case HEAD -> stack.is(MegaBotanyItems.MANAWEAVEDSTEEL_HELMET.get());
                    case CHEST -> stack.is(MegaBotanyItems.MANAWEAVEDSTEEL_CHESTPLATE.get());
                    case LEGS -> stack.is(MegaBotanyItems.MANAWEAVEDSTEEL_LEGGINGS.get());
                    case FEET -> stack.is(MegaBotanyItems.MANAWEAVEDSTEEL_BOOTS.get());
                    default -> false;
                };
            }
        }
    }

    @Override
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("armor.megabotany.manaweavedsteel.desc0").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botania.armorset.manaweave.desc1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public MutableComponent getArmorSetName() {
        return Component.translatable("armor.megabotany.manaweavedsteel.name");
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return stack.is(MegaBotanyItems.MANAWEAVEDSTEEL_BOOTS.get());
    }
}

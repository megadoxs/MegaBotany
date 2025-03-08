package io.github.megadoxs.megabotany.common.item.equipment.armor.manaweavedSteel;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.ManaProficiencyArmor;
import vazkii.botania.api.mana.ManaDiscountArmor;

public class ManaweavedSteelHelmetItem extends ManaweavedSteelArmorItem implements ManaDiscountArmor, ManaProficiencyArmor {
    public ManaweavedSteelHelmetItem(Type type, Properties props) {
        super(type, props);
    }

    @Override
    public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
        return hasArmorSet(player) ? 0.5F : 0F;
    }

    @Override
    public boolean shouldGiveProficiency(ItemStack stack, EquipmentSlot slot, Player player, ItemStack rod) {
        return hasArmorSet(player);
    }
}

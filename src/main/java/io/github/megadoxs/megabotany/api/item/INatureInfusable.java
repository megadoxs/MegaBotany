package io.github.megadoxs.megabotany.api.item;

import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.helper.ItemNBTHelper;

//TODO will be replace by pure / impure mana infusion instead
public interface INatureInfusable {
    String TAG_INFUSION_RATE = "infusion_rate";
    String TAG_INFUSED = "infused";

    default void infuse(ItemStack stack) {
        if (getMaxInfusionRate() == getInfusionRate(stack) - 1)
            ItemNBTHelper.setBoolean(stack, TAG_INFUSED, true);
        else
            ItemNBTHelper.setInt(stack, TAG_INFUSION_RATE, getMaxInfusionRate() + 1);
    }

    default boolean isInfused(ItemStack stack) {
        return ItemNBTHelper.getBoolean(stack, TAG_INFUSED, false);
    }

    default int getInfusionRate(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_INFUSION_RATE, 0);
    }

    int getMaxInfusionRate();
}

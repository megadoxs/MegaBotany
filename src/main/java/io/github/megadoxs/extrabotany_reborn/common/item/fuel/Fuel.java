package io.github.megadoxs.extrabotany_reborn.item.fuel;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class Fuel extends Item {

    private final int burnTime;

    public Fuel(Properties pProperties, int burnTime) {
        super(pProperties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTime;
    }

    public static class BurnTime {
        public static final int NIGHTMARE_FUEL = 2000; // 10 items
        public static final int SPIRIT_FUEL = 3200; // 16 items
    }
}

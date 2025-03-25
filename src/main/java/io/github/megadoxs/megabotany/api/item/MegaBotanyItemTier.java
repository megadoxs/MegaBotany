package io.github.megadoxs.megabotany.api.item;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum MegaBotanyItemTier implements Tier {
    SHADOWIUM(2800, 10, 5, 5, 30, MegaBotanyItems.SHADOWIUM_INGOT),
    PHOTONIUM(2800, 10, 5, 5, 30, MegaBotanyItems.PHOTONIUM_INGOT),
    RELIC(2800, 10, 5, 5, 30, ItemStack.EMPTY::getItem),;

    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int harvestLevel;
    private final int enchantability;
    private final Supplier<Item> repairItem;

    MegaBotanyItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Item> repairItem) {
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
        this.repairItem = repairItem;
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(repairItem.get());
    }
}

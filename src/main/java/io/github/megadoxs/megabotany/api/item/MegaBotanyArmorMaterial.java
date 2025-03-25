package io.github.megadoxs.megabotany.api.item;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public enum MegaBotanyArmorMaterial implements ArmorMaterial {
    MANAWEAVEDSTEEL("manaweavedsteel", 20, Map.of(ArmorItem.Type.BOOTS, 2, ArmorItem.Type.LEGGINGS, 5, ArmorItem.Type.CHESTPLATE, 6, ArmorItem.Type.HELMET, 2), 30, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(ItemStack.EMPTY), 1, 0), //TODO set a repair ingredient
    SHADOWIUM("shadowium", 40, Map.of(ArmorItem.Type.BOOTS, 4, ArmorItem.Type.LEGGINGS, 7, ArmorItem.Type.CHESTPLATE, 10, ArmorItem.Type.HELMET, 4), 26, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MegaBotanyItems.SHADOWIUM_INGOT.get()), 5, 0.2f),
    PHOTONIUM("photonium", 40, Map.of(ArmorItem.Type.BOOTS, 4, ArmorItem.Type.LEGGINGS, 7, ArmorItem.Type.CHESTPLATE, 10, ArmorItem.Type.HELMET, 4), 26, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MegaBotanyItems.PHOTONIUM_INGOT.get()), 5, 0.2f),
    ORICHALCOS("orichalcos", 50, Map.of(ArmorItem.Type.BOOTS, 5, ArmorItem.Type.LEGGINGS, 8, ArmorItem.Type.CHESTPLATE, 12, ArmorItem.Type.HELMET, 5), 30, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MegaBotanyItems.ORICHALCOS_INGOT.get()), 8, 0.4f);
    private final String name;
    private final int durabilityMultiplier;
    private final Map<ArmorItem.Type, Integer> damageReduction; //subject to change
    private final int enchantability;
    private final SoundEvent equipSound;
    private final Supplier<Ingredient> repairItem;
    private final float toughness;
    private final float knockbackResistance;

    MegaBotanyArmorMaterial(String name, int durabilityMultiplier, Map<ArmorItem.Type, Integer> damageReduction, int enchantability, SoundEvent equipSound, Supplier<Ingredient> repairItem, float toughness, float knockbackResistance) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.damageReduction = damageReduction;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.repairItem = repairItem;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    public int getDurabilityForType(ArmorItem.Type slot) {
        int var10000 = switch (slot) {
            case BOOTS -> 13;
            case LEGGINGS -> 15;
            case CHESTPLATE -> 16;
            case HELMET -> 11;
        };

        return this.durabilityMultiplier * var10000;
    }

    public int getDefenseForType(@NotNull ArmorItem.Type slot) {
        return this.damageReduction.get(slot);
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public @NotNull SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return repairItem.get();
    }

    public @NotNull String getName() {
        return MegaBotany.MOD_ID + ":" + this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}

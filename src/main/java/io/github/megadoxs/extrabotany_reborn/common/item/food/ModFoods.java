package io.github.megadoxs.extrabotany_reborn.common.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties SPIRIT_FUEL = new FoodProperties.Builder().alwaysEat().fast()
            .effect( () -> new MobEffectInstance(MobEffects.NIGHT_VISION, 500, 1), 1f)
            .effect( () -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 1), 1f)
            .effect( () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 1), 1f)
            .effect( () -> new MobEffectInstance(MobEffects.LUCK, 500, 1), 1f)
            .build();

    public static final FoodProperties NIGHTMARE_FUEL = new FoodProperties.Builder().alwaysEat().fast()
            .effect( () -> new MobEffectInstance(MobEffects.BLINDNESS, 500, 1), 1f)
            .effect( () -> new MobEffectInstance(MobEffects.WEAKNESS, 500, 1), 1f)
            .effect( () -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 500, 1), 1f)
            .effect( () -> new MobEffectInstance(MobEffects.UNLUCK, 500, 1), 1f)
            .build();

    // not sure about saturation modifiers
    public static final FoodProperties GILDED_MASHED_POTATO = new FoodProperties.Builder().nutrition(3).saturationMod(12f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 3), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 3), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 1), 1f)
            .build();

    public static final FoodProperties FRIED_CHICKEN = new FoodProperties.Builder().nutrition(8).saturationMod(6f).meat().build();

}

package io.github.megadoxs.extrabotany_reborn.common.item.food;

import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import io.github.megadoxs.extrabotany_reborn.item.fuel.Fuel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class NightmareFuel extends Fuel {
    public NightmareFuel(Properties pProperties, int burnTime) {
        super(pProperties, burnTime);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, LivingEntity living) {
        super.finishUsingItem(stack, level, living);
        living.hurt(level.damageSources().magic(),6);

        ItemStack item = new ItemStack(ModItems.SPIRIT_FUEL.get());

        if (living instanceof Player player)
            if (!player.getInventory().add(item))
                player.drop(item, false);

        return stack;
    }
}

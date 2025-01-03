package io.github.megadoxs.megabotany.common.item.equipment.tool.hammer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.CustomDamageItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ManasteelHammer extends HammerItem implements CustomDamageItem {

    private static final int manaPerDamage = 60;

    public ManasteelHammer(Properties pProperties) {
        super(BotaniaAPI.instance().getManasteelItemTier(), 6, 0.8, pProperties);
    }

    public ManasteelHammer(Tier tier, Properties pProperties) {
        super(tier, 6, 0.8, pProperties);
    }

    public ManasteelHammer(Tier tier, double pAttackDamageModifier, double pAttackSpeedModifier, Properties pProperties) {
        super(tier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public int getManaPerDamage() {
        return manaPerDamage;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, getManaPerDamage());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean pIsSelected) {
        if (!world.isClientSide && entity instanceof Player player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, getManaPerDamage() * 2, true)) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }
}

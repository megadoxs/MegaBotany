package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import io.github.megadoxs.extrabotany_reborn.api.item.INatureInfusable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

import java.util.List;

public class NatureOrb extends BaubleItem implements CustomCreativeTabContents, INatureInfusable {

    private static final String TAG_NATURAL_BREATH = "natural_breath";
    private static final int MAX_NATURAL_BREATH = 500000;

    public NatureOrb(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        super.onWornTick(stack, entity);
        if (entity instanceof Player player) {

            ManaItemHandler.instance().dispatchManaExact(stack, player, (int) Math.floor((double) getNaturalBreath(stack) / 100000), true);

            if (getNaturalBreath(stack) > 300000) {
                ManaItemHandler.instance().dispatchManaExact(stack, player, 1, true);
                if (player.tickCount % 60 == 0)
                    player.heal(1F);
            }
            if (getNaturalBreath(stack) > 400000) {
                if (player.tickCount % 40 == 0) {
                    clearEffects(player);
                }
            }
            if (player.tickCount % 40 == 0) {
                addNaturalBreath(stack, -10);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltip, flags);
        tooltip.add(Component.translatable("misc.extrabotany_reborn.natural_breath").withStyle(ChatFormatting.GRAY).append(": " + getNaturalBreath(stack) + "/" + MAX_NATURAL_BREATH));
        tooltip.add(Component.translatable("misc.extrabotany_reborn.nature_orb_effect1").withStyle(ChatFormatting.GRAY).append(": ").append(Component.translatable(getNaturalBreath(stack) < 10000 ? "misc.extrabotany_reborn.deactivated" : "misc.extrabotany_reborn.activated").withStyle(ChatFormatting.AQUA)));
        tooltip.add(Component.translatable("misc.extrabotany_reborn.nature_orb_effect2").withStyle(ChatFormatting.GRAY).append(": ").append(Component.translatable(getNaturalBreath(stack) < 30000 ? "misc.extrabotany_reborn.deactivated" : "misc.extrabotany_reborn.activated").withStyle(ChatFormatting.RED)));
        tooltip.add(Component.translatable("misc.extrabotany_reborn.nature_orb_effect3").withStyle(ChatFormatting.GRAY).append(": ").append(Component.translatable(getNaturalBreath(stack) < 40000 ? "misc.extrabotany_reborn.deactivated" : "misc.extrabotany_reborn.activated").withStyle(ChatFormatting.GREEN)));
    }

    public void setNaturalBreath(ItemStack stack, int natural_breath) {
        ItemNBTHelper.setInt(stack, TAG_NATURAL_BREATH, natural_breath);
    }

    public void addNaturalBreath(ItemStack stack, int natural_breath) {
        setNaturalBreath(stack, Math.min(Math.max(getNaturalBreath(stack) + natural_breath, 0), MAX_NATURAL_BREATH));
    }

    public int getNaturalBreath(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_NATURAL_BREATH, 0);
    }

    public static void clearEffects(Player player) {
        List<MobEffect> effects = player.getActiveEffects().stream()
                .filter(effect -> !effect.getEffect().isBeneficial())
                .filter(effect -> effect.isCurativeItem(new ItemStack(Items.MILK_BUCKET)))
                .map(MobEffectInstance::getEffect)
                .distinct()
                .toList();

        effects.forEach(player::removeEffect);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round(13.0F * (float) getNaturalBreath(pStack) / (float) MAX_NATURAL_BREATH);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return Mth.hsvToRgb(MAX_NATURAL_BREATH / (float) getNaturalBreath(pStack) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void addToCreativeTab(Item item, CreativeModeTab.Output output) {
        ItemStack stack = new ItemStack(this);
        output.accept(stack);

        ItemStack stack2 = new ItemStack(this);
        ItemNBTHelper.setInt(stack2, TAG_NATURAL_BREATH, MAX_NATURAL_BREATH);
        output.accept(stack2);
    }

    @Override
    public int getMaxInfusionRate() {
        return 500000;
    }
}

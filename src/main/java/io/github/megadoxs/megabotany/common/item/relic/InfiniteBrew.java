package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.api.item.InfiniteBrewItem;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewContainer;
import vazkii.botania.api.brew.BrewItem;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class InfiniteBrew extends Item implements BrewItem, BrewContainer, InfiniteBrewItem, CustomCreativeTabContents {

    public static final int DEFAULT_MANA_COST = 250;
    private static final String TAG_BREW_KEY = "brewKey";
    private static final String TAG_USES = "uses";
    private static final String TAG_CHARGE = "charge";
    private static final String TAG_DAY = "day";

    public InfiniteBrew(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;//16?
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        var relic = XplatAbstractions.INSTANCE.findRelic(player.getItemInHand(hand));
        if (ItemNBTHelper.getFloat(player.getItemInHand(hand), TAG_CHARGE, 1) >= 1 && relic != null && relic.isRightPlayer(player))
            return ItemUtils.startUsingInstantly(world, player, hand);
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity living) {
        if (!level.isClientSide) {
            for (MobEffectInstance effect : getBrew(stack).getPotionEffects(stack)) {
                MobEffectInstance newEffect = new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), true, true);
                if (effect.getEffect().isInstantenous()) {
                    effect.getEffect().applyInstantenousEffect(living, living, living, newEffect.getAmplifier(), 1F);
                } else {
                    living.addEffect(newEffect);
                }
            }

            if (level.random.nextBoolean()) {
                level.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1F, 1F);
            }

            if (living instanceof Player player && !player.getAbilities().instabuild) {
                ItemNBTHelper.setFloat(stack, TAG_CHARGE, 0);
                ItemNBTHelper.setInt(stack, TAG_USES, ItemNBTHelper.getInt(stack, TAG_USES, 0) + 1);
            }
        }

        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        if (!level.isClientSide) {
            if (entity instanceof Player player) {
                if (ItemNBTHelper.getFloat(stack, TAG_CHARGE, 1) < 1 && entity.tickCount % 20 == 0) {
                    int manaCost = (int) (DEFAULT_MANA_COST * Math.pow(2, ItemNBTHelper.getInt(stack, TAG_USES, 0) - 1) / 10);
                    if (ManaItemHandler.instance().requestManaExactForTool(stack, player, manaCost, false)) {
                        ManaItemHandler.instance().requestManaExactForTool(stack, player, manaCost, true);
                        ItemNBTHelper.setFloat(stack, TAG_CHARGE, ItemNBTHelper.getFloat(stack, TAG_CHARGE, 0) + 0.1f);
                    }
                }

                var relic = XplatAbstractions.INSTANCE.findRelic(stack);
                if (relic != null) {
                    relic.tickBinding(player);
                }
            }

            int day = (int) Math.floorDiv(level.getDayTime(), 24000);
            if (day != ItemNBTHelper.getInt(stack, TAG_DAY, 0))
                ItemNBTHelper.setInt(stack, TAG_USES, 0);
            ItemNBTHelper.setInt(stack, TAG_DAY, day);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flags) {
        RelicImpl.addDefaultTooltip(stack, list);
        BaseBrewItem.addPotionTooltip(getBrew(stack).getPotionEffects(stack), list, 1);
    }

    @Override
    public Brew getBrew(ItemStack stack) {
        String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
        return BotaniaAPI.instance().getBrewRegistry().get(ResourceLocation.tryParse(key));
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
        ItemStack brewStack = new ItemStack(MegaBotanyItems.INFINITE_SPLASH_BREW.get());
        BaseBrewItem.setBrew(brewStack, brew);

        if (stack.getTag() != null && stack.getTag().contains("soulbindUUID")) {
            CompoundTag tag = new CompoundTag();
            tag.put("soulbindUUID", stack.getTag().get("soulbindUUID"));
            brewStack.setTag(tag);
        }

        return brewStack;
    }

    @Override
    public int getManaCost(Brew brew, ItemStack stack) {
        return 10000; //TODO MADE UP VALUE TO CHANGE
    }

    @Override
    public float getCharge(ItemStack stack) {
        return ItemNBTHelper.getFloat(stack, TAG_CHARGE, 1);
    }

    @Override
    public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
        for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
            if (brew == BotaniaBrews.fallbackBrew) {
                continue;
            }
            ItemStack stack = new ItemStack(this);
            ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
            output.accept(stack);

        }
    }
}

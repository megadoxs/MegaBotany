package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.api.item.InfiniteBrewItem;
import io.github.megadoxs.megabotany.common.entity.ThrownBrew;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewContainer;
import vazkii.botania.api.brew.BrewItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class InfiniteSplashBrew extends Item implements BrewItem, BrewContainer, InfiniteBrewItem, CustomCreativeTabContents {
    public static final int DEFAULT_MANA_COST = 250;
    private static final String TAG_BREW_KEY = "brewKey";
    private static final String TAG_USES = "uses";
    private static final String TAG_CHARGE = "charge";
    private static final String TAG_DAY = "day";

    public InfiniteSplashBrew(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Brew getBrew(ItemStack brew) {
        String key = ItemNBTHelper.getString(brew, TAG_BREW_KEY, "");
        return BotaniaAPI.instance().getBrewRegistry().get(ResourceLocation.tryParse(key));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        var relic = XplatAbstractions.INSTANCE.findRelic(itemstack);
        if (ItemNBTHelper.getFloat(itemstack, TAG_CHARGE, 1) == 1 && relic != null && relic.isRightPlayer(pPlayer)) {
            if (!pLevel.isClientSide) {
                ThrownBrew thrownBrew = new ThrownBrew(pLevel, pPlayer);
                thrownBrew.setItem(itemstack);
                thrownBrew.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), -20.0F, 0.5F, 1.0F);
                pLevel.addFreshEntity(thrownBrew);
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            if (!pPlayer.getAbilities().instabuild) {
                ItemNBTHelper.setFloat(itemstack, TAG_CHARGE, 0);
                ItemNBTHelper.setInt(itemstack, TAG_USES, ItemNBTHelper.getInt(itemstack, TAG_USES, 0) + 1);
            }

            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        }
        return InteractionResultHolder.pass(itemstack);
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
    public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
        ItemStack brewStack = new ItemStack(MegaBotanyItems.INFINITE_LINGERING_BREW.get());
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
        return 10000;
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
            BaseBrewItem.setBrew(stack, brew);
            output.accept(stack);
        }
    }
}

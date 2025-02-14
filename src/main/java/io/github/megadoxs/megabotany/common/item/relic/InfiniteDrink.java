package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewContainer;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class InfiniteDrink extends Item implements BrewContainer {
    public InfiniteDrink(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
        ItemStack brewStack = new ItemStack(MegaBotanyItems.INFINITE_BREW.get());
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
        return brew.getManaCost() * 5;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }
        }
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, new ResourceLocation(MegaBotany.MOD_ID, "main/" + MegaBotanyItems.INFINITE_DRINK.getId().getPath()));
    }
}

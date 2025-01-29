package io.github.megadoxs.megabotany.common.item.relic;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.item.equipment.bauble.ElvenKingRing;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class AFORing extends ElvenKingRing {

    public AFORing(Properties props) {
        super(props);
    }

    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null)
                relic.tickBinding(player);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        super.onWornTick(stack, entity);
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);
        if (relic != null && entity instanceof Player ePlayer)
            relic.tickBinding(ePlayer);
    }

    @Override
    public boolean canEquip(ItemStack stack, LivingEntity entity) {
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);
        return entity instanceof Player player && relic != null && relic.isRightPlayer(player) && super.canEquip(stack, entity);
    }

    @Override
    public int getSize() {
        return 24;
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, new ResourceLocation(MegaBotany.MOD_ID, "main/" + MegaBotanyItems.ALL_FOR_ONE.getId().getPath()));
    }
}

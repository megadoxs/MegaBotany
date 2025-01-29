package io.github.megadoxs.megabotany.common.util;

import io.github.megadoxs.megabotany.api.item.InfiniteBrewItem;
import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import vazkii.botania.network.TriConsumer;

public class MegaBotanyItemProperties {
    public static void addItemProperties(TriConsumer<ItemLike, ResourceLocation, ClampedItemPropertyFunction> consumer) {
        // [VanillaCopy] ItemProperties.BOW's minecraft:pulling property
        ClampedItemPropertyFunction pulling = (stack, worldIn, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        ClampedItemPropertyFunction pull = (stack, worldIn, entity, seed) -> {
            if (entity == null)
                return 0.0F;
            else
                return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
        };
        consumer.accept(MegaBotanyItems.FAILNAUGHT.get(), new ResourceLocation("pulling"), pulling);
        consumer.accept(MegaBotanyItems.FAILNAUGHT.get(), new ResourceLocation("pull"), pull);

        ClampedItemPropertyFunction chargeGetter = (stack, world, entity, seed) -> {
            InfiniteBrewItem item = ((InfiniteBrewItem) stack.getItem());
            return item.getCharge(stack);
        };
        consumer.accept(MegaBotanyItems.INFINITE_BREW.get(), new ResourceLocation(MegaBotany.MOD_ID, "charge"), chargeGetter);
        consumer.accept(MegaBotanyItems.INFINITE_SPLASH_BREW.get(), new ResourceLocation(MegaBotany.MOD_ID, "charge"), chargeGetter);
        consumer.accept(MegaBotanyItems.INFINITE_LINGERING_BREW.get(), new ResourceLocation(MegaBotany.MOD_ID, "charge"), chargeGetter);
    }
}

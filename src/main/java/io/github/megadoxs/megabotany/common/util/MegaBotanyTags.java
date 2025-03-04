package io.github.megadoxs.megabotany.common.util;

import io.github.megadoxs.megabotany.common.MegaBotany;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MegaBotanyTags {
    public static class Blocks {
        public static final TagKey<Block> GAIA_ILLEGAL_BLOCKS = tag("gaia_illegal_blocks");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(MegaBotany.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> HAMMERS = tag("hammers");
        public static final TagKey<Item> MORTAR_INGREDIENTS = tag("mortar_ingredients");


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MegaBotany.MOD_ID, name));
        }
    }
}

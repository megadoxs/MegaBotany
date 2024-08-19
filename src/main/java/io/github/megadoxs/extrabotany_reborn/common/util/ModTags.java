package io.github.megadoxs.extrabotany_reborn.common.util;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{


        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(ExtraBotany_Reborn.MOD_ID, name));
        }
    }

    public static class Items{
        public static final TagKey<Item> HAMMERS = tag("hammers");
        public static final TagKey<Item> MORTAR_INGREDIENTS = tag("mortar_ingredients");


        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(ExtraBotany_Reborn.MOD_ID, name));
        }
    }
}

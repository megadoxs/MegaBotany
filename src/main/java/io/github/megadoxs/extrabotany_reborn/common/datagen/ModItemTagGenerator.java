package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import io.github.megadoxs.extrabotany_reborn.common.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ExtraBotany_Reborn.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BotaniaTags.Items.SPECIAL_FLOWERS)
                .add(ModBlocks.BLOODY_ENCHANTRESS.get().asItem());

        this.tag(ModTags.Items.HAMMERS).add(
                ModItems.MANASTEEL_HAMMER.get(),
                ModItems.ELEMENTIUM_HAMMER.get(),
                ModItems.TERRASTEEL_HAMMER.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "ring"))).add(
                ModItems.MASTER_BAND_OF_MANA.get(),
                ModItems.FROST_RING.get(),
                ModItems.CURSE_RING.get(),
                ModItems.MANA_DRIVE_RING.get(),
                ModItems.ELVEN_KING_RING.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "necklace"))).add(
                ModItems.WALL_JUMP_AMULET.get(),
                ModItems.WALL_CLIMB_AMULET.get(),
                ModItems.PARKOUR_AMULET.get(),
                ModItems.PARKOUR_AMULET2.get(),
                ModItems.PURE_DAISY_PENDANT.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "charm"))).add(
                ModItems.NATURE_ORB.get(),
                ModItems.JINGWEI_FEATHER.get(),
                ModItems.BOTTLED_FLAME.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "head"))).add(
                ModItems.GOD_CORE.get(),
                ModItems.SUPER_CROWN.get()
        );
    }
}

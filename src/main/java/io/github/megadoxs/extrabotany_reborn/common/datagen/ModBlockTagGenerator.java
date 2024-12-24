package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ExtraBotany_Reborn.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantress,
                MegaBotanyFlowerBlocks.SunshineLily
        );

        this.tag(BotaniaTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantressFloating,
                MegaBotanyFlowerBlocks.SunshineLilyFloating
        );

        this.tag(BlockTags.FLOWER_POTS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantressPotted,
                MegaBotanyFlowerBlocks.SunshineLilyPotted
        );

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                MegaBotanyFlowerBlocks.bloodyEnchantressFloating,
                MegaBotanyFlowerBlocks.SunshineLilyFloating
        );


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.PHOTONIUM_BLOCK.get(),
                ModBlocks.SHADOWIUM_BLOCK.get(),
                ModBlocks.ORICHALCOS_BLOCK.get(),
                ModBlocks.MORTAR.get()
        );

        this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                ModBlocks.PHOTONIUM_BLOCK.get(),
                ModBlocks.SHADOWIUM_BLOCK.get(),
                ModBlocks.ORICHALCOS_BLOCK.get()
        );
    }
}

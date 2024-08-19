package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ExtraBotany_Reborn.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BotaniaTags.Blocks.SPECIAL_FLOWERS).add(
                ModBlocks.BLOODY_ENCHANTRESS.get()
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

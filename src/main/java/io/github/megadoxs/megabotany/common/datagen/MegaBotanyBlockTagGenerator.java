package io.github.megadoxs.megabotany.common.datagen;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class MegaBotanyBlockTagGenerator extends BlockTagsProvider {
    public MegaBotanyBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MegaBotany.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantress,
                MegaBotanyFlowerBlocks.sunshineLily,
                MegaBotanyFlowerBlocks.moonlightLily,
                MegaBotanyFlowerBlocks.omniviolet,
                MegaBotanyFlowerBlocks.edelweissPotted,
                MegaBotanyFlowerBlocks.tinkle,
                MegaBotanyFlowerBlocks.bellFlower,
                MegaBotanyFlowerBlocks.reikarLily,
                MegaBotanyFlowerBlocks.stonesia,
                MegaBotanyFlowerBlocks.geminiOrchid
        );

        this.tag(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
                MegaBotanyFlowerBlocks.enchantedOrchid,
                MegaBotanyFlowerBlocks.mirrortunia,
                MegaBotanyFlowerBlocks.annoyingFlower,
                MegaBotanyFlowerBlocks.necrofleur,
                MegaBotanyFlowerBlocks.necrofleurChibi,
                MegaBotanyFlowerBlocks.stardustLotus,
                MegaBotanyFlowerBlocks.manalinkium
        );

        this.tag(BotaniaTags.Blocks.MINI_FLOWERS).add(
                MegaBotanyFlowerBlocks.necrofleurChibi
        );

        this.tag(BotaniaTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantressFloating,
                MegaBotanyFlowerBlocks.sunshineLilyFloating,
                MegaBotanyFlowerBlocks.moonlightLilyFloating,
                MegaBotanyFlowerBlocks.omnivioletFloating,
                MegaBotanyFlowerBlocks.edelweissFloating,
                MegaBotanyFlowerBlocks.tinkleFloating,
                MegaBotanyFlowerBlocks.bellFlowerFloating,
                MegaBotanyFlowerBlocks.reikarLilyFloating,
                MegaBotanyFlowerBlocks.stonesiaFloating,
                MegaBotanyFlowerBlocks.geminiOrchidFloating,
                MegaBotanyFlowerBlocks.enchantedOrchidFloating,
                MegaBotanyFlowerBlocks.mirrortuniaFloating,
                MegaBotanyFlowerBlocks.annoyingFlowerFloating,
                MegaBotanyFlowerBlocks.necrofleurFloating,
                MegaBotanyFlowerBlocks.necrofleurChibiFloating,
                MegaBotanyFlowerBlocks.stardustLotusFloating,
                MegaBotanyFlowerBlocks.manalinkiumFloating
        );

        this.tag(BlockTags.FLOWER_POTS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantressPotted,
                MegaBotanyFlowerBlocks.sunshineLilyPotted,
                MegaBotanyFlowerBlocks.moonlightLilyPotted,
                MegaBotanyFlowerBlocks.omnivioletPotted,
                MegaBotanyFlowerBlocks.edelweissPotted,
                MegaBotanyFlowerBlocks.tinklePotted,
                MegaBotanyFlowerBlocks.bellFlowerPotted,
                MegaBotanyFlowerBlocks.reikarLilyPotted,
                MegaBotanyFlowerBlocks.stonesiaPotted,
                MegaBotanyFlowerBlocks.geminiOrchidPotted,
                MegaBotanyFlowerBlocks.enchantedOrchidPotted,
                MegaBotanyFlowerBlocks.mirrortuniaPotted,
                MegaBotanyFlowerBlocks.annoyingFlowerPotted,
                MegaBotanyFlowerBlocks.necrofleurPotted,
                MegaBotanyFlowerBlocks.stardustLotusPotted,
                MegaBotanyFlowerBlocks.manalinkiumPotted
        );

        //TODO probably can replace with a link tot the special_floating_flowers
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                MegaBotanyFlowerBlocks.bloodyEnchantressFloating,
                MegaBotanyFlowerBlocks.sunshineLilyFloating,
                MegaBotanyFlowerBlocks.moonlightLilyFloating,
                MegaBotanyFlowerBlocks.omnivioletFloating,
                MegaBotanyFlowerBlocks.edelweissFloating,
                MegaBotanyFlowerBlocks.tinkleFloating,
                MegaBotanyFlowerBlocks.bellFlowerFloating,
                MegaBotanyFlowerBlocks.reikarLilyFloating,
                MegaBotanyFlowerBlocks.stonesiaFloating,
                MegaBotanyFlowerBlocks.geminiOrchidFloating,
                MegaBotanyFlowerBlocks.enchantedOrchidFloating,
                MegaBotanyFlowerBlocks.mirrortuniaFloating,
                MegaBotanyFlowerBlocks.annoyingFlowerFloating,
                MegaBotanyFlowerBlocks.necrofleurFloating,
                MegaBotanyFlowerBlocks.stardustLotusFloating,
                MegaBotanyFlowerBlocks.manalinkiumFloating
        );

        this.tag(BlockTags.DEAD_BUSH_MAY_PLACE_ON).add(
                BotaniaBlocks.enchantedSoil
        );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                MegaBotanyBlocks.PHOTONIUM_BLOCK.get(),
                MegaBotanyBlocks.SHADOWIUM_BLOCK.get(),
                MegaBotanyBlocks.ORICHALCOS_BLOCK.get(),
                MegaBotanyBlocks.MORTAR.get()
        );

        this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                MegaBotanyBlocks.PHOTONIUM_BLOCK.get(),
                MegaBotanyBlocks.SHADOWIUM_BLOCK.get(),
                MegaBotanyBlocks.ORICHALCOS_BLOCK.get()
        );
    }
}

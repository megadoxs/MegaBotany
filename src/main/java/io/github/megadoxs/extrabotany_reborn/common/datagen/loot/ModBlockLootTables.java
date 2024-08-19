package io.github.megadoxs.extrabotany_reborn.common.datagen.loot;

import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.PHOTONIUM_BLOCK.get());
        this.dropSelf(ModBlocks.SHADOWIUM_BLOCK.get());
        this.dropSelf(ModBlocks.ORICHALCOS_BLOCK.get());

        this.dropSelf(ModBlocks.BLOODY_ENCHANTRESS.get());

        this.dropSelf(ModBlocks.MORTAR.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
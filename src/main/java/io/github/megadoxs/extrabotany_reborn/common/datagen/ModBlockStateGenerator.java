package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateGenerator extends BlockStateProvider {
    public ModBlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExtraBotany_Reborn.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.PHOTONIUM_BLOCK);
        blockWithItem(ModBlocks.SHADOWIUM_BLOCK);
        blockWithItem(ModBlocks.ORICHALCOS_BLOCK);

        simpleBlockWithItem(ModBlocks.BLOODY_ENCHANTRESS.get(), models().cross(blockTexture(ModBlocks.BLOODY_ENCHANTRESS.get()).getPath(), blockTexture(ModBlocks.BLOODY_ENCHANTRESS.get())).renderType("cutout"));

        simpleBlock(ModBlocks.MORTAR.get(), new ModelFile.UncheckedModelFile(modLoc("block/mortar")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll((blockRegistryObject.get())));
    }
}

package io.github.megadoxs.megabotany.common.datagen;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;

public class MegaBotanyBlockStateGenerator extends BlockStateProvider {
    public MegaBotanyBlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MegaBotany.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(MegaBotanyBlocks.PHOTONIUM_BLOCK);
        blockWithItem(MegaBotanyBlocks.SHADOWIUM_BLOCK);
        blockWithItem(MegaBotanyBlocks.ORICHALCOS_BLOCK);

        noModelBlock(MegaBotanyBlocks.MORTAR);
        noModelBlock(MegaBotanyBlocks.PEDESTAL);
        noModelBlock(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER);
        noModelBlock(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER);

        //Item models are already registered in ModItemModelGenerator
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(id.getNamespace()) && b instanceof FlowerBlock) {
                simpleBlock(b, models().cross(blockTexture(b).getPath(), blockTexture(b)).renderType("cutout"));
            }
        }

        // Making all the floating & potted flowers Block states
        // The models are already done by FloatingFlowerModelGenerator & PottedFlowerModelGenerator
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(id.getNamespace()) && (b instanceof FloatingFlowerBlock || b instanceof FlowerPotBlock)) {
                simpleBlock(b, new ModelFile.UncheckedModelFile(modLoc("block/" + id.getPath())));
            }
        }


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll((blockRegistryObject.get())));
    }

    private void noModelBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MegaBotany.MOD_ID, "block/" + blockRegistryObject.getId().getPath())));
    }
}

package io.github.megadoxs.extrabotany_reborn.common.datagen;

import com.google.gson.JsonObject;
import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.xplat.ClientXplatAbstractions;

public class ModBlockStateGenerator extends BlockStateProvider {
    public ModBlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExtraBotany_Reborn.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.PHOTONIUM_BLOCK);
        blockWithItem(ModBlocks.SHADOWIUM_BLOCK);
        blockWithItem(ModBlocks.ORICHALCOS_BLOCK);

        simpleBlockWithItem(MegaBotanyFlowerBlocks.bloodyEnchantress, models().cross(blockTexture(MegaBotanyFlowerBlocks.bloodyEnchantress).getPath(), blockTexture(MegaBotanyFlowerBlocks.bloodyEnchantress)).renderType("cutout"));
        simpleBlockWithItem(MegaBotanyFlowerBlocks.SunshineLily, models().cross(blockTexture(MegaBotanyFlowerBlocks.SunshineLily).getPath(), blockTexture(MegaBotanyFlowerBlocks.SunshineLily)).renderType("cutout"));


        simpleBlock(ModBlocks.MORTAR.get(), new ModelFile.UncheckedModelFile(modLoc("block/mortar")));

        // Making all the floating & potted flowers Block states
        // The models are already done by FloatingFlowerModelGenerator & PottedFlowerModelGenerator
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (ExtraBotany_Reborn.MOD_ID.equals(id.getNamespace()) && (b instanceof FloatingFlowerBlock || b instanceof FlowerPotBlock)) {
                simpleBlock(b, new ModelFile.UncheckedModelFile(modLoc("block/" + id.getPath())));
            }
        }
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll((blockRegistryObject.get())));
    }
}

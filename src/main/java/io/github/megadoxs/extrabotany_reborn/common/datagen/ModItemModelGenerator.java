package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelGenerator extends ItemModelProvider {
    public ModItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraBotany_Reborn.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SPIRIT_FRAGMENT);
        simpleItem(ModItems.SPIRIT_FUEL);
        simpleItem(ModItems.NIGHTMARE_FUEL);
        simpleItem(ModItems.GILDED_POTATO);
        simpleItem(ModItems.GILDED_MASHED_POTATO);
        simpleItem(ModItems.NATURE_ORB);
        simpleItem(ModItems.MASTER_BAND_OF_MANA);

        simpleItem(ModItems.MANASTEEL_HAMMER);
        simpleItem(ModItems.ELEMENTIUM_HAMMER);
        simpleItem(ModItems.TERRASTEEL_HAMMER);

        simpleItem(ModItems.PHOTONIUM_INGOT);
        simpleItem(ModItems.SHADOWIUM_INGOT);
        simpleItem(ModItems.ORICHALCOS_INGOT);
        simpleItem(ModItems.GOD_CORE);

        simpleBlockItemBlockTexture(ModBlocks.BLOODY_ENCHANTRESS);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(ExtraBotany_Reborn.MOD_ID, "item/" + item.getId().getPath()));
    }

//    private ItemModelBuilder shieldItem(RegistryObject<Item> item){
//        return withExistingParent(item.getId().getPath(), new ResourceLocation())
//    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExtraBotany_Reborn.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExtraBotany_Reborn.MOD_ID,"block/" + item.getId().getPath()));
    }
}

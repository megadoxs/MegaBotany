package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MegaBotanyItemModelGenerator extends ItemModelProvider {
    public MegaBotanyItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MegaBotany.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(MegaBotanyItems.SPIRIT_FUEL);
        simpleItem(MegaBotanyItems.NIGHTMARE_FUEL);
        simpleItem(MegaBotanyItems.NATURE_ORB);
        simpleItem(MegaBotanyItems.MASTER_BAND_OF_MANA);
        simpleItem(MegaBotanyItems.FROST_RING);
        simpleItem(MegaBotanyItems.WALL_JUMP_AMULET);
        simpleItem(MegaBotanyItems.WALL_CLIMB_AMULET);
        simpleItem(MegaBotanyItems.MANA_DRIVE_RING);
        simpleItem(MegaBotanyItems.JINGWEI_FEATHER);
        simpleItem(MegaBotanyItems.PURE_DAISY_PENDANT);
        simpleItem(MegaBotanyItems.SUPER_CROWN);
        simpleItem(MegaBotanyItems.BOTTLED_FLAME);
        simpleItem(MegaBotanyItems.EARTH_ESSENCE);
        simpleItem(MegaBotanyItems.MEDAL_OF_HEROISM);

        handheldItem(MegaBotanyItems.WALKING_CANE);

        handheldItem(MegaBotanyItems.EXCALIBUR);
        simpleItem(MegaBotanyItems.ALL_FOR_ONE);
        simpleItem(MegaBotanyItems.INFINITE_DRINK);

        brewItem(MegaBotanyItems.INFINITE_BREW.getId().getPath(), MegaBotanyItems.INFINITE_DRINK.getKey().location(), MegaBotanyItems.INFINITE_BREW.getId());
        brewItem(MegaBotanyItems.INFINITE_SPLASH_BREW.getId().getPath(), MegaBotanyItems.INFINITE_SPLASH_BREW.getId(), new ResourceLocation(MegaBotanyItems.INFINITE_SPLASH_BREW.getId() + "_brew"));
        brewItem(MegaBotanyItems.INFINITE_LINGERING_BREW.getId().getPath(), MegaBotanyItems.INFINITE_LINGERING_BREW.getId(), new ResourceLocation(MegaBotanyItems.INFINITE_SPLASH_BREW.getId() + "_brew"));

        simpleItem(MegaBotanyItems.PANDORA_BOX);

        simpleItem(MegaBotanyItems.PHOTONIUM_INGOT);
        simpleItem(MegaBotanyItems.SHADOWIUM_INGOT);
        simpleItem(MegaBotanyItems.ORICHALCOS_INGOT);
        simpleItem(MegaBotanyItems.TICKET);
        simpleItem(MegaBotanyItems.GOD_CORE);

        simpleItem(MegaBotanyItems.SMELTING_LENS);
        simpleItem(MegaBotanyItems.MANA_LENS);

        simpleItem(MegaBotanyItems.MANAWEAVEDSTEEL_HELMET);
        simpleItem(MegaBotanyItems.MANAWEAVEDSTEEL_CHESTPLATE);
        simpleItem(MegaBotanyItems.MANAWEAVEDSTEEL_LEGGINGS);
        simpleItem(MegaBotanyItems.MANAWEAVEDSTEEL_BOOTS);
        simpleItem(MegaBotanyItems.SHORT_PHOTONIUM_SWORD);

        simpleItem(MegaBotanyItems.SHADOWIUM_HELMET);
        simpleItem(MegaBotanyItems.SHADOWIUM_CHESTPLATE);
        simpleItem(MegaBotanyItems.SHADOWIUM_LEGGINGS);
        simpleItem(MegaBotanyItems.SHADOWIUM_BOOTS);
        simpleItem(MegaBotanyItems.SHADOWIUM_KATANA);

        simpleItem(MegaBotanyItems.PHOTONIUM_HELMET);
        simpleItem(MegaBotanyItems.PHOTONIUM_CHESTPLATE);
        simpleItem(MegaBotanyItems.PHOTONIUM_LEGGINGS);
        simpleItem(MegaBotanyItems.PHOTONIUM_BOOTS);

        simpleItem(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE);
        simpleItem(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE);
        simpleItem(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE);
        simpleItem(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE);

        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(id.getNamespace()) && b instanceof FlowerBlock) {
                simpleBlockItemBlockTexture(b);
            }
        }
    }

    private void brewItem(String item, ResourceLocation container, ResourceLocation brew) {
        ItemModelBuilder itemModelBuilder = withExistingParent(item, new ResourceLocation("item/generated"));
        float increment = 0.1f;

        itemModelBuilder.texture("layer0", new ResourceLocation(container.getNamespace(), "item/" + container.getPath()));

        for (float i = increment; (int) (i * 10) / 10f <= 1; i += increment) {
            String modelPath = item + "_" + (int) (i / increment);
            ItemModelBuilder model = withExistingParent(modelPath, new ResourceLocation("item/generated"));
            model.texture("layer0", new ResourceLocation(container.getNamespace(), "item/" + container.getPath()));
            model.texture("layer1", new ResourceLocation(brew.getNamespace(), "item/" + brew.getPath() + "_" + Math.round(i / increment)));
            itemModelBuilder.override().predicate(new ResourceLocation(MegaBotany.MOD_ID, "charge"), (int) (i * 10) / 10f).model(new ModelFile.ExistingModelFile(new ResourceLocation(MegaBotany.MOD_ID, "item/" + modelPath), existingFileHelper));
        }
    }

    private void simpleItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void handheldItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld")).texture("layer0", new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void simpleBlockItemBlockTexture(Block item) {
        withExistingParent(ForgeRegistries.BLOCKS.getKey(item).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MegaBotany.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(item).getPath()));
    }
}

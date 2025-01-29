package io.github.megadoxs.megabotany.common.datagen;

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
        simpleItem(MegaBotanyItems.SPIRIT_FRAGMENT);
        simpleItem(MegaBotanyItems.SPIRIT_FUEL);
        simpleItem(MegaBotanyItems.NIGHTMARE_FUEL);
        simpleItem(MegaBotanyItems.GILDED_POTATO);
        simpleItem(MegaBotanyItems.GILDED_MASHED_POTATO);
        simpleItem(MegaBotanyItems.FRIED_CHICKEN);
        simpleItem(MegaBotanyItems.NATURE_ORB);
        simpleItem(MegaBotanyItems.MASTER_BAND_OF_MANA);
        simpleItem(MegaBotanyItems.FROST_RING);
        simpleItem(MegaBotanyItems.CURSE_RING);
        simpleItem(MegaBotanyItems.WALL_JUMP_AMULET);
        simpleItem(MegaBotanyItems.WALL_CLIMB_AMULET);
        simpleItem(MegaBotanyItems.PARKOUR_AMULET);
        simpleItem(MegaBotanyItems.PARKOUR_AMULET2);
        simpleItem(MegaBotanyItems.MANA_DRIVE_RING);
        simpleItem(MegaBotanyItems.ELVEN_KING_RING);
        simpleItem(MegaBotanyItems.JINGWEI_FEATHER);
        simpleItem(MegaBotanyItems.PURE_DAISY_PENDANT);
        simpleItem(MegaBotanyItems.SUPER_CROWN);
        simpleItem(MegaBotanyItems.BOTTLED_FLAME);

        handheldItem(MegaBotanyItems.MANASTEEL_HAMMER);
        handheldItem(MegaBotanyItems.ELEMENTIUM_HAMMER);
        handheldItem(MegaBotanyItems.TERRASTEEL_HAMMER);
        handheldItem(MegaBotanyItems.WALKING_CANE);

        handheldItem(MegaBotanyItems.EXCALIBER);
        simpleItem(MegaBotanyItems.ALL_FOR_ONE);
        simpleItem(MegaBotanyItems.INFINITE_DRINK);
        potionItem(MegaBotanyItems.INFINITE_BREW.getId().getPath(), new ResourceLocation(MegaBotany.MOD_ID, "charge"), 0, 1, 0.1f,  MegaBotanyItems.INFINITE_DRINK.getId().getPath(), MegaBotanyItems.INFINITE_BREW.getId().getPath());
        simpleItem(MegaBotanyItems.INFINITE_SPLASH_BREW);
        simpleItem(MegaBotanyItems.PANDORA_BOX);

        simpleItem(MegaBotanyItems.PHOTONIUM_INGOT);
        simpleItem(MegaBotanyItems.SHADOWIUM_INGOT);
        simpleItem(MegaBotanyItems.ORICHALCOS_INGOT);
        simpleItem(MegaBotanyItems.TICKET);
        simpleItem(MegaBotanyItems.GOD_CORE);

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

    private ItemModelBuilder potionItem(String item, ResourceLocation predicate, float min, float max, float increment, String... textures) {
        ItemModelBuilder itemModelBuilder = withExistingParent(item, new ResourceLocation("item/generated"));

        for (int i = 0; i < textures.length; i++) {
            itemModelBuilder.texture("layer" + i, new ResourceLocation(MegaBotany.MOD_ID, "item/" + textures[i]));
        }

        for (float i = min; Math.floor(i * 10.0)/10.0 < max; i += increment) {
            String modelPath = item + "_" + (int) (i/increment);
            ItemModelBuilder model =  withExistingParent(modelPath, new ResourceLocation("item/generated"));
            for (int j = 0; j < textures.length; j++) {
                if(j == textures.length -1)
                    model.texture("layer" + j, new ResourceLocation(MegaBotany.MOD_ID, "item/" + textures[j] + "_" + (int) ((Math.floor(i * 10.0)/10.0)/increment)));
                else
                    model.texture("layer" + j, new ResourceLocation(MegaBotany.MOD_ID, "item/" + textures[j]));
            }
            itemModelBuilder.override().predicate(predicate, (float) (Math.floor(i * 10.0)/10.0)).model(new ModelFile.ExistingModelFile(new ResourceLocation(MegaBotany.MOD_ID, "item/" + modelPath), existingFileHelper));
        }

        return itemModelBuilder;
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld")).texture("layer0", new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(Block item) {
        return withExistingParent(ForgeRegistries.BLOCKS.getKey(item).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MegaBotany.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(item).getPath()));
    }
}

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

        simpleItem(MegaBotanyItems.MANASTEEL_HAMMER);
        simpleItem(MegaBotanyItems.ELEMENTIUM_HAMMER);
        simpleItem(MegaBotanyItems.TERRASTEEL_HAMMER);

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

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(MegaBotany.MOD_ID, "item/" + item.getId().getPath()));
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

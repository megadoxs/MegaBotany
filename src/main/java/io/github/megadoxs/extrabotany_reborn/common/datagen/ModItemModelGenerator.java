package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
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
        simpleItem(ModItems.FROST_RING);
        simpleItem(ModItems.CURSE_RING);
        simpleItem(ModItems.WALL_JUMP_AMULET);
        simpleItem(ModItems.WALL_CLIMB_AMULET);
        simpleItem(ModItems.PARKOUR_AMULET);
        simpleItem(ModItems.PARKOUR_AMULET2);
        simpleItem(ModItems.MANA_DRIVE_RING);
        simpleItem(ModItems.ELVEN_KING_RING);
        simpleItem(ModItems.JINGWEI_FEATHER);
        simpleItem(ModItems.PURE_DAISY_PENDANT);
        simpleItem(ModItems.SUPER_CROWN);
        simpleItem(ModItems.BOTTLED_FLAME);

        simpleItem(ModItems.MANASTEEL_HAMMER);
        simpleItem(ModItems.ELEMENTIUM_HAMMER);
        simpleItem(ModItems.TERRASTEEL_HAMMER);

        simpleItem(ModItems.PHOTONIUM_INGOT);
        simpleItem(ModItems.SHADOWIUM_INGOT);
        simpleItem(ModItems.ORICHALCOS_INGOT);
        simpleItem(ModItems.TICKET);
        simpleItem(ModItems.GOD_CORE);

        simpleItem(ModItems.ORICHALCOS_HELMET_FEMALE);
        simpleItem(ModItems.ORICHALCOS_CHESTPLATE_FEMALE);
        simpleItem(ModItems.ORICHALCOS_LEGGINGS_FEMALE);
        simpleItem(ModItems.ORICHALCOS_BOOTS_FEMALE);

        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (ExtraBotany_Reborn.MOD_ID.equals(id.getNamespace()) && b instanceof FlowerBlock) {
                simpleBlockItemBlockTexture(b);
            }
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(ExtraBotany_Reborn.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExtraBotany_Reborn.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(Block item) {
        return withExistingParent(ForgeRegistries.BLOCKS.getKey(item).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExtraBotany_Reborn.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(item).getPath()));
    }
}

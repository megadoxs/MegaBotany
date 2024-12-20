package io.github.megadoxs.extrabotany_reborn.common.block;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.entity_block.BloodyEnchantressBlock;
import io.github.megadoxs.extrabotany_reborn.common.block.entity_block.MortarBlock;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExtraBotany_Reborn.MOD_ID);

    // an example of adding a normal flower
    public static final RegistryObject<Block> BLOODY_ENCHANTRESS = registerBlock("bloody_enchantress", () -> new BloodyEnchantressBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));

    public static final RegistryObject<Block> MORTAR = registerBlock("mortar", () -> new MortarBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> PHOTONIUM_BLOCK = registerBlock("photonium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> SHADOWIUM_BLOCK = registerBlock("shadowium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ORICHALCOS_BLOCK = registerBlock("orichalcos_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}

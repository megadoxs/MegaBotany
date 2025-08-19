package io.github.megadoxs.megabotany.common.block;

import io.github.megadoxs.megabotany.api.RedstoneSpreader;
import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.block_entity.PedestalBlock;
import io.github.megadoxs.megabotany.common.block.block_entity.SpiritPortalBlock;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;

import java.util.function.Supplier;

import static vazkii.botania.common.block.BotaniaBlocks.dreamwood;

public class MegaBotanyBlocks {
    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MegaBotany.MOD_ID);

    public static final RegistryObject<Block> SPIRIT_PORTAL = registerBlock("spirit_portal", () -> new SpiritPortalBlock(BlockBehaviour.Properties.copy(BotaniaBlocks.alfPortal)));
    public static final RegistryObject<Block> PEDESTAL = registerBlock("pedestal", () -> new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK)));

    public static final RegistryObject<Block> PHOTONIUM_BLOCK = registerBlock("photonium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> SHADOWIUM_BLOCK = registerBlock("shadowium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> ORICHALCOS_BLOCK = registerBlock("orichalcos_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)));

    public static final RegistryObject<Block> REDSTONE_ELVEN_SPREADER = registerBlock("redstone_elven_spreader", () -> manaSpreader(ManaSpreaderBlock.Variant.ELVEN));
    public static final RegistryObject<Block> REDSTONE_GAIA_SPREADER = registerBlock("redstone_gaia_spreader", () -> manaSpreader(ManaSpreaderBlock.Variant.GAIA));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return MegaBotanyItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static FlowerPotBlock flowerPot(Block block, int lightLevel) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        return new FlowerPotBlock(block, lightLevel > 0 ? properties.lightLevel(blockState -> lightLevel) : properties);
    }

    static ManaSpreaderBlock manaSpreader(ManaSpreaderBlock.Variant variant) {
        ManaSpreaderBlock manaSpreaderBlock = new ManaSpreaderBlock(variant, BlockBehaviour.Properties.copy(dreamwood).isValidSpawn(NO_SPAWN));
        ((RedstoneSpreader) (manaSpreaderBlock)).setRedstone(true);
        return manaSpreaderBlock;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

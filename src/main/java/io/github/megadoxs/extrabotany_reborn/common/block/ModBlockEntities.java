package io.github.megadoxs.extrabotany_reborn.common.block;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.entity_block.MortarBlockEntity;
import io.github.megadoxs.extrabotany_reborn.common.block.flower.generating.BloodyEnchantressBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExtraBotany_Reborn.MOD_ID);

    public static final RegistryObject<BlockEntityType<BloodyEnchantressBlockEntity>> BLOODY_ENCHANTRESS = BLOCK_ENTITIES.register("bloody_enchantress", () -> BlockEntityType.Builder.of(BloodyEnchantressBlockEntity::new, ModBlocks.BLOODY_ENCHANTRESS.get()).build(null));
    public static final RegistryObject<BlockEntityType<MortarBlockEntity>> MORTAR = BLOCK_ENTITIES.register("mortar", () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}

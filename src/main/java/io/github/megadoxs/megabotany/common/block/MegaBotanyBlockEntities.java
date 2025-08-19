package io.github.megadoxs.megabotany.common.block;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.block_entity.PedestalBlockEntity;
import io.github.megadoxs.megabotany.common.block.block_entity.SpiritPortalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MegaBotanyBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MegaBotany.MOD_ID);

    public static final RegistryObject<BlockEntityType<SpiritPortalBlockEntity>> SPIRIT_PORTAL = BLOCK_ENTITIES.register("spirit_portal", () -> BlockEntityType.Builder.of(SpiritPortalBlockEntity::new, MegaBotanyBlocks.SPIRIT_PORTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, MegaBotanyBlocks.PEDESTAL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

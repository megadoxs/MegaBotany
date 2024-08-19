package io.github.megadoxs.extrabotany_reborn.common.entity;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExtraBotany_Reborn.MOD_ID);

    public static final RegistryObject<EntityType<AuraFire>> AURA_FIRE = ENTITY_TYPES.register("aura_fire", () -> EntityType.Builder.<AuraFire>of(AuraFire::new, MobCategory.MISC).sized(0, 0).build("aura_fire"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}

package io.github.megadoxs.megabotany.common.entity;

import io.github.megadoxs.megabotany.common.MegaBotany;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MegaBotanyEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MegaBotany.MOD_ID);

    public static final RegistryObject<EntityType<AuraFire>> AURA_FIRE = ENTITY_TYPES.register("aura_fire", () -> EntityType.Builder.of(AuraFire::new, MobCategory.MISC).sized(0, 0).build("aura_fire"));
    public static final RegistryObject<EntityType<LandMine>> LAND_MINE = ENTITY_TYPES.register("land_mine", () -> EntityType.Builder.of(LandMine::new, MobCategory.MISC).sized(5F, 0.1F).updateInterval(40).build("land_mine"));
    public static final RegistryObject<EntityType<ThrownBrew>> THROWN_BREW = ENTITY_TYPES.register("thrown_brew", () -> EntityType.Builder.<ThrownBrew>of(ThrownBrew::new, MobCategory.MISC).sized(0.5f, 0.5f).build("thrown_brew"));
    public static final RegistryObject<EntityType<GaiaGuardianIII>> GAIA_GUARDIAN_III = ENTITY_TYPES.register("gaia_guardian_iii", () -> EntityType.Builder.of(GaiaGuardianIII::new, MobCategory.MONSTER).sized(0.6F, 1.8F).fireImmune().build("gaia_guardian_iii"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

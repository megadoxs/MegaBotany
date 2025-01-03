package io.github.megadoxs.megabotany.common.block;

import com.google.common.collect.ImmutableSet;
import io.github.megadoxs.megabotany.common.MegaBotany;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MegaBotanyPOITypes {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, MegaBotany.MOD_ID);

    public static final RegistryObject<PoiType> REIKAR_LILY = POI_TYPES.register("reikar_lily", () -> new PoiType(ImmutableSet.copyOf(Stream.concat(MegaBotanyFlowerBlocks.reikarLily.getStateDefinition().getPossibleStates().stream(), MegaBotanyFlowerBlocks.reikarLilyFloating.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toSet())), 0, 1));
    public static final RegistryObject<PoiType> TINKLE = POI_TYPES.register("tinkle", () -> new PoiType(ImmutableSet.copyOf(Stream.concat(MegaBotanyFlowerBlocks.tinkle.getStateDefinition().getPossibleStates().stream(), MegaBotanyFlowerBlocks.tinkleFloating.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toSet())), 0, 1));
    public static final RegistryObject<PoiType> MIRRORTUNIA = POI_TYPES.register("mirrortunia", () -> new PoiType(ImmutableSet.copyOf(Stream.concat(MegaBotanyFlowerBlocks.mirrortunia.getStateDefinition().getPossibleStates().stream(), MegaBotanyFlowerBlocks.mirrortuniaFloating.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toSet())), 0, 1));
    public static final RegistryObject<PoiType> NECROFLEUR = POI_TYPES.register("necrofleur", () -> new PoiType(ImmutableSet.copyOf(Stream.concat(MegaBotanyFlowerBlocks.necrofleur.getStateDefinition().getPossibleStates().stream(), MegaBotanyFlowerBlocks.necrofleurFloating.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toSet())), 0, 1));
    public static final RegistryObject<PoiType> NECROFLEUR_CHIBI = POI_TYPES.register("necrofleur_chibi", () -> new PoiType(ImmutableSet.copyOf(MegaBotanyFlowerBlocks.necrofleurChibi.getStateDefinition().getPossibleStates()), 0, 1));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
    }
}

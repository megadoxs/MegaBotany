package io.github.megadoxs.megabotany.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MegaBotanyLayerDefinition {
    public static void init(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(MegaBotanyModelLayer.MANAWEAVEDSTEEL_INNER_ARMOR, () -> LayerDefinition.create(ModelArmorManaweavedsteel.createInsideMesh(), 128, 128));
        consumer.accept(MegaBotanyModelLayer.MANAWEAVEDSTEEL_OUTER_ARMOR, () -> LayerDefinition.create(ModelArmorManaweavedsteel.createOutsideMesh(), 128, 128));
        consumer.accept(MegaBotanyModelLayer.SHADOWIUM_INNER_ARMOR, () -> LayerDefinition.create(ModelArmorShadowium.createInsideMesh(), 128, 128));
        consumer.accept(MegaBotanyModelLayer.SHADOWIUM_OUTER_ARMOR, () -> LayerDefinition.create(ModelArmorShadowium.createOutsideMesh(), 128, 128));
        consumer.accept(MegaBotanyModelLayer.PHOTONIUM_INNER_ARMOR, () -> LayerDefinition.create(ModelArmorPhotonium.createInsideMesh(), 128, 128));
        consumer.accept(MegaBotanyModelLayer.PHOTONIUM_OUTER_ARMOR, () -> LayerDefinition.create(ModelArmorPhotonium.createOutsideMesh(), 128, 128));
        consumer.accept(MegaBotanyModelLayer.ORICHALCOS_FEMALE_INNER_ARMOR, () -> LayerDefinition.create(ModelArmorOrichalcosFemale.createInsideMesh(), 64, 128));
        consumer.accept(MegaBotanyModelLayer.ORICHALCOS_FEMALE_OUTER_ARMOR, () -> LayerDefinition.create(ModelArmorOrichalcosFemale.createOutsideMesh(), 64, 128));
    }
}

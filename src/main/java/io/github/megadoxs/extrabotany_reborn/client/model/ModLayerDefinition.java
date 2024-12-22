package io.github.megadoxs.extrabotany_reborn.client.model;

import io.github.megadoxs.extrabotany_reborn.client.renderer.ExplosiveMissileRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModLayerDefinition {
    public static void init(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer){
        consumer.accept(ModModelLayer.ORICHALCOS_FEMALE_OUTER_ARMOR, () -> LayerDefinition.create(ModelArmorOrichalcosFemale.createOutsideMesh(), 64, 128));
        consumer.accept(ModModelLayer.ORICHALCOS_FEMALE_INNER_ARMOR, () -> LayerDefinition.create(ModelArmorOrichalcosFemale.createInsideMesh(), 64, 128));
        consumer.accept(ModModelLayer.EXPLOSIVE_MISSILE, ExplosiveMissileRenderer::createSkullLayer);
    }
}

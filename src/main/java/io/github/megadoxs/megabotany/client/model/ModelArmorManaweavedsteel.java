package io.github.megadoxs.megabotany.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelArmorManaweavedsteel {
    public static MeshDefinition createInsideMesh() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

        //leggings
        PartDefinition skirt = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        skirt.addOrReplaceChild("front_center", CubeListBuilder.create().mirror().texOffs(0, 65).addBox(0, 0, 0, 6, 7, 1), PartPose.offsetAndRotation(-3, 9, -2, -0.2617994F, 0, 0));
        skirt.addOrReplaceChild("front_left", CubeListBuilder.create().mirror().texOffs(15, 65).addBox(0, 0, 0, 3, 7, 1), PartPose.offsetAndRotation(2.7f, 9, -2, -0.2617994F, -0.5235988F, 0));
        skirt.addOrReplaceChild("front_right", CubeListBuilder.create().mirror().texOffs(24, 65).addBox(0, 0, 0, 3, 7, 1), PartPose.offsetAndRotation(-2.2f, 9, -1, 0.2617994F, 3.665191F, 0));
        skirt.addOrReplaceChild("back_center", CubeListBuilder.create().mirror().texOffs(51, 65).addBox(0, 0, 0, 6, 7, 1), PartPose.offsetAndRotation(-3, 9, 1, 0.2617994F, 0, 0));
        skirt.addOrReplaceChild("back_left", CubeListBuilder.create().mirror().texOffs(75, 65).addBox(0, 0, 0, 3, 7, 1), PartPose.offsetAndRotation(-4.5f, 9, -0.5f, 0.2617994F, -0.5235988F, 0));
        skirt.addOrReplaceChild("back_right", CubeListBuilder.create().mirror().texOffs(66, 65).addBox(0, 0, 0, 3, 7, 1), PartPose.offsetAndRotation(5.5f, 9, 0.5f, -0.2617994F, 3.665191F, 0));
        skirt.addOrReplaceChild("side_left", CubeListBuilder.create().mirror().texOffs(33, 65).addBox(0, 0, 0, 1, 6, 3), PartPose.offsetAndRotation(-4, 10, -1.5f, 0, 0, 0.3490659F));
        skirt.addOrReplaceChild("side_right", CubeListBuilder.create().mirror().texOffs(42, 65).addBox(0, 0, 0, 1, 6, 3), PartPose.offsetAndRotation(3, 10.5f, -1.5f, 0, 0, -0.3490659F));

        return mesh;
    }

    public static MeshDefinition createOutsideMesh() {
        CubeDeformation deformation = new CubeDeformation(0.1F);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //helmet
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("left_hair", CubeListBuilder.create().texOffs(0, 33).mirror().addBox(0, 0, 0, 2, 12, 2, deformation), PartPose.offsetAndRotation(2.5f, -9, -1, 0.2617994F, 0, -0.4014257F));
        head.addOrReplaceChild("right_hair", CubeListBuilder.create().texOffs(9, 33).mirror().addBox(0, 0, 0, 2, 12, 2, deformation), PartPose.offsetAndRotation(-4.33f, -9.8f, -1, 0.2617994F, 0, 0.4014257F));
        head.addOrReplaceChild("left_circle", CubeListBuilder.create().texOffs(18, 33).mirror().addBox(0, 0, 0, 1, 3, 3, deformation), PartPose.offsetAndRotation(4.5f, -10, -1, 0, 0, -0.1047198F));
        head.addOrReplaceChild("right_circle", CubeListBuilder.create().texOffs(27, 33).mirror().addBox(0, 0, 0, 1, 3, 3, deformation), PartPose.offsetAndRotation(-5.5f, -10, -1, 0, 0, 0.1047198F));
        head.addOrReplaceChild("head_band", CubeListBuilder.create().texOffs(36, 33).mirror().addBox(0, 0, 0, 10, 3, 1, deformation), PartPose.offset(-5, -9, -1.3f));
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        //chestplate
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).mirror().addBox(-4, 0, -2, 8, 12, 4, deformation), PartPose.ZERO);
        body.addOrReplaceChild("breasts", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(0, 0, 0, 6, 3, 3), PartPose.offsetAndRotation(-3, 2, -4, 0.6108652F, 0, 0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

        //boots
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 12, 4, deformation), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2, 0, -2, 4, 12, 4, deformation), PartPose.ZERO);
        return mesh;
    }
}

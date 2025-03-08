package io.github.megadoxs.megabotany.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelArmorOrichalcosFemale {
    public static MeshDefinition createInsideMesh() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

        //leggings
        PartDefinition skirt = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        skirt.addOrReplaceChild("front", CubeListBuilder.create().texOffs(1, 48).addBox(0F, 0F, 0F, 8, 5, 1), PartPose.offsetAndRotation(-4F, 9F, -2F, -0.3490659F, 0F, 0F));
        skirt.addOrReplaceChild("back", CubeListBuilder.create().texOffs(20, 48).addBox(0F, 0F, 0F, 8, 5, 1), PartPose.offsetAndRotation(-4F, 9F, 1F, 0.3490659F, 0F, 0F));
        skirt.addOrReplaceChild("left", CubeListBuilder.create().texOffs(1, 38).addBox(0F, 0F, 0F, 1, 5, 4), PartPose.offsetAndRotation(3F, 9F, -2F, 0F, 0F, -0.3490659F));
        skirt.addOrReplaceChild("right", CubeListBuilder.create().texOffs(12, 38).addBox(0F, 0F, 0F, 1, 5, 4), PartPose.offsetAndRotation(-4F, 9F, -2F, 0F, 0F, 0.3490659F));
        skirt.addOrReplaceChild("front_apron", CubeListBuilder.create().texOffs(1, 32).addBox(0F, 0F, 0F, 6, 4, 1), PartPose.offsetAndRotation(-3F, 9F, -2F, -0.5235988F, 0F, 0F));
        skirt.addOrReplaceChild("back_apron", CubeListBuilder.create().texOffs(16, 32).addBox(0F, 0F, 0F, 6, 4, 1), PartPose.offsetAndRotation(-3F, 9F, 1F, 0.5235988F, 0F, 0F));

        return mesh;
    }

    public static MeshDefinition createOutsideMesh() {
        CubeDeformation deformation = new CubeDeformation(0.1F);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //helmet
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(1, 55).mirror().addBox(0, 0, 0, 3, 3, 2, deformation), PartPose.offsetAndRotation(3, -10.8f, -3, 0, 0, 0.9075712F));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(1, 55).mirror().addBox(0, 0, 0, 3, 3, 2, deformation), PartPose.offsetAndRotation(-5, -8.5f, -3, 0, 0, -0.9075712F));
        head.addOrReplaceChild("head_band", CubeListBuilder.create().texOffs(1, 61).addBox(0, 0, 0, 10, 4, 1), PartPose.offset(-5, -9, -2));
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        //chestplate
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).mirror().addBox(-4, 0, -2, 8, 12, 4, deformation), PartPose.ZERO);
        body.addOrReplaceChild("breasts", CubeListBuilder.create().texOffs(1, 67).mirror().addBox(0, 0, 0, 6, 4, 3), PartPose.offsetAndRotation(-3, 1, -4, 0.1745329F, 0, 0));
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror().addBox(0, -1, -1, 2, 2, 2, deformation), PartPose.offset(4, 2, 0));
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().mirror().addBox(0, -1, -1, 2, 2, 2, deformation), PartPose.offset(-4, 2, 0));
        left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create().texOffs(1, 75).mirror().addBox(0, 0, 0, 5, 4, 6, deformation), PartPose.offset(-1, -2, -3));
        right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create().texOffs(24, 75).mirror().addBox(0, 0, 0, 5, 4, 6, deformation), PartPose.offset(-4, -2, -3));

        //boots
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 12, 4, deformation), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2, 0, -2, 4, 12, 4, deformation), PartPose.ZERO);
        return mesh;
    }
}

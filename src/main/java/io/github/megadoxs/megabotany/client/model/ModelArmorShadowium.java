package io.github.megadoxs.megabotany.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelArmorShadowium {
    public static MeshDefinition createInsideMesh() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

        //leggings
        PartDefinition pants = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        pants.addOrReplaceChild("front_Center1", CubeListBuilder.create().mirror().texOffs(0, 67).addBox(0, 0, 0, 5, 5, 1), PartPose.offsetAndRotation(-2.5f, 10, -3, -0.2792527f, 0, 0));
        pants.addOrReplaceChild("front_center2", CubeListBuilder.create().mirror().texOffs(0, 67).addBox(0, 0, 0, 5, 5, 1), PartPose.offsetAndRotation(-2.5f, 12, -3, -0.2792527f, 0, 0));
        pants.addOrReplaceChild("front_center3", CubeListBuilder.create().mirror().texOffs(0, 67).addBox(0, 0, 0, 5, 5, 1), PartPose.offsetAndRotation(-2.5f, 14, -3, -0.2792527f, 0, 0));

        pants.addOrReplaceChild("front_left1", CubeListBuilder.create().mirror().texOffs(22, 67).addBox(0, 0, 0, 3, 3, 1), PartPose.offsetAndRotation(1.5f, 10, -3, -0.2617994f, 0, 0));
        pants.addOrReplaceChild("front_left2", CubeListBuilder.create().mirror().texOffs(22, 67).addBox(0, 0, 0, 3, 3, 1), PartPose.offsetAndRotation(1.5f, 12, -3, -0.2617994f, 0, 0));
        pants.addOrReplaceChild("front_left3", CubeListBuilder.create().mirror().texOffs(22, 67).addBox(0, 0, 0, 3, 3, 1), PartPose.offsetAndRotation(1.5f, 14, -3, -0.2617994f, 0, 0));
        pants.addOrReplaceChild("front_right1", CubeListBuilder.create().mirror().texOffs(13, 67).addBox(0, 0, 0, 3, 3, 1), PartPose.offsetAndRotation(-4.5f, 10, -3, -0.2617994f, 0, 0));
        pants.addOrReplaceChild("front_right2", CubeListBuilder.create().mirror().texOffs(13, 67).addBox(0, 0, 0, 3, 3, 1), PartPose.offsetAndRotation(-4.5f, 12, -3, -0.2617994f, 0, 0));
        pants.addOrReplaceChild("front_right3", CubeListBuilder.create().mirror().texOffs(13, 67).addBox(0, 0, 0, 3, 3, 1), PartPose.offsetAndRotation(-4.5f, 14, -3, -0.2617994f, 0, 0));

        pants.addOrReplaceChild("back1", CubeListBuilder.create().mirror().texOffs(11, 74).addBox(0, 0, 0, 9, 5, 1), PartPose.offsetAndRotation(-4.5f, 10, 1.8f, 0.2617994f, 0, 0));
        pants.addOrReplaceChild("back2", CubeListBuilder.create().mirror().texOffs(11, 74).addBox(0, 0, 0, 9, 5, 1), PartPose.offsetAndRotation(-4.5f, 12, 1.8f, 0.2617994f, 0, 0));
        pants.addOrReplaceChild("back3", CubeListBuilder.create().mirror().texOffs(11, 74).addBox(0, 0, 0, 9, 5, 1), PartPose.offsetAndRotation(-4.5f, 14, 1.8f, 0.2617994f, 0, 0));
        pants.addOrReplaceChild("left1", CubeListBuilder.create().mirror().texOffs(0, 74).addBox(0, 0, 0, 1, 5, 4), PartPose.offsetAndRotation(3, 10, -2, 0, 0, -0.2617994f));
        pants.addOrReplaceChild("left2", CubeListBuilder.create().mirror().texOffs(0, 74).addBox(0, 0, 0, 1, 5, 4), PartPose.offsetAndRotation(3, 12, -2, 0, 0, -0.2617994f));
        pants.addOrReplaceChild("left3", CubeListBuilder.create().mirror().texOffs(0, 74).addBox(0, 0, 0, 1, 5, 4), PartPose.offsetAndRotation(3, 14, -2, 0, 0, -0.2617994f));
        pants.addOrReplaceChild("right1", CubeListBuilder.create().mirror().texOffs(0, 74).addBox(0, 0, 0, 1, 5, 4), PartPose.offsetAndRotation(-4.2f, 9.8f, -2, 0, 0, 0.2617994f));
        pants.addOrReplaceChild("right2", CubeListBuilder.create().mirror().texOffs(0, 74).addBox(0, 0, 0, 1, 5, 4), PartPose.offsetAndRotation(-4.2f, 11.8f, -2, 0, 0, 0.2617994f));
        pants.addOrReplaceChild("right3", CubeListBuilder.create().mirror().texOffs(0, 74).addBox(0, 0, 0, 1, 5, 4), PartPose.offsetAndRotation(-4.2f, 13.8f, -2, 0, 0, 0.2617994f));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        return mesh;
    }

    public static MeshDefinition createOutsideMesh() {
        CubeDeformation deformation = new CubeDeformation(0.1F);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("top", CubeListBuilder.create().mirror().texOffs(0, 33).addBox(0, 0, 0, 9, 3, 9, deformation), PartPose.offset(-4.5f, -8.5f, -4.5f));
        head.addOrReplaceChild("back", CubeListBuilder.create().mirror().texOffs(37, 33).addBox(0, 0, 0, 9, 7, 1), PartPose.offsetAndRotation(-4.5f, -8, 3, 0.2617994f, 0, 0));
        head.addOrReplaceChild("medasl", CubeListBuilder.create().mirror().texOffs(7, 50).addBox(0, 0, 0, 2, 4, 1, deformation), PartPose.offset(-1, -11.5f, -5));
        head.addOrReplaceChild("front", CubeListBuilder.create().mirror().texOffs(97, 33).addBox(0, 0, 0, 6, 2, 1, deformation), PartPose.offset(-3f, -8, -5.1f));
        head.addOrReplaceChild("front1", CubeListBuilder.create().mirror().texOffs(58, 33).addBox(0, 0, 0, 3, 5, 1), PartPose.offsetAndRotation(-5.7f, -6, -1.9f, 0, 0.9948377f, 0));
        head.addOrReplaceChild("front2", CubeListBuilder.create().mirror().texOffs(67, 33).addBox(0, 0, 0, 3, 5, 1), PartPose.offsetAndRotation(3.7f, -6, -4.5f, 0, -0.9948377f, 0));
        head.addOrReplaceChild("right1", CubeListBuilder.create().mirror().texOffs(0, 50).addBox(0, 0, 0, 2, 6, 1, deformation), PartPose.offsetAndRotation(-5, -13, -4.9f, 0.0872665f, 0.0872665f, -0.2617994f));
        head.addOrReplaceChild("right2", CubeListBuilder.create().mirror().texOffs(76, 33).addBox(0, 0, 0, 1, 6, 9, deformation), PartPose.offsetAndRotation(-4, -8, -4.5f, 0, 0, 0.3316126f));
        head.addOrReplaceChild("right3", CubeListBuilder.create().mirror().texOffs(14, 50).addBox(0, 0, 0, 3, 4, 1), PartPose.offsetAndRotation(-7, -7.2f, -4.3f, 0, 0, 0.3316126f));
        head.addOrReplaceChild("left1", CubeListBuilder.create().mirror().texOffs(53, 50).addBox(0, 0, 0, 2, 6, 1), PartPose.offsetAndRotation(3, -13.5f, -5, 0.0872665f, -0.0872665f, 0.2617994f));
        head.addOrReplaceChild("left2", CubeListBuilder.create().mirror().texOffs(23, 50).addBox(0, 0, 0, 1, 6, 9), PartPose.offsetAndRotation(3, -8, -4.5f, 0, 0, -0.3316126f));
        head.addOrReplaceChild("left3", CubeListBuilder.create().mirror().texOffs(44, 50).addBox(0, 0, 0, 3, 4, 1), PartPose.offsetAndRotation(3.7f, -6.5f, -4.3f, 0, 0, -0.3316126f));
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        //chestplate
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().mirror().texOffs(16, 16).addBox(-4, 0, -2, 8, 12, 4), PartPose.ZERO);
        body.addOrReplaceChild("front", CubeListBuilder.create().mirror().texOffs(0, 84).addBox(0, 0, 0, 6, 7, 1, deformation), PartPose.offset(-3, 1, -3));
        body.addOrReplaceChild("book", CubeListBuilder.create().mirror().texOffs(0, 93).addBox(0, 0, 0, 4, 6, 2, deformation), PartPose.offsetAndRotation(1, 1.2f, 2.7f, 0, 0, 0.7853982f));
        body.addOrReplaceChild("back", CubeListBuilder.create().mirror().texOffs(0, 102).addBox(0, 0, 0, 6, 10, 1, deformation), PartPose.offset(-3, 1, 1.7f));
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        left_arm.addOrReplaceChild("1", CubeListBuilder.create().mirror().texOffs(15, 85).addBox(0, 0, 0, 5, 2, 5, deformation), PartPose.offsetAndRotation(-2, -3, -2.5f, 0, 0, -0.1745329f));
        left_arm.addOrReplaceChild("2", CubeListBuilder.create().mirror().texOffs(60, 84).addBox(0, 0, 0, 1, 5, 4, deformation), PartPose.offsetAndRotation(1.7f, -2, -2, 0, 0, -0.3490659f));
        left_arm.addOrReplaceChild("3", CubeListBuilder.create().mirror().texOffs(49, 84).addBox(0, 0, 0, 1, 6, 4, deformation), PartPose.offsetAndRotation(1.7f, -1, -2, 0, 0, -0.1745329f));
        left_arm.addOrReplaceChild("4", CubeListBuilder.create().mirror().texOffs(36, 84).addBox(0, 0, 0, 2, 5, 4, new CubeDeformation(0.25f)), PartPose.offset(0.8f, 3.2f, -2));
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        right_arm.addOrReplaceChild("1", CubeListBuilder.create().mirror().texOffs(15, 96).addBox(0, 0, 0, 5, 2, 5, deformation), PartPose.offsetAndRotation(-3, -4, -2.5f, 0, 0, 0.1745329f));
        right_arm.addOrReplaceChild("2", CubeListBuilder.create().mirror().texOffs(36, 96).addBox(0, 0, 0, 1, 5, 4, deformation), PartPose.offsetAndRotation(-2.7f, -2, -2, 0, 0, 0.3490659f));
        right_arm.addOrReplaceChild("3", CubeListBuilder.create().mirror().texOffs(47, 96).addBox(0, 0, 0, 1, 6, 4, deformation), PartPose.offsetAndRotation(-2.7f, -1, -2, 0, 0, 0.1745329f));
        right_arm.addOrReplaceChild("4", CubeListBuilder.create().mirror().texOffs(58, 96).addBox(0, 0, 0, 2, 5, 4, new CubeDeformation(0.25f)), PartPose.offset(-2.8f, 3.2f, -2));

        //boots
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create().mirror().texOffs(0, 114).addBox(0, 0, 0, 4, 4, 5, deformation), PartPose.offset(-2, 8, -2.5f));
        left_leg.addOrReplaceChild("left_foot_toes", CubeListBuilder.create().mirror().texOffs(19, 114).addBox(0, 0, 0, 3, 3, 1, deformation), PartPose.offset(-2, 9, -3));
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create().mirror().texOffs(0, 114).addBox(0, 0, 0, 4, 4, 5, deformation), PartPose.offset(-2, 8, -2.5f));
        right_leg.addOrReplaceChild("right_foot_toes", CubeListBuilder.create().mirror().texOffs(19, 114).addBox(0, 0, 0, 3, 3, 1, deformation), PartPose.offset(-1, 9, -3));

        return mesh;
    }
}

package io.github.megadoxs.megabotany.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelArmorPhotonium {
    public static MeshDefinition createInsideMesh() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

        //leggings
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12,0));
        right_leg.addOrReplaceChild("1", CubeListBuilder.create().texOffs(112, 112).addBox(-2, 0, -2, 4, 12, 4), PartPose.ZERO);
        right_leg.addOrReplaceChild("2", CubeListBuilder.create().texOffs(108, 61).addBox(-2.5f, -0.5f, -2.5f, 5, 13, 5), PartPose.ZERO);
        right_leg.addOrReplaceChild("3", CubeListBuilder.create().texOffs(98, 61).addBox(-2, 1, -3.5f, 4, 5, 1), PartPose.ZERO);
        right_leg.addOrReplaceChild("4", CubeListBuilder.create().texOffs(84, 70).addBox(-3, 2, -3, 6, 3, 6), PartPose.ZERO);
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12, 0));
        left_leg.addOrReplaceChild("1", CubeListBuilder.create().texOffs(112, 112).addBox(-2, 0, -2, 4, 12, 4, true), PartPose.ZERO);
        left_leg.addOrReplaceChild("2", CubeListBuilder.create().texOffs(108, 61).addBox(-2.5f, -0.5f, -2.5f, 5, 13, 5, true), PartPose.ZERO);
        left_leg.addOrReplaceChild("3", CubeListBuilder.create().texOffs(98, 61).addBox(-2, 1, -3.5f, 4, 5, 1, true), PartPose.ZERO);
        left_leg.addOrReplaceChild("4", CubeListBuilder.create().texOffs(84, 70).addBox(-3, 2, -3, 6, 3, 6, true), PartPose.ZERO);
        return mesh;
    }

    public static MeshDefinition createOutsideMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5f, -8.5f, -4.5f, 9, 9, 9), PartPose.ZERO);
        head.addOrReplaceChild("shin", CubeListBuilder.create().texOffs(0, 18).addBox(-2, -1, -5, 4, 2, 2), PartPose.ZERO);
        head.addOrReplaceChild("right_center_bar", CubeListBuilder.create().texOffs(4, 23).addBox(-1.5f, -6, -5, 1, 5, 1), PartPose.ZERO);
        head.addOrReplaceChild("left_center_bar", CubeListBuilder.create().texOffs(8, 23).addBox(0.5f, -6, -5, 1, 5, 1), PartPose.ZERO);
        PartDefinition plates = head.addOrReplaceChild("plates", CubeListBuilder.create(), PartPose.offsetAndRotation(0, -8.5f, -2, 0, 0.7854f, 0));
        plates.addOrReplaceChild("1", CubeListBuilder.create().texOffs(0, 49).addBox(-3, -0.5f, -3, 6, 1, 6), PartPose.ZERO);
        plates.addOrReplaceChild("2",  CubeListBuilder.create().texOffs(0, 56).addBox(-3, -1.5f, -2, 5, 1, 5), PartPose.ZERO);
        plates.addOrReplaceChild("3", CubeListBuilder.create().texOffs(0, 65).addBox(-1, -2.5f, 1, 2, 1, 3), PartPose.ZERO);
        plates.addOrReplaceChild("4", CubeListBuilder.create().texOffs(0, 74).addBox(-3, -0.5f, 3, 4, 1, 1), PartPose.ZERO);
        plates.addOrReplaceChild("5", CubeListBuilder.create().texOffs(0, 69).addBox(-4, -0.5f, -1, 1, 1, 4), PartPose.ZERO);
        plates.addOrReplaceChild("6", CubeListBuilder.create().texOffs(0, 62).addBox(-4, -2.5f, -1, 5, 1, 2), PartPose.ZERO);
        plates.addOrReplaceChild("7", CubeListBuilder.create().texOffs(0, 35).addBox(-2, 1.5f, -4, 6, 1, 6), PartPose.ZERO);
        plates.addOrReplaceChild("8", CubeListBuilder.create().texOffs(0, 29).addBox(-1.5f, 2.5f, -3.5f, 5, 1, 5), PartPose.ZERO);
        plates.addOrReplaceChild("9", CubeListBuilder.create().texOffs(0, 42).addBox(-2.5f, 0.5f, -3.5f, 6, 1, 6), PartPose.ZERO);

        head.addOrReplaceChild("bang1", CubeListBuilder.create().texOffs(12, 22).addBox(-0.25f, -6.25f, -1, 1, 6, 1), PartPose.offsetAndRotation(2, 0, -4, 0, 0, 0.1745f));
        head.addOrReplaceChild("bang2", CubeListBuilder.create().texOffs(0, 22).addBox(-0.75f, -6.25f, 0, 1, 6, 1), PartPose.offsetAndRotation(-2, 0, -5,0, 0, -0.1745f));
        head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(16, 18).addBox(-1, -1, -2, 1, 3, 3), PartPose.offsetAndRotation(5.5f, -4, 0, 0.7854f, 0, 0));
        head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(16, 24).addBox(-11, -1, -2, 1, 3, 3), PartPose.offsetAndRotation(5.5f, -4, 0, 0.7854f, 0, 0));
        head.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(35, 0).addBox(-1.5f, -0.5f, 0.25f, 3, 0, 4), PartPose.offsetAndRotation(0, -9.5f, 2, 0.1745f, -0.3491f, 0));
        head.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(35, 4).addBox(-1.25f, -0.5f, 0, 3, 0, 5), PartPose.offsetAndRotation(-0.25f, -9.5f, 2.25f, -0.3491f, 0.2618f, 0));

        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        //chestplate
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("1", CubeListBuilder.create().texOffs(100, 0).addBox(-4.5f, -0.5f, -2.5f, 9, 13, 5), PartPose.ZERO);
        body.addOrReplaceChild("2", CubeListBuilder.create().texOffs(50, 8).addBox(-5, 6, -3, 10, 1, 6), PartPose.ZERO);
        body.addOrReplaceChild("3", CubeListBuilder.create().texOffs(68, 0).addBox(-5, 11, -3, 10, 2, 6), PartPose.ZERO);
        body.addOrReplaceChild("4", CubeListBuilder.create().texOffs(110, 18).addBox(-4, 0, 2.25f, 8, 6, 1), PartPose.ZERO);

        PartDefinition bone1 = body.addOrReplaceChild("bone1", CubeListBuilder.create(), PartPose.offsetAndRotation(0, 7, -2.5f, 0, 0, -0.7854f));
        bone1.addOrReplaceChild("1", CubeListBuilder.create().texOffs(50, 0).addBox(5.5f, -4, -1, 1, 4, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("2", CubeListBuilder.create().texOffs(82, 8).addBox(-1.5f, -2.5f, -1, 4, 4, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("3", CubeListBuilder.create().texOffs(54, 0).addBox(-0.5f, -5.5f, -2, 6, 6, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("4", CubeListBuilder.create().texOffs(96, 18).addBox(-3.5f, -2.5f, 5, 6, 6, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("5", CubeListBuilder.create().texOffs(46, 9).addBox(0, -6.5f, -1, 4, 1, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("6", CubeListBuilder.create().texOffs(50, 15).addBox(0.5f, -4.5f, -3, 4, 2, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("7", CubeListBuilder.create().texOffs(60, 15).addBox(2.5f, -2.5f, -3, 2, 2, 1), PartPose.ZERO);
        bone1.addOrReplaceChild("8", CubeListBuilder.create().texOffs(92, 8).addBox(-5.5f, 2.5f, -2.5f, 3, 3, 1), PartPose.ZERO);

        PartDefinition bone2 = body.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(0, 12, 0, 0, 0, -0.2618f));
        bone2.addOrReplaceChild("1", CubeListBuilder.create().texOffs(78, 15).addBox(0, -1, -4, 5, 2, 1), PartPose.ZERO);
        bone2.addOrReplaceChild("2", CubeListBuilder.create().texOffs(78, 15).addBox(0, -1, 3, 5, 2, 1), PartPose.ZERO);
        bone2.addOrReplaceChild("3", CubeListBuilder.create().texOffs(66, 21).addBox(5, -1, -3, 1, 2, 6), PartPose.ZERO);

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.rotation(0, 0, 0.5236f));
        bone3.addOrReplaceChild("1", CubeListBuilder.create().texOffs(78, 18).addBox(0, -0.25f, -4, 5, 2, 1), PartPose.ZERO);
        bone3.addOrReplaceChild("2", CubeListBuilder.create().texOffs(78, 18).addBox(0, -0.25f, 3, 5, 2, 1), PartPose.ZERO);
        bone3.addOrReplaceChild("3", CubeListBuilder.create().texOffs(80, 21).addBox(5, -0.25f, -3, 1, 2, 6), PartPose.ZERO);

        PartDefinition bone4 = body.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(0, 12, 0, 0, 0, 0.2618f));
        bone4.addOrReplaceChild("1", CubeListBuilder.create().texOffs(66, 15).addBox(-5, -1, -4, 5, 2, 1), PartPose.ZERO);
        bone4.addOrReplaceChild("2", CubeListBuilder.create().texOffs(66, 15).addBox(-5, -1, 3, 5, 2, 1), PartPose.ZERO);
        bone4.addOrReplaceChild("3", CubeListBuilder.create().texOffs(66, 21).addBox(-6, -1, -3, 1, 2, 6), PartPose.ZERO);

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.rotation(0, 0, -0.5236f));
        bone5.addOrReplaceChild("1", CubeListBuilder.create().texOffs(66, 18).addBox(-5, -0.25f, -4, 5, 2, 1), PartPose.ZERO);
        bone5.addOrReplaceChild("2", CubeListBuilder.create().texOffs(66, 18).addBox(-5, -0.25f, 3, 5, 2, 1), PartPose.ZERO);
        bone5.addOrReplaceChild("3", CubeListBuilder.create().texOffs(80, 21).addBox(-6f, -0.25f, -3, 1, 2, 6), PartPose.ZERO);

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5, 2, 0));
        left_arm.addOrReplaceChild("1", CubeListBuilder.create().texOffs(108, 48).addBox(-1.25f, 2.5f, -2.5f, 5, 8, 5, true), PartPose.ZERO);
        left_arm.addOrReplaceChild("2", CubeListBuilder.create().texOffs(98, 48).addBox(3.25f, 2, -2, 1, 6, 4,true), PartPose.ZERO);
        left_arm.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(100, 25).addBox(-1.75f, -4, -3.5f, 7, 5, 7,true), PartPose.rotation(0, 0, -0.3491f));
        left_arm.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(104, 37).addBox(-2, 0, -3, 6, 4, 6, true), PartPose.rotation(0, 0, -0.1745f));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5, 2, 0));
        right_arm.addOrReplaceChild("1", CubeListBuilder.create().texOffs(108, 48).addBox(-3.75f, 2.5f, -2.5f, 5, 8, 5), PartPose.ZERO);
        right_arm.addOrReplaceChild("2", CubeListBuilder.create().texOffs(98, 48).addBox(-4.25f, 3, -2, 1, 6, 4), PartPose.ZERO);
        right_arm.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(100, 25).addBox(-5.25f, -4, -3.5f, 7, 5, 7), PartPose.rotation(0, 0, 0.3491f));
        right_arm.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(104, 37).addBox(-4, 0, -3, 6, 4, 6), PartPose.rotation(0, 0, 0.1745f));

        //boots
        PartDefinition left_boot = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12, 0));
        left_boot.addOrReplaceChild("1", CubeListBuilder.create().texOffs(24, 31).addBox(-3, 8, -3, 6, 5, 6, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("2", CubeListBuilder.create().texOffs(24, 42).addBox(-3, 6, 2, 6, 2, 1, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("3", CubeListBuilder.create().texOffs(24, 45).addBox(2, 6, -1, 1, 2, 3, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("4", CubeListBuilder.create().texOffs(24, 45).addBox(-3, 6, -1, 1, 2, 3, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("5", CubeListBuilder.create().texOffs(29, 45).addBox(2, 7, -2, 1, 1, 1, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("6", CubeListBuilder.create().texOffs(29, 45).addBox(-3, 7, -2, 1, 1, 1, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("7", CubeListBuilder.create().texOffs(24, 27).addBox(-2, 10, -4, 4, 3, 1, true), PartPose.ZERO);
        left_boot.addOrReplaceChild("8", CubeListBuilder.create().texOffs(21, 33).addBox(-1, 9, -4, 2, 1, 1, true), PartPose.ZERO);
        PartDefinition right_boot = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12, 0));
        right_boot.addOrReplaceChild("1", CubeListBuilder.create().texOffs(24, 31).addBox(-3, 8, -3, 6, 5, 6), PartPose.ZERO);
        right_boot.addOrReplaceChild("2", CubeListBuilder.create().texOffs(24, 42).addBox(-3, 6, 2, 6, 2, 1), PartPose.ZERO);
        right_boot.addOrReplaceChild("3", CubeListBuilder.create().texOffs(24, 45).addBox(2, 6, -1, 1, 2, 3), PartPose.ZERO);
        right_boot.addOrReplaceChild("4", CubeListBuilder.create().texOffs(24, 45).addBox(-3, 6, -1, 1, 2, 3), PartPose.ZERO);
        right_boot.addOrReplaceChild("5", CubeListBuilder.create().texOffs(29, 45).addBox(2, 7, -2, 1, 1, 1), PartPose.ZERO);
        right_boot.addOrReplaceChild("6", CubeListBuilder.create().texOffs(29, 45).addBox(-3, 7, -2, 1, 1, 1), PartPose.ZERO);
        right_boot.addOrReplaceChild("7", CubeListBuilder.create().texOffs(24, 27).addBox(-2, 10, -4, 4, 3, 1), PartPose.ZERO);
        right_boot.addOrReplaceChild("8", CubeListBuilder.create().texOffs(21, 33).addBox(-1, 9, -4, 2, 1, 1), PartPose.ZERO);

        return mesh;
    }
}

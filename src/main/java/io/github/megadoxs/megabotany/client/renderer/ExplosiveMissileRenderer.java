package io.github.megadoxs.megabotany.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.megadoxs.megabotany.client.model.MegaBotanyModelLayer;
import io.github.megadoxs.megabotany.common.entity.Missile;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ExplosiveMissileRenderer extends EntityRenderer<Missile> {
    private final SkullModel model;

    public ExplosiveMissileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SkullModel(context.bakeLayer(MegaBotanyModelLayer.EXPLOSIVE_MISSILE));
    }

    @Override
    public void render(Missile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        float $$6 = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float $$7 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        this.model.setupAnim(0.0F, $$6, $$7);
        VertexConsumer $$8 = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, $$8, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    public static LayerDefinition createSkullLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 35).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }


    @Override
    public ResourceLocation getTextureLocation(Missile entity) {
//        if (ClientProxy.halloween) {
//            return new ResourceLocation();
//        }
        return new ResourceLocation("megabotany", "textures/model/explosive_red.png");
    }

//    private static class Model extends SkullModel {
//        Model(ModelPart root) {
//            super(root);
//        }
//    }
}
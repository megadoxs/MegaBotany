package io.github.megadoxs.megabotany.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.megadoxs.megabotany.common.entity.LandMine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;

public class LandMineRenderer extends EntityRenderer<LandMine> {
    public LandMineRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(LandMine pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.pushPose();
        AABB aabb = pEntity.getBoundingBox().move(pEntity.position().scale(-1));

        float gs = (float) (Math.sin(ClientTickHandler.total() / 20) + 1) * 0.2F + 0.6F;
        int r = 0;
        int g = 0;
        int b = 0;

        switch (pEntity.getMineType()) {
            case 0 -> {
                r = (int) (105 * gs);
                g = (int) (25 * gs);
                b = (int) (145 * gs);
            }
            case 1 -> r = (int) (255 * gs);
            case 2 -> {
                r = (int) (255 * gs);
                g = (int) (255 * gs);
            }
            case 3 -> {
                r = (int) (153 * gs);
                g = (int) (204 * gs);
                b = (int) (255 * gs);
            }
        }

        int alpha = 32;
        if (pEntity.tickCount < 8) {
            alpha *= (int) Math.min((pEntity.tickCount + pPartialTick) / 8F, 1F);
        } else if (pEntity.tickCount > 47) {
            alpha *= (int) Math.min(1F - (pEntity.tickCount - 47 + pPartialTick) / 8F, 1F);
        }

        pPoseStack.translate(aabb.minX, aabb.minY + RenderHelper.getOffY(), aabb.minZ);

        float f = 1F / 16F;
        float x = (float) (aabb.getXsize() - f);
        float z = (float) (aabb.getZsize() - f);

        VertexConsumer buffer = pBuffer.getBuffer(RenderHelper.RECTANGLE);
        Matrix4f mat = pPoseStack.last().pose();
        RenderHelper.flatRectangle(buffer, mat, f, x, 0, f, z, r, g, b, alpha);
        RenderHelper.incrementOffY();
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(LandMine pEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}

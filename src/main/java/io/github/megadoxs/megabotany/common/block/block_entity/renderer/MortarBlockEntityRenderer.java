package io.github.megadoxs.megabotany.common.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.megadoxs.megabotany.common.block.block_entity.MortarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class MortarBlockEntityRenderer implements BlockEntityRenderer<MortarBlockEntity> {
    public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MortarBlockEntity mortarBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = mortarBlockEntity.getRenderStack();

        poseStack.pushPose();
        poseStack.translate(0.6f, 0.2f, 0.5f);

        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, getLightLevel(mortarBlockEntity.getLevel(), mortarBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, mortarBlockEntity.getLevel(), 1);
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos blockPos) {
        int blight = level.getBrightness(LightLayer.BLOCK, blockPos);
        int slight = level.getBrightness(LightLayer.SKY, blockPos);
        return LightTexture.pack(blight, slight);
    }
}

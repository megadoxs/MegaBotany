package io.github.megadoxs.megabotany.common.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.megadoxs.megabotany.common.block.block_entity.SpiritPortalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.helper.VecHelper;

import java.util.Objects;

public class SpiritPortalBlockEntityRenderer implements BlockEntityRenderer<SpiritPortalBlockEntity> {
    private final TextureAtlasSprite daySprite;
    private final TextureAtlasSprite nightSprite;

    public SpiritPortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        daySprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("megabotany:block/spirit_portal_swirl")));
        nightSprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("megabotany:block/spirit_portal_swirl_night")));
    }

    @Override
    public void render(@NotNull SpiritPortalBlockEntity portal, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        AlfheimPortalState state = portal.getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE);
        if (state == AlfheimPortalState.OFF) {
            return;
        }

        float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 7D + 0.6D) * (Math.min(60, portal.ticksOpen) / 60F) * 0.5F;

        ms.pushPose();
        if (state == AlfheimPortalState.ON_X) {
            ms.translate(0.3125, 1, 2);
            ms.mulPose(VecHelper.rotateY(90));
        } else {
            ms.translate(-1, 1, 0.3125);
        }
        renderIcon(ms, buffers, portal.dayMode ? daySprite : nightSprite, 0, 0, 3, 3, alpha * portal.spriteAlpha, overlay);
        ms.popPose();

        ms.pushPose();
        if (state == AlfheimPortalState.ON_X) {
            ms.translate(0.6875, 1, -1);
            ms.mulPose(VecHelper.rotateY(90));
        } else {
            ms.translate(2, 1, 0.6875);
        }
        ms.mulPose(VecHelper.rotateY(180));
        renderIcon(ms, buffers, portal.dayMode ? daySprite : nightSprite, 0, 0, 3, 3, alpha * portal.spriteAlpha, overlay);
        ms.popPose();
    }

    public void renderIcon(PoseStack ms, MultiBufferSource buffers, TextureAtlasSprite icon, int x, int y, int width, int height, float alpha, int overlay) {
        VertexConsumer buffer = buffers.getBuffer(Sheets.translucentItemSheet());
        Matrix4f model = ms.last().pose();
        Matrix3f normal = ms.last().normal();
        buffer.vertex(model, x, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV1()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(model, x + width, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV1()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(model, x + width, y, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV0()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(model, x, y, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV0()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
    }
}

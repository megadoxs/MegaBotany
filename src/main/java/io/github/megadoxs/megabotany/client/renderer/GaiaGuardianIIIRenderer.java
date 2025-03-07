package io.github.megadoxs.megabotany.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.megadoxs.megabotany.common.entity.GaiaGuardianIII;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.model.armor.ArmorModels;

public class GaiaGuardianIIIRenderer extends HumanoidMobRenderer<GaiaGuardianIII, HumanoidModel<GaiaGuardianIII>> {
    private final GaiaGuardianIIIRenderer.Model normalModel = (GaiaGuardianIIIRenderer.Model) this.getModel();
    private final GaiaGuardianIIIRenderer.Model slimModel;

    public GaiaGuardianIIIRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GaiaGuardianIIIRenderer.Model(ctx.bakeLayer(ModelLayers.PLAYER)), 0.0F);
        this.slimModel = new GaiaGuardianIIIRenderer.Model(ctx.bakeLayer(ModelLayers.PLAYER_SLIM));
        ArmorModels.init(ctx);
    }

    public void render(@NotNull GaiaGuardianIII dopple, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
        int invulTime = dopple.getInvulnerableTime();
        ShaderInstance shader = CoreShaders.doppleganger();
        if (shader != null) {
            float grainIntensity, disfiguration;
            if (invulTime > 0) {
                grainIntensity = invulTime > 20 ? 1F : invulTime * 0.05F;
                disfiguration = grainIntensity * 0.3F;
            } else {
                disfiguration = (0.025F + dopple.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
                grainIntensity = 0.05F + dopple.hurtTime * ((1F - 0.15F) / 10F);
            }
            shader.safeGetUniform("BotaniaGrainIntensity").set(grainIntensity);
            shader.safeGetUniform("BotaniaDisfiguration").set(disfiguration);
        }

        Entity view = Minecraft.getInstance().getCameraEntity();
        if (view instanceof AbstractClientPlayer && DefaultPlayerSkin.getSkinModelName(view.getUUID()).equals("slim")) {
            this.model = this.slimModel;
        } else {
            this.model = this.normalModel;
        }

        super.render(dopple, yaw, partialTicks, ms, buffers, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GaiaGuardianIII entity) {
        Minecraft mc = Minecraft.getInstance();
        Entity var4 = mc.getCameraEntity();
        if (var4 instanceof AbstractClientPlayer clientPlayer) {
            return clientPlayer.getSkinTextureLocation();
        } else {
            return DefaultPlayerSkin.getDefaultSkin(entity.getUUID());
        }
    }

    protected boolean isBodyVisible(GaiaGuardianIII dopple) {
        return true;
    }

    private static class Model extends HumanoidModel<GaiaGuardianIII> {
        Model(ModelPart root) {
            super(root, RenderHelper::getDopplegangerLayer);
        }
    }
}

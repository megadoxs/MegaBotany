package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.item.equipment.bauble.FlugelTiaraItem;
import vazkii.botania.common.proxy.Proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static vazkii.botania.common.item.equipment.bauble.FlugelTiaraItem.getVariant;

public class CoreGod extends BaubleItem implements CustomCreativeTabContents {

    private static final ResourceLocation textureHud = new ResourceLocation("botania:textures/gui/hud_icons.png");
    public static final ResourceLocation textureHalo = new ResourceLocation("botania:textures/misc/halo.png");
    private static final String TAG_VARIANT = "variant";
    private static final String TAG_FLYING = "flying";
    private static final String TAG_GLIDING = "gliding";
    private static final String TAG_TIME_LEFT = "timeLeft";
    private static final String TAG_INFINITE_FLIGHT = "infiniteFlight";
    private static final String TAG_DASH_COOLDOWN = "dashCooldown";
    private static final String TAG_IS_SPRINTING = "isSprinting";
    private static final String TAG_BOOST_PENDING = "boostPending";
    private static final List<String> playersWithFlight = Collections.synchronizedList(new ArrayList<>());
    private static final int COST = 35;
    private static final int COST_OVERKILL = 105;
    private static final int SUBTYPES = 13;
    public static final int WING_TYPES = 14;
    private static final String SUPER_AWESOME_HASH = "4D0F274C5E3001C95640B5E88A821422C8B1E132264492C043A3D746B705C025";

    public CoreGod(Properties props) {
        super(props);
        Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity living) {
        if (living instanceof Player player) {
            boolean flying = player.getAbilities().flying;
            boolean wasSprting = ItemNBTHelper.getBoolean(stack, "isSprinting", false);
            boolean isSprinting = player.isSprinting();
            if (isSprinting != wasSprting) {
                ItemNBTHelper.setBoolean(stack, "isSprinting", isSprinting);
            }

            Vec3 look = player.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
            boolean maxCd;
            if (flying) {
                int cooldown = ItemNBTHelper.getInt(stack, "dashCooldown", 0);
                if (!wasSprting && isSprinting && cooldown == 0) {
                    player.setDeltaMovement(player.getDeltaMovement().add(look.x, 0.0, look.z));
                    player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.dash, SoundSource.PLAYERS, 1.0F, 1.0F);
                    ItemNBTHelper.setInt(stack, "dashCooldown", 80);
                    ItemNBTHelper.setBoolean(stack, "boostPending", true);
                } else if (cooldown > 0) {
                    if (ItemNBTHelper.getBoolean(stack, "boostPending", false)) {
                        living.moveRelative(5.0F, new Vec3(0.0, 0.0, 1.0));
                        ItemNBTHelper.removeEntry(stack, "boostPending");
                    }

                    ItemNBTHelper.setInt(stack, "dashCooldown", cooldown - 2);
                }
            } else {
                maxCd = ItemNBTHelper.getBoolean(stack, "gliding", false);
                boolean doGlide = living.isShiftKeyDown() && !living.onGround() && (living.getDeltaMovement().y() < -0.699999988079071 || maxCd);

                if (doGlide) {
                    float mul = 0.6F;
                    living.setDeltaMovement(look.x * (double) mul, Math.max(-0.15000000596046448, living.getDeltaMovement().y()), look.z * (double) mul);
                    living.fallDistance = 2.0F;
                }

                ItemNBTHelper.setBoolean(stack, "gliding", doGlide);
            }

            ItemNBTHelper.setBoolean(stack, "flying", flying);
        }

    }

    public static void updatePlayerFlyStatus(Player player) {
        ItemStack core = EquipmentHandler.findOrEmpty(MegaBotanyItems.GOD_CORE.get(), player);
        if (playersWithFlight.contains(playerStr(player))) {
            if (shouldPlayerHaveFlight(player)) {
                player.getAbilities().mayfly = true;
                if (player.getAbilities().flying) {
                    if (!player.level().isClientSide) {
                        if (!player.isCreative() && !player.isSpectator()) {
                            ManaItemHandler.instance().requestManaExact(core, player, 35, true);
                        }
                    } else if (Math.abs(player.getDeltaMovement().x()) > 0.1 || Math.abs(player.getDeltaMovement().z()) > 0.1) {
                        double x = player.getX() - 0.5;
                        double y = player.getY() - 0.5;
                        double z = player.getZ() - 0.5;
                        float r = 1.0F;
                        float g = 1.0F;
                        float b = 1.0F;
                        int variant = getVariant(core);
                        switch (variant) {
                            case 2:
                                r = 0.1F;
                                g = 0.1F;
                                b = 0.1F;
                                break;
                            case 3:
                                r = 0.0F;
                                g = 0.6F;
                                break;
                            case 4:
                                g = 0.3F;
                                b = 0.3F;
                                break;
                            case 5:
                                r = 0.6F;
                                g = 0.0F;
                                b = 0.6F;
                                break;
                            case 6:
                                r = 0.4F;
                                g = 0.0F;
                                b = 0.0F;
                                break;
                            case 7:
                                r = 0.2F;
                                g = 0.6F;
                                b = 0.2F;
                                break;
                            case 8:
                                r = 0.85F;
                                g = 0.85F;
                                b = 0.0F;
                                break;
                            case 9:
                                r = 0.0F;
                                b = 0.0F;
                        }

                        for (int i = 0; i < 2; ++i) {
                            SparkleParticleData data = SparkleParticleData.sparkle(2.0F * (float) Math.random(), r, g, b, 20);
                            player.level().addParticle(data, x + Math.random() * (double) player.getBbWidth(), y + Math.random() * 0.4, z + Math.random() * (double) player.getBbWidth(), 0.0, 0.0, 0.0);
                        }
                    }
                }
            } else {
                if (!player.isSpectator() && !player.getAbilities().instabuild) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.getAbilities().invulnerable = false;
                }

                playersWithFlight.remove(playerStr(player));
            }
        } else if (shouldPlayerHaveFlight(player)) {
            playersWithFlight.add(playerStr(player));
            player.getAbilities().mayfly = true;
        }
    }

    public static void playerLoggedOut(ServerPlayer player) {
        String username = player.getGameProfile().getName();
        playersWithFlight.remove(username + ":false");
        playersWithFlight.remove(username + ":true");
    }

    private static boolean shouldPlayerHaveFlight(Player player) {
        ItemStack armor = EquipmentHandler.findOrEmpty(MegaBotanyItems.GOD_CORE.get(), player);
        if (!armor.isEmpty()) {
            return ManaItemHandler.instance().requestManaExact(armor, player, 35, false);
        } else {
            return false;
        }
    }

    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltip, flags);
        tooltip.add(Component.translatable("misc.megabotany.wings" + getVariant(stack)));
    }

    private static String playerStr(Player player) {
        String var10000 = player.getGameProfile().getName();
        return var10000 + ":" + player.level().isClientSide;
    }

    @Override
    public void addToCreativeTab(Item item, CreativeModeTab.Output output) {
        for (int i = 0; i <= WING_TYPES; i++) {
            ItemStack stack = new ItemStack(this);
            if (i != 0)
                ItemNBTHelper.setInt(stack, TAG_VARIANT, i);
            output.accept(stack);
        }
    }

    public static class Renderer implements AccessoryRenderer {
        public Renderer() {
        }

        private static void renderBasic(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0.0, 0.5, 0.2);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                ms.translate(-1.0F, 0.0F, 0.0F);
                ms.mulPose(VecHelper.rotateZ(-60.0F));
                ms.scale(1.5F, -1.5F, -1.5F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
                ms.popPose();
            }

            ms.popPose();
        }

        private static void renderSephiroth(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0.0, 0.5, 0.2);
            ms.mulPose(VecHelper.rotateY(flap));
            ms.translate(-1.1, 0.0, 0.0);
            ms.mulPose(VecHelper.rotateZ(-60.0F));
            ms.scale(1.6F, -1.6F, -1.6F);
            Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
            ms.popPose();
        }

        private static void renderCirno(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(-0.8, 0.15, 0.25);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                if (i == 1) {
                    ms.mulPose(VecHelper.rotateY(180.0F));
                    ms.translate(-1.6, 0.0, 0.0);
                }

                ms.scale(1.6F, -1.6F, -1.6F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
                ms.popPose();
            }

            ms.popPose();
        }

        private static void renderPhoenix(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0.0, -0.2, 0.2);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                ms.translate(-0.9, 0.0, 0.0);
                ms.scale(1.7F, -1.7F, -1.7F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, 15728880, OverlayTexture.NO_OVERLAY, model);
                ms.popPose();
            }

            ms.popPose();
        }

        private static void renderKuroyukihime(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0.0, -0.4, 0.2);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                ms.translate(-1.3, 0.0, 0.0);
                ms.scale(2.5F, -2.5F, -2.5F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, 15728880, OverlayTexture.NO_OVERLAY, model);
                ms.popPose();
            }

            ms.popPose();
        }

        private static void renderCustomColor(HumanoidModel<?> bipedModel, BakedModel model, LivingEntity living, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap, int color) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0.0, 0.0, 0.2);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                ms.translate(-0.7, 0.0, 0.0);
                ms.scale(1.5F, -1.5F, -1.5F);
                RenderHelper.renderItemCustomColor(living, stack, color, ms, buffers, 15728880, OverlayTexture.NO_OVERLAY, model);
                ms.popPose();
            }

            ms.popPose();
        }

        private static void renderGodness(HumanoidModel<?> bipedModel, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0, 0.5, 0.2);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                ms.translate(i == 0 ? -0.15 : 0.15, -0.2, -0.12); // wings location on the back
                ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                ms.translate(-0.72, 0, 0); // rotate point //must be found first
                ms.mulPose(VecHelper.rotateZ(-10.0F));
                ms.scale(1.5F, -1.70F, -1.5F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels.INSTANCE.goddessWingIcons[i]);
                ms.popPose();
            }

            ms.popPose();
        }

        // yo can someone kill me please
        private static void renderElf(HumanoidModel<?> bipedModel, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0, 0.5, 0.2);

            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels.INSTANCE.elfWingIcons.length; j++) {
                    ms.pushPose();
                    ms.translate(i == 0 ? -0.15 : 0.15, 0, -0.05 + 0.01 * j);
                    ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                    ms.translate(-0.72 + 0.053 * j, 1.75 - (2 / (1 + (j * 0.1))), 0);
                    ms.mulPose(VecHelper.rotateZ(-6.0F * j));
                    ms.scale(1.5F - j * 0.1F, -1.70F + 0.15F * j, -1F);
                    Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels.INSTANCE.elfWingIcons[j]);
                    ms.popPose();
                }
            }

            ms.popPose();
        }

        private static void renderBasicCore(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap, double wingDistance) {
            ms.pushPose();
            bipedModel.body.translateAndRotate(ms);
            ms.translate(0, 0.5, 0.2);

            for (int i = 0; i < 2; ++i) {
                ms.pushPose();
                ms.translate(i == 0 ? -wingDistance : wingDistance, -0.3, -0.05);
                ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180.0F - flap));
                ms.translate(-0.72, 0, 0);
                ms.scale(1.5F, -1.70F, -1.5F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
                ms.popPose();
            }

            ms.popPose();
        }

        public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            int meta = FlugelTiaraItem.getVariant(stack);
            if (meta > 0) {
                BakedModel model;
                boolean var10000;
                label48:
                {
                    if (meta < 10)
                        model = MiscellaneousModels.INSTANCE.tiaraWingIcons[meta - 1];
                    else
                        model = MiscellaneousModels.INSTANCE.tiaraWingIcons[0]; // grrrrr
                    if (living instanceof Player player) {
                        if (player.getAbilities().flying) {
                            var10000 = true;
                            break label48;
                        }
                    }

                    var10000 = false;
                }

                boolean flying = var10000;
                float flap = 20.0F + (float) ((Math.sin((double) ((float) living.tickCount + partialTicks) * (double) (flying ? 0.4F : 0.2F)) + 0.5) * (double) (flying ? 30.0F : 5.0F));
                float alpha;
                int color;
                switch (meta) {
                    case 1:
                        renderBasic(bipedModel, model, stack, ms, buffers, light, flap);
                        ms.pushPose();
                        FlugelTiaraItem.ClientLogic.renderHalo(bipedModel, living, ms, buffers, partialTicks);
                        ms.popPose();
                        break;
                    case 2:
                        renderSephiroth(bipedModel, model, stack, ms, buffers, light, flap);
                        break;
                    case 3:
                        renderCirno(bipedModel, model, stack, ms, buffers, light);
                        break;
                    case 4:
                        renderPhoenix(bipedModel, model, stack, ms, buffers, flap);
                        break;
                    case 5:
                        renderKuroyukihime(bipedModel, model, stack, ms, buffers, flap);
                        break;
                    case 6, 8:
                        renderBasic(bipedModel, model, stack, ms, buffers, light, flap);
                        break;
                    case 7:
                        alpha = 0.5F + (float) Math.cos((double) ((float) living.tickCount + partialTicks) * 0.30000001192092896) * 0.2F;
                        color = 16777215 | (int) (alpha * 255.0F) << 24;
                        renderCustomColor(bipedModel, model, living, stack, ms, buffers, flap, color);
                        break;
                    case 9:
                        flap = -((float) ((Math.sin((double) ((float) living.tickCount + partialTicks) * 0.20000000298023224) + 0.6000000238418579) * (double) (flying ? 12.0F : 5.0F)));
                        alpha = 0.5F + (flying ? (float) Math.cos((double) ((float) living.tickCount + partialTicks) * 0.30000001192092896) * 0.25F + 0.25F : 0.0F);
                        color = 16777215 | (int) (alpha * 255.0F) << 24;
                        renderCustomColor(bipedModel, model, living, stack, ms, buffers, flap, color);
                        break;
                    case 10:
                        renderGodness(bipedModel, stack, ms, buffers, light, flap);
                        break;
                    case 11:
                        model = io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels.INSTANCE.flandreWingIcon;
                        renderBasicCore(bipedModel, model, stack, ms, buffers, light, flap, 0.15);
                        break;
                    case 12:
                        renderElf(bipedModel, stack, ms, buffers, light, flap);
                        break;
                    case 13:
                        model = io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels.INSTANCE.steamPunkWingIcon;
                        renderBasicCore(bipedModel, model, stack, ms, buffers, light, flap, 0.05);
                        break;
                    case 14:
                        model = io.github.megadoxs.megabotany.client.core.handler.MiscellaneousModels.INSTANCE.jimWingIcon;
                        renderBasicCore(bipedModel, model, stack, ms, buffers, light, flap, 0.15);
                        break;
                }

            }
        }
    }

    public static class ClientLogic {
        public ClientLogic() {
        }

        @SuppressWarnings("deprecation")
        private static int estimateAdditionalNumRowsRendered(Player player) {
            if (!player.isEyeInFluid(FluidTags.WATER) && player.getAirSupply() >= player.getMaxAirSupply()) {
                Entity playerVehicle = player.getVehicle();
                if (playerVehicle instanceof LivingEntity vehicle) {
                    if (vehicle.showVehicleHealth()) {
                        return (Math.min(30, (int) ((double) vehicle.getMaxHealth() + 0.5) / 2) - 1) / 10;
                    }
                }

                return 0;
            } else {
                return 1;
            }
        }

        public static void renderHUD(GuiGraphics gui, Player player, ItemStack stack) {
            int u = Math.max(1, FlugelTiaraItem.getVariant(stack)) * 9 - 9;
            int v = 0;
            Minecraft mc = Minecraft.getInstance();
            int xo = mc.getWindow().getGuiScaledWidth() / 2 + 10;
            int y = mc.getWindow().getGuiScaledHeight() - 10 * estimateAdditionalNumRowsRendered(player) - 49;
            int left = ItemNBTHelper.getInt(stack, "timeLeft", 1200);
            int segTime = 120;
            int segs = left / segTime + 1;
            int last = left % segTime;

            int width;
            for (width = 0; width < segs; ++width) {
                float trans = 1.0F;
                if (width == segs - 1) {
                    trans = (float) last / (float) segTime;
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(770, 771);
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, trans);
                RenderHelper.drawTexturedModalRect(gui, textureHud, xo + 8 * width, y, u, v, 9, 9);
            }

            if (player.getAbilities().flying) {
                width = ItemNBTHelper.getInt(stack, "dashCooldown", 0);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                if (width > 0) {
                    gui.fill(xo, y - 2, xo + 80, y - 1, -2013265920);
                }

                gui.fill(xo, y - 2, xo + width, y - 1, -1);
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}

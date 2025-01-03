package io.github.megadoxs.megabotany.api.block_entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.client.core.helper.RenderHelper;

import static vazkii.botania.client.gui.HUDHandler.drawSimpleManaHUD;

public abstract class FlowerBindableFunctionalFlowerBlockEntity<T extends FlowerBindableFunctionalFlowerBlockEntity<?>> extends FunctionalFlowerBlockEntity implements DimensionalFlowerWandBindable {

    private final Class<T> flowerBlockEntityClass;
    protected @Nullable BlockPos flowerBindingPos = null;
    protected @Nullable String flowerBindingDimensionId = null;
    protected boolean isValidFlowerBinding = false;
    private static final String TAG_FLOWER_BINDING = "FlowerBinding";
    private static final String TAG_DIMENSION_ID = "DimensionId";

    public FlowerBindableFunctionalFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<T> flowerBlockEntityClass) {
        super(type, pos, state);
        this.flowerBlockEntityClass = flowerBlockEntityClass;
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        // to update the custom hud
        if (!getLevel().isClientSide)
            isValidFlower();
    }

    public T getBlockFlower() {
        if (flowerBindingPos == null || flowerBindingDimensionId == null || flowerBindingDimensionId.isEmpty()) {
            return null;
        }
        Level dimension = getLevel().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(flowerBindingDimensionId)));
        BlockEntity be = dimension.getBlockEntity(flowerBindingPos);
        return flowerBlockEntityClass.isInstance(be) ? flowerBlockEntityClass.cast(be) : null;
    }

    public boolean isValidFlower() {
        T be = getBlockFlower();
        if (be == null) {
            if (isValidFlowerBinding) {
                isValidFlowerBinding = false;
                getLevel().sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
                setChanged();
            }
            return false;
        }

        boolean valid = be.flowerBindingPos != null && be.flowerBindingPos.equals(getBlockPos()) && be.flowerBindingDimensionId != null && be.flowerBindingDimensionId.equals(getLevel().dimension().location().toString());
        if (isValidFlowerBinding != valid) {
            isValidFlowerBinding = valid;
            setChanged();
            getLevel().sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
        return valid;
    }

    public @Nullable T findBindCandidateAt(BlockPos pos, String dimensionID) {
        if (level == null || pos == null || dimensionID == null) {
            return null;
        }

        Level dimension = level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionID)));

        if (dimension == null)
            return null;

        BlockEntity be = dimension.getBlockEntity(pos);
        return flowerBlockEntityClass.isInstance(be) ? flowerBlockEntityClass.cast(be) : null;
    }

    public abstract ItemStack getFlowerHudIcon();

    @Override
    public boolean bindTo(Player player, ItemStack wand, BlockPos pos, String dimensionId, Direction side) {
        boolean bound = bindTo(player, wand, pos, side);

        if (findBindCandidateAt(pos, dimensionId) != null && !isValidFlower() || ((!pos.equals(flowerBindingPos) || (flowerBindingDimensionId == null || !flowerBindingDimensionId.equals(dimensionId)))) && !pos.equals(getEffectivePos())) {
            this.flowerBindingPos = pos;
            this.flowerBindingDimensionId = dimensionId;
            this.isValidFlowerBinding = true;
            setChanged();
            sync();

            FlowerBindableFunctionalFlowerBlockEntity<?> flower = getBlockFlower();
            if (flower.flowerBindingPos == null || !flower.isValidFlowerBinding || !flower.flowerBindingPos.equals(getBlockPos()) || flower.flowerBindingDimensionId == null || !flower.flowerBindingDimensionId.equals(getLevel().dimension().location().toString())) {
                flower.bindTo(player, wand, getBlockPos(), getLevel().dimension().location().toString(), side);
            }

            return true;
        }
        return bound;
    }

    public static class WandHud<F extends FlowerBindableFunctionalFlowerBlockEntity<?>> implements WandHUD {
        protected final F flower;

        public WandHud(F flower) {
            this.flower = flower;
        }

        //TODO fix the is flower connected not updating when connection is invalid
        public void renderHUD(GuiGraphics gui, Minecraft mc, int minLeft, int minRight, int minDown) {
            String name = I18n.get(flower.getBlockState().getBlock().getDescriptionId());
            int color = flower.getColor();

            int centerX = mc.getWindow().getGuiScaledWidth() / 2;
            int centerY = mc.getWindow().getGuiScaledHeight() / 2;
            int left = (Math.max(102, mc.font.width(name)) + 4) / 2;
            // padding + item
            int right = left + 40;

            left = Math.max(left, minLeft);
            right = Math.max(right, minRight);

            RenderHelper.renderHUDBox(gui, centerX - left, centerY + 8, centerX + right, centerY + Math.max(30, minDown));


            PoseStack ms = gui.pose();
            drawSimpleManaHUD(gui, color, flower.getMana(), flower.getMaxMana(), name);

            int x = mc.getWindow().getGuiScaledWidth() / 2 + Math.max(51, mc.font.width(name) / 2) + 4;
            int y = mc.getWindow().getGuiScaledHeight() / 2 + 12;

            gui.renderItem(flower.getHudIcon(), x, y);
            gui.renderItem(flower.getFlowerHudIcon(), x + 20, y);

            RenderSystem.disableDepthTest();
            ms.pushPose();
            // Magic number to get the string above the item we just rendered.
            ms.translate(0, 0, 200);
            if (flower.isValidBinding()) {
                gui.drawString(mc.font, "✔", x + 10, y + 9, 0x004C00);
                gui.drawString(mc.font, "✔", x + 10, y + 8, 0x0BD20D);
            } else {
                gui.drawString(mc.font, "✘", x + 10, y + 9, 0x4C0000);
                gui.drawString(mc.font, "✘", x + 10, y + 8, 0xD2080D);
            }

            if (flower.isValidFlowerBinding) {
                gui.drawString(mc.font, "✔", x + 30, y + 9, 0x004C00);
                gui.drawString(mc.font, "✔", x + 30, y + 8, 0x0BD20D);
            } else {
                gui.drawString(mc.font, "✘", x + 30, y + 9, 0x4C0000);
                gui.drawString(mc.font, "✘", x + 30, y + 8, 0xD2080D);
            }
            ms.popPose();

            RenderSystem.enableDepthTest();
        }

        @Override
        public void renderHUD(GuiGraphics gui, Minecraft mc) {
            renderHUD(gui, mc, 0, 0, 0);
        }
    }

    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        if (flowerBindingPos != null && flowerBindingDimensionId != null) {
            CompoundTag binding = NbtUtils.writeBlockPos(flowerBindingPos);
            binding.putString(TAG_DIMENSION_ID, flowerBindingDimensionId);
            cmp.put(TAG_FLOWER_BINDING, binding);
        }
        cmp.putBoolean("Valid", isValidFlowerBinding);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        CompoundTag binding = cmp.getCompound(TAG_FLOWER_BINDING);

        flowerBindingPos = NbtUtils.readBlockPos(binding);
        flowerBindingDimensionId = binding.getString(TAG_DIMENSION_ID);
        isValidFlowerBinding = cmp.getBoolean("Valid");
    }
}

package io.github.megadoxs.megabotany.common.block.flower.functional;

import io.github.megadoxs.megabotany.api.block_entity.DimensionalFlowerWandBindable;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.mana.KeyLocked;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.BotaniaBlocks;

//TODO find a way to remove the windBindable without the WandOfTheForest complaining
public class ManalinkiumBlockEntity extends SpecialFlowerBlockEntity implements DimensionalFlowerWandBindable, WandBindable, ManaReceiver, KeyLocked {
    private static final String TAG_FLOWER_BINDING = "flowerBinding";
    private static final String TAG_DIMENSION_ID = "dimensionId";
    private static final String TAG_VALID_BINDING = "validBinding";
    private static final String TAG_VALID_STRUCTURE = "validStructure";
    private static final String TAG_MANA = "mana";
    private static final String TAG_INPUT_KEY = "inputKey";
    private static final String TAG_OUTPUT_KEY = "outputKey";
    private static final double MANA_COST = 0.15; // 15% of the receiving mana is used as cost

    public static final BlockPos[] LIVINGWOOD_LOCATIONS = {
            new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0), new BlockPos(0, -1, 1), new BlockPos(0, -1, -1),
    };

    public static final BlockPos[] GLIMMERING_LIVINGWOOD_LOCATIONS = {
            new BlockPos(1, -1, 1), new BlockPos(1, -1, -1), new BlockPos(-1, -1, 1), new BlockPos(-1, -1, -1),
    };

    protected @Nullable BlockPos flowerBindingPos = null;
    protected @Nullable String flowerBindingDimensionId = null;
    protected boolean isValidFlowerBinding = false;
    protected boolean isValidStructure = false;
    private int mana;
    public int redstoneSignal = 0;
    private String inputKey = "";
    private String outputKey = "";

    public ManalinkiumBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.MANALINKIUM, pos, state);
    }

    @Override
    protected void tickFlower() {
        super.tickFlower();

        redstoneSignal = 0;
        for (Direction dir : Direction.values()) {
            int redstoneSide = getLevel().getSignal(getBlockPos().relative(dir), dir);
            redstoneSignal = Math.max(redstoneSignal, redstoneSide);
        }

        if (getLevel().isClientSide) {
            double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
            int color = getColor();
            float red = (color >> 16 & 0xFF) / 255F;
            float green = (color >> 8 & 0xFF) / 255F;
            float blue = (color & 0xFF) / 255F;
            if (Math.random() > particleChance) {
                BotaniaAPI.instance().sparkleFX(getLevel(), getBlockPos().getX() + 0.3 + Math.random() * 0.5, getBlockPos().getY() + 0.5 + Math.random() * 0.5, getBlockPos().getZ() + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
            }
        }

        if (ticksExisted == 1 && getLevel() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = new ChunkPos(getBlockPos());
            serverLevel.setChunkForced(chunkPos.x, chunkPos.z, true);
        }

        if (!getLevel().isClientSide && isValidFlower()) {
            boolean oldValid = isValidStructure;
            boolean newValid = haveValidStructure();
            if (oldValid && !newValid) {
                ManalinkiumBlockEntity manalink = getBlockFlower();
                this.mana = getMana() / 2;
                setChanged();
                sync();
                manalink.mana = manalink.getMana() / 2;
                manalink.setChanged();
                manalink.sync();
            } else if (!oldValid && newValid) {
                ManalinkiumBlockEntity manalink = getBlockFlower();
                int mana = Math.min(manalink.getMana() + getMana(), getMaxMana());
                manalink.mana = mana;
                manalink.setChanged();
                manalink.sync();
                this.mana = mana;
                setChanged();
                sync();
            } else
                haveValidStructure();
        }


        if (!getLevel().isClientSide)
            emptyManaIntoCollector();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (getLevel() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = new ChunkPos(getBlockPos());
            serverLevel.setChunkForced(chunkPos.x, chunkPos.z, true);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (getLevel() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = new ChunkPos(getBlockPos());
            serverLevel.setChunkForced(chunkPos.x, chunkPos.z, false);
        }
    }

    public void emptyManaIntoCollector() {
        if (getMana() > 0)
            for (Direction dir : Direction.values()) {
                if (getLevel().getBlockEntity(getBlockPos().relative(dir)) instanceof ManaCollector manaCollector && !manaCollector.isFull()) {
                    int mana = Math.min(getMana(), manaCollector.getMaxMana() - manaCollector.getCurrentMana());
                    manaCollector.receiveMana(mana);
                    if (isValidFlowerBinding && isValidStructure)
                        addMana(-mana);
                    else {
                        this.mana -= mana;
                        setChanged();
                    }
                    sync();
                }
            }
    }

    public boolean haveValidStructure() {
        isValidStructure = true;
        for (BlockPos o : LIVINGWOOD_LOCATIONS)
            if (getLevel().getBlockState(getBlockPos().offset(o)).getBlock() != BotaniaBlocks.livingwood)
                isValidStructure = false;
        for (BlockPos o : GLIMMERING_LIVINGWOOD_LOCATIONS)
            if (getLevel().getBlockState(getBlockPos().offset(o)).getBlock() != BotaniaBlocks.livingwoodGlimmering)
                isValidStructure = false;

        ManalinkiumBlockEntity manalink = getBlockFlower();
        Level world = manalink.getLevel();

        for (BlockPos o : LIVINGWOOD_LOCATIONS)
            if (world.getBlockState(manalink.getBlockPos().offset(o)).getBlock() != BotaniaBlocks.livingwood)
                isValidStructure = false;
        for (BlockPos o : GLIMMERING_LIVINGWOOD_LOCATIONS)
            if (world.getBlockState(manalink.getBlockPos().offset(o)).getBlock() != BotaniaBlocks.livingwoodGlimmering)
                isValidStructure = false;

        manalink.isValidStructure = isValidStructure;
        manalink.setChanged();
        getLevel().sendBlockUpdated(manalink.getBlockPos(), manalink.getBlockState(), manalink.getBlockState(), Block.UPDATE_CLIENTS);
        getLevel().sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        setChanged();
        return isValidStructure;
    }

    public void syncMana(int mana) {
        this.mana = mana;
        setChanged();
        sync();
    }

    public int getMana() {
        return mana;
    }

    public void addMana(int mana) {
        if (!getLevel().isClientSide) {
            this.mana = Mth.clamp(this.mana + mana, 0, getMaxMana());
            setChanged();
            sync();
            getBlockFlower().syncMana(this.mana);
        }
    }

    public int getMaxMana() {
        return 10000;
    }

    public int getColor() {
        return 0x00FFFF;
    }

    public ItemStack getFlowerIcon() {
        return new ItemStack(MegaBotanyFlowerBlocks.manalinkium);
    }

    public ManalinkiumBlockEntity getBlockFlower() {
        if (flowerBindingPos == null || flowerBindingDimensionId == null || flowerBindingDimensionId.isEmpty()) {
            return null;
        }
        Level dimension = getLevel().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(flowerBindingDimensionId)));
        BlockEntity be = dimension.getBlockEntity(flowerBindingPos);
        return be instanceof ManalinkiumBlockEntity mbe ? mbe : null;
    }

    public boolean isValidFlower() {
        ManalinkiumBlockEntity be = getBlockFlower();
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

    public boolean isValid() {
        return isValidFlowerBinding && isValidStructure;
    }

    public @Nullable ManalinkiumBlockEntity findBindCandidateAt(BlockPos pos, String dimensionID) {
        if (level == null || pos == null || dimensionID == null) {
            return null;
        }

        Level dimension = level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionID)));

        if (dimension == null)
            return null;

        BlockEntity be = dimension.getBlockEntity(pos);
        return be instanceof ManalinkiumBlockEntity mbe ? mbe : null;
    }

    @Override
    public boolean bindTo(Player player, ItemStack wand, BlockPos pos, String dimensionId, Direction side) {
        if (findBindCandidateAt(pos, dimensionId) != null && !isValidFlower() || ((!pos.equals(flowerBindingPos) || (flowerBindingDimensionId == null || !flowerBindingDimensionId.equals(dimensionId)))) && !pos.equals(getEffectivePos())) {
            if (flowerBindingPos != null && isValidFlowerBinding)
                this.mana = 0;

            this.flowerBindingPos = pos;
            this.flowerBindingDimensionId = dimensionId;
            this.isValidFlowerBinding = true;

            ManalinkiumBlockEntity manalink = getBlockFlower();
            if (manalink.flowerBindingPos == null || !manalink.isValidFlowerBinding || !manalink.flowerBindingPos.equals(getBlockPos()) || manalink.flowerBindingDimensionId == null || !manalink.flowerBindingDimensionId.equals(getLevel().dimension().location().toString())) {
                manalink.bindTo(player, wand, getBlockPos(), getLevel().dimension().location().toString(), side);
            } else
                return true;


            int mana = Math.min(manalink.getMana() + getMana(), getMaxMana());
            manalink.mana = mana;
            manalink.setChanged();
            manalink.sync();
            this.mana = mana;
            setChanged();
            sync();

            return true;
        }
        return false;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return null;
    }

    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putInt(TAG_MANA, mana);
        if (flowerBindingPos != null && flowerBindingDimensionId != null) {
            CompoundTag binding = NbtUtils.writeBlockPos(flowerBindingPos);
            binding.putString(TAG_DIMENSION_ID, flowerBindingDimensionId);
            binding.putBoolean(TAG_VALID_BINDING, isValidFlowerBinding);
            cmp.put(TAG_FLOWER_BINDING, binding);
        }
        cmp.putBoolean(TAG_VALID_STRUCTURE, isValidStructure);
        cmp.putString(TAG_INPUT_KEY, inputKey);
        cmp.putString(TAG_OUTPUT_KEY, outputKey);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        mana = cmp.getInt(TAG_MANA);

        CompoundTag binding = cmp.getCompound(TAG_FLOWER_BINDING);
        flowerBindingPos = NbtUtils.readBlockPos(binding);
        flowerBindingDimensionId = binding.getString(TAG_DIMENSION_ID);
        isValidFlowerBinding = binding.getBoolean(TAG_VALID_BINDING);

        isValidStructure = cmp.getBoolean(TAG_VALID_STRUCTURE);
        if (cmp.contains(TAG_INPUT_KEY)) {
            inputKey = cmp.getString(TAG_INPUT_KEY);
        }
        if (cmp.contains(TAG_OUTPUT_KEY)) {
            outputKey = cmp.getString(TAG_OUTPUT_KEY);
        }
    }

    //-------------------------------------------------------------------------------------
    // aren't used only here for the wand of the forest to not complain
    @Override
    public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public @Nullable BlockPos getBinding() {
        return null;
    }
    //-------------------------------------------------------------------------------------

    @Override
    public Level getManaReceiverLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return getBlockPos();
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public boolean isFull() {
        return mana >= getMaxMana();
    }

    @Override
    public void receiveMana(int mana) {
        if (mana < getMaxMana()) {
            addMana((int) (mana * (1 - MANA_COST)));
            sync();
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    @Override
    public String getInputKey() {
        return inputKey;
    }

    @Override
    public String getOutputKey() {
        return outputKey;
    }

    public static class WandHud<F extends ManalinkiumBlockEntity> implements WandHUD {
        protected final F flower;

        public WandHud(F flower) {
            this.flower = flower;
        }

        public void renderHUD(GuiGraphics gui, Minecraft mc, int minLeft, int minRight, int minDown) {
            String name = I18n.get(flower.getBlockState().getBlock().getDescriptionId());
            int color = flower.getColor();

            int centerX = mc.getWindow().getGuiScaledWidth() / 2;
            int centerY = mc.getWindow().getGuiScaledHeight() / 2;
            int left = (Math.max(102, mc.font.width(name)) + 4) / 2;
            // padding + item
            int right = left + 20;

            left = Math.max(left, minLeft);
            right = Math.max(right, minRight);

            RenderHelper.renderHUDBox(gui, centerX - left, centerY + 8, centerX + right, centerY + Math.max(30, minDown));

            BotaniaAPIClient.instance().drawComplexManaHUD(gui, color, flower.getMana(), flower.getMaxMana(),
                    name, flower.getFlowerIcon(), flower.isValidFlowerBinding);
        }

        @Override
        public void renderHUD(GuiGraphics gui, Minecraft mc) {
            renderHUD(gui, mc, 0, 0, 0);
        }
    }
}

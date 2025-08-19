package io.github.megadoxs.megabotany.common.block.block_entity;

import com.google.common.base.Suppliers;
import io.github.megadoxs.megabotany.common.advancements.SpiritPortalTrigger;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlockEntities;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.SpiritTradeRecipe;
import io.github.megadoxs.megabotany.common.mixin.PylonBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.TriPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class SpiritPortalBlockEntity extends BotaniaBlockEntity implements Wandable {
    public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() -> {
        record Matcher(TagKey<Block> tag, Direction.Axis displayedRotation, Block defaultBlock) implements IStateMatcher {
            @Override
            public BlockState getDisplayedState(long ticks) {
                var blocks = StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(this.tag).spliterator(), false)
                        .map(Holder::value)
                        .toList();
                if (blocks.isEmpty()) {
                    return Blocks.BEDROCK.defaultBlockState();
                }

                BlockState block = blocks.contains(defaultBlock)
                        ? defaultBlock.defaultBlockState()
                        : blocks.get((int) ((ticks / 20) % blocks.size())).defaultBlockState();

                return block.hasProperty(BlockStateProperties.AXIS)
                        ? block.setValue(BlockStateProperties.AXIS, displayedRotation())
                        : block;
            }

            @Override
            public TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate() {
                return (blockGetter, pos, state) -> state.is(tag());
            }
        }
        var horizontal = new Matcher(BotaniaTags.Blocks.DREAMWOOD_LOGS, Direction.Axis.X, BotaniaBlocks.dreamwoodLog);
        var vertical = new Matcher(BotaniaTags.Blocks.DREAMWOOD_LOGS, Direction.Axis.Y, BotaniaBlocks.dreamwoodLog);
        var horizontalGlimmer = new Matcher(BotaniaTags.Blocks.DREAMWOOD_LOGS_GLIMMERING, Direction.Axis.X, BotaniaBlocks.dreamwoodLogGlimmering);
        var verticalGlimmer = new Matcher(BotaniaTags.Blocks.DREAMWOOD_LOGS_GLIMMERING, Direction.Axis.Y, BotaniaBlocks.dreamwoodLogGlimmering);

        return PatchouliAPI.get().makeMultiblock(
                new String[][]{
                        {"_", "w", "g", "w", "_"},
                        {"W", " ", " ", " ", "W"},
                        {"G", " ", " ", " ", "G"},
                        {"W", " ", " ", " ", "W"},
                        {"_", "w", "0", "w", "_"}
                },
                'W', vertical,
                'w', horizontal,
                'G', verticalGlimmer,
                'g', horizontalGlimmer,
                '0', MegaBotanyBlocks.SPIRIT_PORTAL.get()
        );
    });

    public static final int MANA_COST = 500;
    public static final int MANA_COST_OPENING = 200000;
    public static final int MIN_REQUIRED_PYLONS = 2;
    private static final String TAG_TICKS_OPEN = "ticksOpen";
    private static final String TAG_MODE = "mode";
    private static final String TAG_TICKS_SINCE_LAST_ITEM = "ticksSinceLastItem";
    private static final String TAG_STACK_COUNT = "stackCount";
    private static final String TAG_STACK = "portalStack";

    private final List<ItemStack> stacksIn = new ArrayList<>();
    private final List<BlockPos> cachedPylonPositions = new ArrayList<>();

    public int ticksOpen = 0;
    public boolean dayMode;
    private int ticksSinceLastItem = 0;
    private boolean closeNow = false;

    //some of these probably need to be saved in nbt
    public float spriteAlpha;

    private int fadeTimer = 0;
    private boolean shouldFade = false;
    private static final int FADE_DURATION = 120;
    private boolean hasSwitchedSprite = false;

    public SpiritPortalBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyBlockEntities.SPIRIT_PORTAL.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos worldPosition, BlockState blockState, SpiritPortalBlockEntity self) {
        AlfheimPortalState state = blockState.getValue(BotaniaStateProperties.ALFPORTAL_STATE);
        float time = level.getDayTime() % 24000; //day = 0 && night = 13000

        if (state == AlfheimPortalState.OFF) {
            self.ticksOpen = 0;
            return;
        }
        AlfheimPortalState newState = self.getValidState(state);

        self.ticksOpen++;

        if (self.ticksOpen == 1) {
            self.dayMode = time >= 13000;
            self.spriteAlpha = 1;
        }

        if (self.ticksOpen > 0) {
            if (time == 12940 || time == 23940) {
                self.shouldFade = true;
                self.fadeTimer = 0;
            } else if (((time < 13000 && !self.dayMode) || (time >= 13000 && self.dayMode)) && !self.shouldFade) {
                self.dayMode = !self.dayMode;
            }

            if (self.shouldFade) {
                float progress = self.fadeTimer / (float) FADE_DURATION;
                self.spriteAlpha = (float) (0.5 * (1 + Math.cos(progress * Math.PI)));

                self.fadeTimer++;

                if (!self.hasSwitchedSprite && progress >= 0.5f) {
                    self.hasSwitchedSprite = true;
                    self.dayMode = !self.dayMode;
                }

                if (self.fadeTimer >= FADE_DURATION) {
                    self.shouldFade = false;
                    self.fadeTimer = 0;
                    self.hasSwitchedSprite = false;
                    self.spriteAlpha = 1;
                }
            }
        }

        AABB aabb = self.getPortalAABB(state);

        if (self.ticksOpen > 60) {
            self.ticksSinceLastItem++;
            if (level.isClientSide && BotaniaConfig.client().elfPortalParticlesEnabled()) { //ok to use, but should be made in its own config eventually
                self.blockParticle(state);
            }

            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, aabb);
            if (!level.isClientSide) {
                for (ItemEntity item : items) {
                    if (!item.isAlive()) {
                        continue;
                    }

                    ItemStack stack = item.getItem();
                    if (XplatAbstractions.INSTANCE.itemFlagsComponent(item).elvenPortalSpawned) {
                        continue;
                    }

                    item.discard();
                    if (self.validateItemUsage(item)) {
                        self.addItem(stack);
                    }
                    self.ticksSinceLastItem = 0;
                }
            }

            if (!level.isClientSide && !self.stacksIn.isEmpty() && self.ticksSinceLastItem >= 4) {
                self.resolveRecipes(level);
            }
        }

        if (self.closeNow) {
            if (!level.isClientSide) {
                level.setBlockAndUpdate(worldPosition, MegaBotanyBlocks.SPIRIT_PORTAL.get().defaultBlockState());
            }
            for (int i = 0; i < 36; i++) {
                self.blockParticle(state);
            }
            self.closeNow = false;
        } else if (newState != state) {
            if (newState == AlfheimPortalState.OFF) {
                for (int i = 0; i < 36; i++) {
                    self.blockParticle(state);
                }
            }

            if (!level.isClientSide) {
                level.setBlockAndUpdate(worldPosition, blockState.setValue(BotaniaStateProperties.ALFPORTAL_STATE, newState));
            }
        }
    }

    private boolean validateItemUsage(ItemEntity entity) {
        ItemStack inputStack = entity.getItem();
        for (Recipe<?> recipe : BotaniaRecipeTypes.getRecipes(level, SpiritTradeRecipe.Type.INSTANCE).values()) {
            if (recipe instanceof SpiritTradeRecipe tradeRecipe && tradeRecipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(inputStack))) {
                return true;
            }
        }
        return false;
    }

    private void blockParticle(AlfheimPortalState state) {
        // Pick one of the inner positions, offsets [-1,+1] and [+1,+3]
        int rnd = level.random.nextInt(9);
        double dh = (rnd / 3) - 1;
        double dy = (rnd % 3) + 1;
        double dx = state == AlfheimPortalState.ON_X ? 0 : dh;
        double dz = state == AlfheimPortalState.ON_Z ? 0 : dh;

        float motionMul = 0.2F;
        WispParticleData data = WispParticleData.wisp((float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() * 0.25F + 0.75F), (float) (Math.random() * 0.25F + 0.5F), (float) (Math.random() * 0.25F + 0.75F));
        level.addParticle(data, getBlockPos().getX() + dx, getBlockPos().getY() + dy, getBlockPos().getZ() + dz, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
    }

    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        AlfheimPortalState state = getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE);
        if (state == AlfheimPortalState.OFF) {
            AlfheimPortalState newState = getValidState(state);
            if (newState != AlfheimPortalState.OFF) {
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.ALFPORTAL_STATE, newState));
                if (player instanceof ServerPlayer serverPlayer) {
                    SpiritPortalTrigger.INSTANCE.trigger(serverPlayer, serverPlayer.serverLevel(), getBlockPos(), stack);
                }
                return true;
            }
        }
        return false;
    }

    private AABB getPortalAABB(AlfheimPortalState state) {
        return state == AlfheimPortalState.ON_X
                ? new AABB(worldPosition.offset(0, 1, -1), worldPosition.offset(1, 4, 2))
                : new AABB(worldPosition.offset(-1, 1, 0), worldPosition.offset(2, 4, 1));
    }

    private void addItem(ItemStack stack) {
        int size = stack.getCount();
        stack.setCount(1);
        for (int i = 0; i < size; i++) {
            stacksIn.add(stack.copy());
        }
    }

    private void resolveRecipes(Level level) {
        List<BlockPos> pylons = locatePylons(true);

        SimpleContainer container = new SimpleContainer(stacksIn.toArray(ItemStack[]::new));

        Optional<SpiritTradeRecipe> recipe = level.getRecipeManager().getRecipeFor(SpiritTradeRecipe.Type.INSTANCE, container, level);
        if (recipe.isPresent() && consumeMana(pylons, MANA_COST * recipe.get().getIngredients().size(), false)) {
            for (Ingredient ingredient : recipe.get().getIngredients()) {
                for (ItemStack ingredientStack : ingredient.getItems()) {
                    stacksIn.removeIf(stack -> ItemStack.isSameItem(stack, ingredientStack));
                }
            }

            if (recipe.get().getOutputs(dayMode).stream().anyMatch(ItemStack::isEmpty)) {
                level.setBlockAndUpdate(worldPosition, MegaBotanyBlocks.SPIRIT_PORTAL.get().defaultBlockState());
                level.explode(null, worldPosition.getX() + .5, worldPosition.getY() + 2.0, worldPosition.getZ() + .5, 3f, Level.ExplosionInteraction.NONE);
                return;
            }

            for (ItemStack stack : recipe.get().getOutputs(dayMode)) {
                spawnItem(stack);
            }
        }
    }

    private void spawnItem(ItemStack stack) {
        ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, stack);
        XplatAbstractions.INSTANCE.itemFlagsComponent(item).elvenPortalSpawned = true;
        level.addFreshEntity(item);
        ticksSinceLastItem = 0;
    }

    @Override
    public void saveAdditional(CompoundTag cmp) {
        super.saveAdditional(cmp);

        cmp.putInt(TAG_STACK_COUNT, stacksIn.size());
        int i = 0;
        for (ItemStack stack : stacksIn) {
            CompoundTag stackcmp = stack.save(new CompoundTag());
            cmp.put(TAG_STACK + i, stackcmp);
            i++;
        }
    }

    @Override
    public void load(@NotNull CompoundTag cmp) {
        super.load(cmp);

        int count = cmp.getInt(TAG_STACK_COUNT);
        stacksIn.clear();
        for (int i = 0; i < count; i++) {
            CompoundTag stackcmp = cmp.getCompound(TAG_STACK + i);
            ItemStack stack = ItemStack.of(stackcmp);
            stacksIn.add(stack);
        }
    }

    @Override
    public void writePacketNBT(CompoundTag cmp) {
        cmp.putInt(TAG_TICKS_OPEN, ticksOpen);
        cmp.putBoolean(TAG_MODE, dayMode);
        cmp.putInt(TAG_TICKS_SINCE_LAST_ITEM, ticksSinceLastItem);
    }

    @Override
    public void readPacketNBT(CompoundTag cmp) {
        ticksOpen = cmp.getInt(TAG_TICKS_OPEN);
        dayMode = cmp.getBoolean(TAG_MODE);
        spriteAlpha = 1;
        ticksSinceLastItem = cmp.getInt(TAG_TICKS_SINCE_LAST_ITEM);
    }

    private static Rotation getStateRotation(AlfheimPortalState state) {
        return switch (state) {
            case ON_X -> Rotation.CLOCKWISE_90;
            case ON_Z -> Rotation.NONE;
            default -> null;
        };
    }

    private AlfheimPortalState getValidState(AlfheimPortalState oldState) {
        Rotation rot;
        if (oldState != AlfheimPortalState.OFF) {
            Rotation oldRot = getStateRotation(oldState);
            if (!MULTIBLOCK.get().validate(level, getBlockPos(), oldRot)) {
                return AlfheimPortalState.OFF;
            }
            rot = oldRot;
        } else {
            rot = MULTIBLOCK.get().validate(level, getBlockPos());
        }
        if (rot == null) {
            return AlfheimPortalState.OFF;
        }

        lightPylons();
        return switch (rot) {
            case NONE, CLOCKWISE_180 -> AlfheimPortalState.ON_Z;
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> AlfheimPortalState.ON_X;
        };
    }

    public List<BlockPos> locatePylons(boolean rescanNow) {
        if (!rescanNow && cachedPylonPositions.size() >= MIN_REQUIRED_PYLONS) {
            List<BlockPos> cachedResult = new ArrayList<>();
            for (BlockPos pos : cachedPylonPositions) {
                if (isValidPylonPosition(pos)) {
                    cachedResult.add(pos);
                }
            }
            if (cachedResult.size() >= MIN_REQUIRED_PYLONS) {
                return cachedResult;
            }

            // not enough valid cached pylons, scan again
        }

        int range = 5;
        List<BlockPos> result = new ArrayList<>();

        for (BlockPos pos : BlockPos.betweenClosed(getBlockPos().offset(-range, -range, -range),
                getBlockPos().offset(range, range, range))) {
            if (isValidPylonPosition(pos)) {
                result.add(pos.immutable());
            }
        }

        cachedPylonPositions.clear();
        cachedPylonPositions.addAll(result);

        return result;
    }

    private boolean isValidPylonPosition(BlockPos pos) {
        return getLevel().hasChunkAt(pos)
                && getLevel().getBlockState(pos).is(BotaniaBlocks.gaiaPylon)
                && getLevel().getBlockState(pos.below()).getBlock() instanceof ManaPoolBlock;
    }

    public void lightPylons() {
        if (ticksOpen < 50) {
            return;
        }

        boolean finishOpening = ticksOpen == 50;
        List<BlockPos> pylons = locatePylons(finishOpening);
        for (BlockPos pos : pylons) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof PylonBlockEntityAccessor pylon) {
                pylon.setActivated(true);
                pylon.setCenterPos(getBlockPos());
            }
        }

        if (finishOpening) {
            consumeMana(pylons, MANA_COST_OPENING, true);
        }
    }

    public boolean consumeMana(List<BlockPos> pylons, int totalCost, boolean close) {
        List<ManaPoolBlockEntity> consumePools = new ArrayList<>();
        int consumed = 0;

        if (pylons.size() < MIN_REQUIRED_PYLONS) {
            closeNow = true;
            return false;
        }

        int costPer = Math.max(1, totalCost / pylons.size());
        int expectedConsumption = costPer * pylons.size();

        for (BlockPos pos : pylons) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof PylonBlockEntityAccessor pylon) {
                pylon.setActivated(true);
                pylon.setCenterPos(getBlockPos());
            }

            tile = level.getBlockEntity(pos.below());
            if (tile instanceof ManaPoolBlockEntity pool) {
                if (pool.getCurrentMana() < costPer) {
                    closeNow = closeNow || close;
                    return false;
                } else if (!level.isClientSide) {
                    consumePools.add(pool);
                    consumed += costPer;
                }
            }
        }

        if (consumed >= expectedConsumption) {
            for (ManaPoolBlockEntity pool : consumePools) {
                pool.receiveMana(-costPer);
                pool.craftingEffect(false);
            }
            return true;
        }

        return false;
    }
}

package io.github.megadoxs.megabotany.common.entity;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.helper.GaiaArenaHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;

import java.util.*;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GaiaGuardianIII extends Monster implements IEntityAdditionalSpawnData {
    protected GaiaGuardianIII(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final TagKey<Block> BLACKLIST = BotaniaTags.Blocks.GAIA_BREAK_BLACKLIST;
    private final ServerBossEvent bossInfo = (ServerBossEvent) new ServerBossEvent(MegaBotanyEntities.GAIA_GUARDIAN_III.get().getDescription(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS).setCreateWorldFog(true);
    private final UUID bossInfoUUID = bossInfo.getId();
    private int playerCount;
    private final ArrayList<UUID> playerUUIDS = new ArrayList<>();
    private BlockPos source = ManaBurst.NO_SOURCE;
    private static final float MAX_HP = 320F;
    private static final int DAMAGE_CAP = 32;
    private static final String TAG_SOURCE_X = "sourceX";
    private static final String TAG_SOURCE_Y = "sourceY";
    private static final String TAG_SOURCE_Z = "sourcesZ";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_PLAYER_COUNT = "player_count";

    @SuppressWarnings("deprecation")
    public static boolean spawn(Player player, ItemStack stack, Level world, BlockPos pos) {
        if (GaiaArenaHelper.shouldSpawn(player, world, pos) && !(countGaiaGuardiansIIIAround(world, pos) > 0) && !world.isClientSide) {
            stack.shrink(1);

            GaiaGuardianIII e = MegaBotanyEntities.GAIA_GUARDIAN_III.get().create(world);
            e.setPos(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            e.source = pos;
            player.sendSystemMessage(Component.literal(pos.toString()));

            List<Player> playersAround = GaiaArenaHelper.getPlayersInsideArena(world, pos);
            e.playerUUIDS.addAll(playersAround.stream().map(Player::getUUID).toList());
            int playerCount = playersAround.size();
            e.playerCount = playerCount;

            float healthMultiplier = 1;
            if (playerCount > 1) {
                healthMultiplier += playerCount * 0.25F;
            }
            e.getAttribute(Attributes.MAX_HEALTH).setBaseValue(MAX_HP * healthMultiplier);
            e.setHealth(e.getMaxHealth());

            e.playSound(BotaniaSounds.gaiaSummon, 1F, 1F);
            e.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.EVENT, null, null);
            world.addFreshEntity(e);


            for (Player nearbyPlayer : playersAround) {
                if (nearbyPlayer instanceof ServerPlayer serverPlayer)
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, e);
            }
            return true;
        } else
            return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, GaiaArenaHelper.ARENA_RANGE * 1.5F));
    }

//    @Override
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        entityData.define(INVUL_TIME, 0);
//    }

    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
//        cmp.putInt(TAG_INVUL_TIME, getInvulTime());
//        cmp.putBoolean(TAG_AGGRO, aggro);
//
        cmp.putInt(TAG_SOURCE_X, source.getX());
        cmp.putInt(TAG_SOURCE_Y, source.getY());
        cmp.putInt(TAG_SOURCE_Z, source.getZ());

        ListTag playerTagList = new ListTag();
        for (UUID playerUUID : playerUUIDS) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("PlayerUUID", playerUUID);
            playerTagList.add(playerTag);
        }
        cmp.put(TAG_PLAYERS, playerTagList);
        //cmp.put(TAG_PLAYER_COUNT, playerTagList.size());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
//        aggro = cmp.getBoolean(TAG_AGGRO);
//        mobSpawnTicks = cmp.getInt(TAG_MOB_SPAWN_TICKS);
//
        int x = cmp.getInt(TAG_SOURCE_X);
        int y = cmp.getInt(TAG_SOURCE_Y);
        int z = cmp.getInt(TAG_SOURCE_Z);
        source = new BlockPos(x, y, z);

//        if (cmp.contains(TAG_PLAYER_COUNT)) {
//            playerCount = cmp.getInt(TAG_PLAYER_COUNT);
//        } else {
//            playerCount = 1;
//        }

        ListTag playerTagList = cmp.getList(TAG_PLAYERS, 10);
        for (int i = 0; i < playerTagList.size(); i++) {
            CompoundTag playerTag = playerTagList.getCompound(i);
            UUID playerUUID = playerTag.getUUID("PlayerUUID");
            playerUUIDS.add(playerUUID);
        }

        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossInfo.removePlayer(player);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (tickCount % 20 == 5)
            spawnMissile();
    }

    private void spawnMissile() {
        Missile missile = new Missile(this, 0);
        missile.setPos(getX() + (Math.random() - 0.5 * 0.1), getY() + 2.4 + (Math.random() - 0.5 * 0.1), getZ() + (Math.random() - 0.5 * 0.1));
        if (missile.findTarget()) {
            playSound(BotaniaSounds.missile, 1F, 0.8F + (float) Math.random() * 0.2F);
            level().addFreshEntity(missile);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            GaiaArenaHelper.SpawnArenaParticle(level(), source);
            Player player = Proxy.INSTANCE.getClientPlayer();
            if (GaiaArenaHelper.getPlayersInsideArena(level(), source).contains(player)) {
                player.getAbilities().flying &= player.getAbilities().instabuild;
            }
            return;
        }
        bossInfo.setProgress(getHealth() / getMaxHealth());

        List<Player> players = GaiaArenaHelper.getPlayersInsideArena(level(), source);
        List<Player> validPlayers = playerUUIDS.stream().map(uuid -> level().getPlayerByUUID(uuid)).filter(Objects::nonNull).toList();
        List<Entity> entities = GaiaArenaHelper.getEntitiesInsideArena(level(), source);
        players.removeIf(player -> !validPlayers.contains(player));
        entities.removeAll(validPlayers);
        playerUUIDS.removeIf(uuid -> players.stream().noneMatch(player -> player.getUUID().equals(uuid)));

        if (players.isEmpty() && !level().players().isEmpty())
            discard();
        else {
            for (Player player : players) {
                GaiaArenaHelper.keepPlayerInsideArena(player, source);
                player.getAbilities().flying &= player.getAbilities().instabuild;
            }

            for (Entity entity : entities) {
                GaiaArenaHelper.keepEntityOutsideArena(entity, source);
            }
        }


        // move all of that to gaia helper
        for (Player nearbyPlayer : players) {
            if (nearbyPlayer instanceof ServerPlayer serverPlayer) {
                Inventory playerInv = serverPlayer.getInventory(); // minecraft inventory
                LazyOptional<ICuriosItemHandler> playerCurio = CuriosApi.getCuriosInventory(serverPlayer); // curio inventory

                // I feel like there is probably a better way to do all this
                for (int i = 0; i < playerInv.items.size(); i++) {
                    if (!playerInv.items.get(i).isEmpty() && !GaiaArenaHelper.mods.contains(BuiltInRegistries.ITEM.getKey(playerInv.items.get(i).getItem()).getNamespace())) {
                        serverPlayer.drop(playerInv.items.get(i), true, true).setPickUpDelay(10000); // TODO can't stay like that item will despawn before it can be pickup.
                        playerInv.items.set(i, ItemStack.EMPTY);
                    }
                }

                for (int i = 0; i < playerInv.armor.size(); i++) {
                    if (!playerInv.armor.get(i).isEmpty() & !GaiaArenaHelper.mods.contains(BuiltInRegistries.ITEM.getKey(playerInv.items.get(i).getItem()).getNamespace())) {
                        serverPlayer.drop(playerInv.armor.get(i), true, true).setPickUpDelay(10000);
                        playerInv.armor.set(i, ItemStack.EMPTY);
                    }
                }

                for (int i = 0; i < playerInv.offhand.size(); i++) {
                    if (!playerInv.offhand.get(i).isEmpty() & !GaiaArenaHelper.mods.contains(BuiltInRegistries.ITEM.getKey(playerInv.items.get(i).getItem()).getNamespace())) {
                        serverPlayer.drop(playerInv.offhand.get(i), true, true).setPickUpDelay(10000);
                        playerInv.offhand.set(i, ItemStack.EMPTY);
                    }
                }

                playerCurio.resolve().get().getCurios().forEach((slot, itemStackHandler) -> {
                    for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                        ItemStack curioItem = itemStackHandler.getStacks().getStackInSlot(i);
                        if (!curioItem.isEmpty() && !BuiltInRegistries.ITEM.getKey(curioItem.getItem()).getNamespace().equals("minecraft")) {
                            ItemEntity item = new ItemEntity(serverPlayer.level(), serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), curioItem);
                            item.setPickUpDelay(10000);
                            serverPlayer.level().addFreshEntity(item);

                            itemStackHandler.getStacks().setStackInSlot(i, ItemStack.EMPTY);
                        }
                    }
                });
            }
        }
    }

    private void teleportRandomly() {
        //choose a location to teleport to
        double oldX = getX(), oldY = getY(), oldZ = getZ();
        double newX, newY = source.getY(), newZ;
        int tries = 0;

        do {
            newX = source.getX() + (random.nextDouble() - .5) * GaiaArenaHelper.ARENA_RANGE;
            newZ = source.getZ() + (random.nextDouble() - .5) * GaiaArenaHelper.ARENA_RANGE;
            tries++;
            //ensure it's inside the arena ring, and not just its bounding square
        } while (tries < 50 && MathHelper.pointDistanceSpace(newX, newY, newZ, source.getX(), source.getY(), source.getZ()) > 12);

        if (tries == 50) {
            //failsafe: teleport to the beacon
            newX = source.getX() + .5;
            newY = source.getY() + 1.6;
            newZ = source.getZ() + .5;
        }

        //for low-floor arenas, ensure landing on the ground
        BlockPos tentativeFloorPos = BlockPos.containing(newX, newY - 1, newZ);
        if (level().getBlockState(tentativeFloorPos).getCollisionShape(level(), tentativeFloorPos).isEmpty()) {
            newY--;
        }

        //teleport there
        teleportTo(newX, newY, newZ);

        //play sound
        level().playSound(null, oldX, oldY, oldZ, BotaniaSounds.gaiaTeleport, this.getSoundSource(), 1F, 1F);
        this.playSound(BotaniaSounds.gaiaTeleport, 1F, 1F);

        var random = getRandom();

        //spawn particles along the path
        int particleCount = 128;
        for (int i = 0; i < particleCount; ++i) {
            double progress = i / (double) (particleCount - 1);
            float vx = (random.nextFloat() - 0.5F) * 0.2F;
            float vy = (random.nextFloat() - 0.5F) * 0.2F;
            float vz = (random.nextFloat() - 0.5F) * 0.2F;
            double px = oldX + (newX - oldX) * progress + (random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
            double py = oldY + (newY - oldY) * progress + random.nextDouble() * getBbHeight();
            double pz = oldZ + (newZ - oldZ) * progress + (random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
            level().addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
        }

        Vec3 oldPosVec = new Vec3(oldX, oldY + getBbHeight() / 2, oldZ);
        Vec3 newPosVec = new Vec3(newX, newY + getBbHeight() / 2, newZ);

        if (oldPosVec.distanceToSqr(newPosVec) > 1) {
            //damage players in the path of the teleport
            for (Player player : GaiaArenaHelper.getPlayersInsideArena(level(), source)) {
                boolean hit = player.getBoundingBox().inflate(0.25).clip(oldPosVec, newPosVec)
                        .isPresent();
                if (hit) {
                    player.hurt(damageSources().mobAttack(this), 6);
                }
            }

            //break blocks in the path of the teleport
            int breakSteps = (int) oldPosVec.distanceTo(newPosVec);
            if (breakSteps >= 2) {
                for (int i = 0; i < breakSteps; i++) {
                    float progress = i / (float) (breakSteps - 1);
                    int breakX = Mth.floor(oldX + (newX - oldX) * progress);
                    int breakY = Mth.floor(oldY + (newY - oldY) * progress);
                    int breakZ = Mth.floor(oldZ + (newZ - oldZ) * progress);

                    smashBlocksAround(breakX, breakY, breakZ, 1);
                }
            }
        }
    }

    private void smashBlocksAround(int centerX, int centerY, int centerZ, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius + 1; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int x = centerX + dx;
                    int y = centerY + dy;
                    int z = centerZ + dz;

                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level().getBlockState(pos);
                    Block block = state.getBlock();

                    if (state.getDestroySpeed(level(), pos) == -1) {
                        continue;
                    }

                    if (CHEATY_BLOCKS.contains(BuiltInRegistries.BLOCK.getKey(block))) {
                        level().destroyBlock(pos, true);
                    } else {
                        //don't break blacklisted blocks
                        if (state.is(BLACKLIST)) {
                            continue;
                        }
                        //don't break the floor
                        if (y < source.getY()) {
                            continue;
                        }
                        //don't break blocks in pylon columns
                        if (Math.abs(source.getX() - x) == 4 && Math.abs(source.getZ() - z) == 4) {
                            continue;
                        }

                        level().destroyBlock(pos, true);
                    }
                }
            }
        }
    }

    // TODO can't I just prevent the player from posing block and make all the arena block unbreakable?
    private static final List<ResourceLocation> CHEATY_BLOCKS = Arrays.asList(
            new ResourceLocation("openblocks", "beartrap"),
            new ResourceLocation("thaumictinkerer", "magnet")
    );

    @Override
    protected void actuallyHurt(@NotNull DamageSource source, float amount) {
        super.actuallyHurt(source, Math.min(DAMAGE_CAP, amount));

        Entity attacker = source.getDirectEntity();
        if (attacker != null) {
            Vec3 thisVector = VecHelper.fromEntityCenter(this);
            Vec3 playerVector = VecHelper.fromEntityCenter(attacker);
            Vec3 motionVector = thisVector.subtract(playerVector).normalize().scale(0.75);

            if (getHealth() > 0) {
                setDeltaMovement(-motionVector.x, 0.5, -motionVector.z);
                // TODO implement the random tp after a while.
                //tpDelay = 4;
            }
        }
        invulnerableTime = Math.max(invulnerableTime, 20);
    }

    private static int countGaiaGuardiansIIIAround(Level world, BlockPos source) {
        List<GaiaGuardianIII> l = world.getEntitiesOfClass(GaiaGuardianIII.class, GaiaArenaHelper.getArenaBB(source));
        return l.size();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MegaBotanyEntities.GAIA_GUARDIAN_III.get(), createAttributes().build());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(source);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        source = friendlyByteBuf.readBlockPos();
    }

    @Override
    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        if (pEntity instanceof Player player)
            playerUUIDS.remove(player.getUUID());
        return super.killedEntity(pLevel, pEntity);
    }
}

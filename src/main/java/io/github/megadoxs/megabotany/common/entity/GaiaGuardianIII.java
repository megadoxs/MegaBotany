package io.github.megadoxs.megabotany.common.entity;

import io.github.megadoxs.megabotany.client.renderer.GaiaGuardianBossBarHandler;
import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.helper.GaiaArenaHelper;
import io.github.megadoxs.megabotany.common.mixin.InventoryAccessor;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.entity.MagicMissileEntity;
import vazkii.botania.common.entity.PixieEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static vazkii.botania.common.helper.PlayerHelper.isTruePlayer;

public class GaiaGuardianIII extends Monster implements IEntityAdditionalSpawnData {
    protected GaiaGuardianIII(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        if (pLevel.isClientSide) {
            GaiaGuardianBossBarHandler.bosses.add(this);
        }
    }

    private final ServerBossEvent bossInfo = (ServerBossEvent) new ServerBossEvent(MegaBotanyEntities.GAIA_GUARDIAN_III.get().getDescription(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS).setCreateWorldFog(true);
    private UUID bossInfoUUID = bossInfo.getId();
    private boolean spawnPixies = false;
    private int tpDelay = 0;
    private int mobWave = 0;
    private int playerCount;
    private int phase = 0;
    private int mobSpawnTickDelay = 20;
    public Player trueKiller = null;
    private BlockPos source = ManaBurst.NO_SOURCE;
    private final ArrayList<UUID> entityInsideUUIDS = new ArrayList<>();
    private final ArrayList<UUID> playerAttackerUUIDS = new ArrayList<>();
    private final ArrayList<UUID> summonedMobUUIDS = new ArrayList<>();
    private static final int SPAWN_TICKS = 160;
    private static final int DAMAGE_CAP = 32;
    private static final float MAX_HP = 500F;
    private static final String TAG_INVULNERABLE_TIME = "invulnerableTime";
    private static final String TAG_MOB_WAVE = "mobWave";
    private static final String TAG_SOURCE_X = "sourceX";
    private static final String TAG_SOURCE_Y = "sourceY";
    private static final String TAG_SOURCE_Z = "sourcesZ";
    private static final String TAG_ENTITIES_INSIDE = "entitiesInside";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_MOBS = "mobs";
    private static final String TAG_UUID = "uuid";
    private static final String TAG_PLAYER_COUNT = "playerCount";
    private static final String TAG_PHASE = "phase";
    private static final EntityDataAccessor<Integer> MOBS = SynchedEntityData.defineId(GaiaGuardianIII.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> INVULNERABLE_TIME = SynchedEntityData.defineId(GaiaGuardianIII.class, EntityDataSerializers.INT);

    @SuppressWarnings("deprecation")
    public static boolean spawn(Player player, ItemStack stack, Level world, BlockPos pos) {
        if (GaiaArenaHelper.shouldSpawn(player, world, pos) && !(countGaiaGuardiansIIIAround(world, pos) > 0) && !world.isClientSide) {
            stack.shrink(1);

            GaiaGuardianIII e = MegaBotanyEntities.GAIA_GUARDIAN_III.get().create(world);
            e.setPos(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            e.setInvulnerableTime(SPAWN_TICKS);
            e.setHealth(1F);
            e.source = pos;
            e.tpDelay = 30; //initial tpDelay

            List<Player> playersAround = GaiaArenaHelper.getPlayersInsideArena(world, pos);
            e.entityInsideUUIDS.addAll(playersAround.stream().map(Player::getUUID).toList());
            int playerCount = playersAround.size();
            e.playerCount = playerCount;

            float healthMultiplier = 1;
            if (playerCount > 1) {
                healthMultiplier += playerCount * 0.25F;
            }
            e.getAttribute(Attributes.MAX_HEALTH).setBaseValue(MAX_HP * healthMultiplier);
            e.getAttribute(Attributes.ARMOR).setBaseValue(20);

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
    public void kill() {
        this.setHealth(0.0F);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        Entity e = source.getEntity();
        if (e instanceof Player player && isTruePlayer(e) && getInvulnerableTime() == 0) {

            if (!playerAttackerUUIDS.contains(player.getUUID())) {
                playerAttackerUUIDS.add(player.getUUID());
            }

            return super.hurt(source, Math.min(DAMAGE_CAP, amount));
        }

        return false;
    }

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
                tpDelay = 4;
                spawnPixies = true;
            }
        }
        invulnerableTime = Math.max(invulnerableTime, 20);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, GaiaArenaHelper.ARENA_RANGE * 1.5F));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
        cmp.putInt(TAG_INVULNERABLE_TIME, getInvulnerableTime());
        cmp.putInt(TAG_MOB_WAVE, mobWave);
        cmp.putInt(TAG_PHASE, phase);

        cmp.putInt(TAG_SOURCE_X, source.getX());
        cmp.putInt(TAG_SOURCE_Y, source.getY());
        cmp.putInt(TAG_SOURCE_Z, source.getZ());

        ListTag entityTagList = new ListTag();
        for (UUID uuid : entityInsideUUIDS) {
            CompoundTag entityTag = new CompoundTag();
            entityTag.putUUID(TAG_UUID, uuid);
            entityTagList.add(entityTag);
        }
        cmp.put(TAG_ENTITIES_INSIDE, entityTagList);

        ListTag playerTagList = new ListTag();
        for (UUID uuid : playerAttackerUUIDS) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID(TAG_UUID, uuid);
            playerTagList.add(playerTag);
        }
        cmp.put(TAG_PLAYERS, playerTagList);

        ListTag mobTagList = new ListTag();
        for (UUID uuid : summonedMobUUIDS) {
            CompoundTag mobTag = new CompoundTag();
            mobTag.putUUID(TAG_UUID, uuid);
            mobTagList.add(mobTag);
        }
        cmp.put(TAG_MOBS, mobTagList);

        cmp.putInt(TAG_PLAYER_COUNT, playerCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
        setInvulnerableTime(cmp.getInt(TAG_INVULNERABLE_TIME));
        mobWave = cmp.getInt(TAG_MOB_WAVE);
        phase = cmp.getInt(TAG_PHASE);

        int x = cmp.getInt(TAG_SOURCE_X);
        int y = cmp.getInt(TAG_SOURCE_Y);
        int z = cmp.getInt(TAG_SOURCE_Z);
        source = new BlockPos(x, y, z);

        if (cmp.contains(TAG_PLAYER_COUNT)) {
            playerCount = cmp.getInt(TAG_PLAYER_COUNT);
        } else {
            playerCount = 1;
        }

        ListTag entityTagList = cmp.getList(TAG_ENTITIES_INSIDE, 10);
        for (int i = 0; i < entityTagList.size(); i++) {
            CompoundTag entityTag = entityTagList.getCompound(i);
            entityInsideUUIDS.add(entityTag.getUUID(TAG_UUID));
        }

        ListTag playerTagList = cmp.getList(TAG_PLAYERS, 10);
        for (int i = 0; i < playerTagList.size(); i++) {
            CompoundTag playerTag = playerTagList.getCompound(i);
            playerAttackerUUIDS.add(playerTag.getUUID(TAG_UUID));
        }

        ListTag mobTagList = cmp.getList(TAG_MOBS, 10);
        for (int i = 0; i < mobTagList.size(); i++) {
            CompoundTag mobTag = mobTagList.getCompound(i);
            summonedMobUUIDS.add(mobTag.getUUID(TAG_UUID));
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

        if (!isAlive() || level().isClientSide() || level().players().isEmpty())
            return;

        if (getInvulnerableTime() == 0) {
            if (tickCount % 20 == 5)
                spawnMissile();

            tpDelay--;
            if (tpDelay == 0 && getHealth() > 0) {
                teleportRandomly();

                List<Player> players = GaiaArenaHelper.getPlayersInsideArena(level(), source);
                int count = 9 - (phase == 2 ? 1 : (int) (5 * (getHealth() / getMaxHealth())));
                for (int i = 0; i < count; i++) {
                    int x = source.getX() - 10 + random.nextInt(20);
                    int y = (int) players.get(random.nextInt(players.size())).getY();
                    int z = source.getZ() - 10 + random.nextInt(20);

                    LandMine landmine = MegaBotanyEntities.LAND_MINE.get().create(level());
                    landmine.setPos(x + 0.5, y, z + 0.5);
                    landmine.summoner = this;
                    landmine.setMineType(level().random.nextInt(4));
                    level().addFreshEntity(landmine);
                }

                tpDelay = 20 + (phase == 2 ? 6 : (int) (30 * (getHealth() / getMaxHealth())));

                for (int pl = 0; pl < playerCount; pl++) {
                    for (int i = 0; i < (spawnPixies ? level().random.nextInt(6) : 1); i++) {
                        PixieEntity pixie = new PixieEntity(level());
                        pixie.setProps(players.get(random.nextInt(players.size())), this, 1, 8);
                        pixie.setPos(getX() + getBbWidth() / 2, getY() + 2, getZ() + getBbWidth() / 2);
                        pixie.finalizeSpawn((ServerLevelAccessor) level(), level().getCurrentDifficultyAt(pixie.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        level().addFreshEntity(pixie);
                    }
                }

                spawnPixies = false;
            }

            //20% health activates phase two, aka mob spawning plus regen
            if (getHealth() < getMaxHealth() / 5 && phase == 1) {
                phase = 2;
                setInvulnerableTime(25);
                tpDelay = 4;
            }
        } else if (phase == 2) { //mob spawn
            setDeltaMovement(Vec3.ZERO);

            if (mobSpawnTickDelay > 0) {
                setDeltaMovement(getDeltaMovement().x(), 0.2, getDeltaMovement().z());
                mobSpawnTickDelay--;
            } else if (mobSpawnTickDelay == 0 && tickCount % 20 == 0 && mobWave < 10) { //doesn't work only wave one spawns
                spawnMob(entityInsideUUIDS.stream().map(uuid -> level().getPlayerByUUID(uuid)).filter(Objects::nonNull).toList());
            }

            if (mobWave != 10 || !summonedMobUUIDS.isEmpty()) {
                setInvulnerableTime(getInvulnerableTime() + 1);
            }

            if (mobSpawnTickDelay == 0)
                setHealth(getHealth() + getMaxHealth() / 1000);
        } else {//spawn
            //happens randomly a few time in the original gaia
            //spawnAnim();
            setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
            setDeltaMovement(getDeltaMovement().x(), 0, getDeltaMovement().z());
            if (getInvulnerableTime() == 1)
                phase = 1;
        }

        if (getInvulnerableTime() > 0)
            setInvulnerableTime(getInvulnerableTime() - 1);
    }

    private void spawnMob(List<Player> players) {
        for (int pl = 0; pl < playerCount; pl++) {
            for (int i = 0; i < 3 + level().random.nextInt(2); i++) {
                Mob entity = switch (level().random.nextInt(3)) {
                    case 0 -> {
                        if (level().random.nextInt(3) == 0) {
                            yield EntityType.WITCH.create(level());
                        }
                        yield EntityType.ZOMBIE.create(level());
                    }
                    case 1 -> {
                        if (level().random.nextInt(8) == 0) {
                            yield EntityType.WITHER_SKELETON.create(level());
                        }
                        yield EntityType.SKELETON.create(level());
                    }
                    case 2 -> {
                        if (!players.isEmpty()) {
                            for (int j = 0; j < 1 + level().random.nextInt(8); j++) {
                                PixieEntity pixie = new PixieEntity(level());
                                pixie.setProps(players.get(random.nextInt(players.size())), this, 1, 8);
                                pixie.setPos(getX() + getBbWidth() / 2, getY() + 2, getZ() + getBbWidth() / 2);
                                pixie.finalizeSpawn((ServerLevelAccessor) level(), level().getCurrentDifficultyAt(pixie.blockPosition()),
                                        MobSpawnType.MOB_SUMMONED, null, null);
                                level().addFreshEntity(pixie);
                            }
                        }
                        yield null;
                    }
                    default -> null;
                };

                if (entity != null) {
                    entityInsideUUIDS.add(entity.getUUID());
                    summonedMobUUIDS.add(entity.getUUID());
                    if (entity.isPassenger()) {
                        Entity vehicle = entity.getVehicle();
                        if (vehicle.getType().getCategory() == MobCategory.MONSTER)
                            summonedMobUUIDS.add(vehicle.getUUID());
                        entityInsideUUIDS.add(vehicle.getUUID());
                    }

                    if (!entity.fireImmune()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
                    }
                    float range = 6F;
                    entity.setPos(getX() + 0.5 + Math.random() * range - range / 2, getY() - 1,
                            getZ() + 0.5 + Math.random() * range - range / 2);
                    entity.finalizeSpawn((ServerLevelAccessor) level(), level().getCurrentDifficultyAt(entity.blockPosition()),
                            MobSpawnType.MOB_SUMMONED, null, null);
                    if (entity instanceof WitherSkeleton || entity instanceof Zombie)
                        entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BotaniaItems.elementiumSword));
                    level().addFreshEntity(entity);
                }
            }
        }
        mobWave++;
    }

    private void spawnMissile() {
        MagicMissileEntity missile = new MagicMissileEntity(this, true);
        missile.setPos(getX() + (Math.random() - 0.5 * 0.1), getY() + 2.4 + (Math.random() - 0.5 * 0.1), getZ() + (Math.random() - 0.5 * 0.1));
        if (missile.findTarget()) {
            playSound(BotaniaSounds.missile, 1F, 0.8F + (float) Math.random() * 0.2F);
            level().addFreshEntity(missile);
        }
    }

    public static boolean isFightingBoss(Player player, Iterable<Entity> entities) {
        List<GaiaGuardianIII> bosses = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof GaiaGuardianIII gaiaGuardianIII) {
                bosses.add(gaiaGuardianIII);
            }
        }

        for (GaiaGuardianIII currentBoss : bosses) {
            if (currentBoss.getPlayersInside().contains(player))
                return true;
        }

        return false;
    }

    public void setInvulnerableTime(int time) {
        entityData.set(INVULNERABLE_TIME, time);
    }

    public int getInvulnerableTime() {
        return entityData.get(INVULNERABLE_TIME);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(INVULNERABLE_TIME, 0);
        entityData.define(MOBS, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            GaiaArenaHelper.SpawnArenaParticle(level(), source);
            Player player = Proxy.INSTANCE.getClientPlayer();
            if (GaiaArenaHelper.getPlayersInsideArena(level(), source).contains(player))
                player.getAbilities().flying &= player.getAbilities().instabuild;

            if (getInvulnerableTime() > 20)
                GaiaArenaHelper.particles(this, source);
            return;
        }
        bossInfo.setProgress(getHealth() / getMaxHealth());

        if (!isAlive())
            return;

        List<Entity> entities = GaiaArenaHelper.getEntitiesInsideArena(level(), source);
        List<Entity> entitiesToRemove = entities.stream().filter(entity -> !entityInsideUUIDS.contains(entity.getUUID())).toList();
        List<Entity> entitiesToKeep = entities.stream().filter(entity -> entityInsideUUIDS.contains(entity.getUUID())).toList();
        List<Player> players = entityInsideUUIDS.stream().map(entity -> level().getPlayerByUUID(entity)).filter(Objects::nonNull).toList();
        entityInsideUUIDS.removeIf(uuid -> entities.stream().noneMatch(entity -> entity.getUUID().equals(uuid)) && !level().players().isEmpty());
        summonedMobUUIDS.removeIf(uuid -> entities.stream().noneMatch(entity -> entity.getUUID().equals(uuid)));
        playerAttackerUUIDS.removeIf(uuid -> players.stream().noneMatch(player -> player.getUUID().equals(uuid)) && !level().players().isEmpty()); //prevents a player that died or dced to get the reward

        if (players.isEmpty() && !level().players().isEmpty()) {
            discard();
            return;
        }

        if (entityData.get(MOBS) != summonedMobUUIDS.size())
            entityData.set(MOBS, summonedMobUUIDS.size());

        for (Entity entity : entitiesToKeep)
            GaiaArenaHelper.keepInsideArena(entity, source);
        for (Entity entity : entitiesToRemove)
            GaiaArenaHelper.keepOutsideArena(entity, source);

        for (Player player : players) {
            InventoryAccessor playerInv = (InventoryAccessor) player.getInventory(); // minecraft inventory
            LazyOptional<ICuriosItemHandler> playerCurio = CuriosApi.getCuriosInventory(player); // curio inventory

            player.getAbilities().flying &= player.getAbilities().instabuild;

            for (NonNullList<ItemStack> inv : playerInv.getCompartments()) {
                for (int i = 0; i < inv.size(); i++) {
                    if (!inv.get(i).isEmpty() && !GaiaArenaHelper.mods.contains(BuiltInRegistries.ITEM.getKey(inv.get(i).getItem()).getNamespace())) {
                        player.drop(inv.get(i), true, true);
                        inv.set(i, ItemStack.EMPTY);
                    }
                }
            }

            playerCurio.resolve().get().getCurios().forEach((slot, itemStackHandler) -> {
                for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                    ItemStack curioItem = itemStackHandler.getStacks().getStackInSlot(i);
                    if (!curioItem.isEmpty() && !GaiaArenaHelper.mods.contains(BuiltInRegistries.ITEM.getKey(curioItem.getItem()).getNamespace())) {
                        ItemEntity item = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), curioItem);
                        player.level().addFreshEntity(item);

                        itemStackHandler.getStacks().setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            });
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

                    if (state.getDestroySpeed(level(), pos) == -1) {
                        continue;
                    }

                    //don't break blacklisted blocks
                    if (state.is(BotaniaTags.Blocks.GAIA_BREAK_BLACKLIST)) {
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

    public static List<GaiaGuardianIII> getAllGaiaGuardianIII(Iterable<Entity> entities) {
        List<GaiaGuardianIII> gaiaGuardianIII = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof GaiaGuardianIII)
                gaiaGuardianIII.add((GaiaGuardianIII) entity);
        }
        return gaiaGuardianIII;
    }

    private static int countGaiaGuardiansIIIAround(Level world, BlockPos source) {
        List<GaiaGuardianIII> l = world.getEntitiesOfClass(GaiaGuardianIII.class, GaiaArenaHelper.getArenaBB(source));
        return l.size();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(source);
        friendlyByteBuf.writeInt(playerCount);
        friendlyByteBuf.writeUUID(bossInfoUUID);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        source = friendlyByteBuf.readBlockPos();
        playerCount = friendlyByteBuf.readInt();
        bossInfoUUID = friendlyByteBuf.readUUID();
    }

    @Override
    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        if (pEntity instanceof Player player)
            entityInsideUUIDS.remove(player.getUUID());
        return super.killedEntity(pLevel, pEntity);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (level().isClientSide) {
            GaiaGuardianBossBarHandler.bosses.remove(this);
        }
        super.remove(reason);
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        LivingEntity lastAttacker = getKillCredit();

        if (!level().isClientSide) {
            for (UUID u : playerAttackerUUIDS) {
                Player player = level().getPlayerByUUID(u);
                if (!isTruePlayer(player)) {
                    continue;
                }
                DamageSource currSource = player == lastAttacker ? source : player.damageSources().playerAttack(player);
                if (player != lastAttacker) {
                    CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger((ServerPlayer) player, this, currSource);
                }
            }

            for (Player player : GaiaArenaHelper.getPlayersInsideArena(level(), this.source)) {
                if (player.getEffect(MobEffects.WITHER) != null) {
                    player.removeEffect(MobEffects.WITHER);
                }
            }

            // Stop all the pixies leftover from the fight
            for (PixieEntity pixie : level().getEntitiesOfClass(PixieEntity.class, GaiaArenaHelper.getArenaBB(this.source), p -> p.isAlive() && p.getPixieType() == 1)) {
                pixie.spawnAnim();
                pixie.discard();
            }
            for (LandMine landmine : level().getEntitiesOfClass(LandMine.class, GaiaArenaHelper.getArenaBB(this.source))) {
                landmine.discard();
            }
        }

        playSound(BotaniaSounds.gaiaDeath, 1F, (1F + (level().random.nextFloat() - level().random.nextFloat()) * 0.2F) * 0.7F);
        level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1D, 0D, 0D);
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        if (phase != 2) {
            return BuiltInLootTables.EMPTY;
        }
        return new ResourceLocation(MegaBotany.MOD_ID, "gaia_guardian_3");
    }

    @Override // copy from gaia
    protected void dropFromLootTable(@NotNull DamageSource source, boolean wasRecentlyHit) {
        if (wasRecentlyHit && isTruePlayer(source.getEntity())) {
            trueKiller = (Player) source.getEntity();
        }

        for (UUID u : playerAttackerUUIDS) {
            Player player = level().getPlayerByUUID(u);
            if (!isTruePlayer(player)) {
                continue;
            }

            Player saveLastAttacker = lastHurtByPlayer;
            Vec3 savePos = position();

            lastHurtByPlayer = player;
            setPos(player.getX(), player.getY(), player.getZ());
            super.dropFromLootTable(player.damageSources().playerAttack(player), wasRecentlyHit);
            setPos(savePos.x(), savePos.y(), savePos.z());
            lastHurtByPlayer = saveLastAttacker;
        }

        trueKiller = null;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getSpawnedMob() {
        return entityData.get(MOBS);
    }

    public UUID getBossInfoUuid() {
        return bossInfoUUID;
    }

    public List<Player> getPlayersInside() {
        return entityInsideUUIDS.stream().map(uuid -> level().getPlayerByUUID(uuid)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class ModEvent {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(MegaBotanyEntities.GAIA_GUARDIAN_III.get(), createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID)
    private static class ForgeEvent {
        @SubscribeEvent
        public static void onBlockBreak(BlockEvent.BreakEvent event) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                for (GaiaGuardianIII gaiaGuardianIII : getAllGaiaGuardianIII(serverLevel.getAllEntities())) {
                    if (GaiaArenaHelper.isBlockPosInsideArena(gaiaGuardianIII.source, event.getPos()))
                        event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                for (GaiaGuardianIII gaiaGuardianIII : getAllGaiaGuardianIII(serverLevel.getAllEntities())) {
                    if (GaiaArenaHelper.isBlockPosInsideArena(gaiaGuardianIII.source, event.getPos()))
                        event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void itemCooldown(InputEvent.InteractionKeyMappingTriggered event) {
            if (event.isAttack()) {
                Player LocalPlayer = Minecraft.getInstance().player;
                for (GaiaGuardianIII gaiaGuardianIII : getAllGaiaGuardianIII(Minecraft.getInstance().level.entitiesForRendering())) {
                    for (Player player : GaiaArenaHelper.getPlayersInsideArena(Minecraft.getInstance().level, gaiaGuardianIII.source)) {
                        if (player == LocalPlayer && LocalPlayer.getCooldowns().isOnCooldown(LocalPlayer.getMainHandItem().getItem())) {
                            event.setCanceled(true);
                            event.setSwingHand(false);
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void removePickUp(EntityItemPickupEvent event) {
            if (event.getEntity().level() instanceof ServerLevel server && isFightingBoss(event.getEntity(), server.getAllEntities()) && !GaiaArenaHelper.mods.contains(BuiltInRegistries.ITEM.getKey(event.getItem().getItem().getItem()).getNamespace()))
                event.setCanceled(true);
        }
    }
}

package io.github.megadoxs.megabotany.common.helper;

import com.google.common.collect.ImmutableList;
import io.github.megadoxs.megabotany.common.entity.GaiaGuardianIII;
import io.github.megadoxs.megabotany.common.util.MegaBotanyTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public class GaiaArenaHelper {

    public static final float ARENA_RANGE = 12.0F;
    public static final int ARENA_HEIGHT = 5;
    public static final List<String> mods = List.of("minecraft", "botania", "megabotany"); // will have to be renamed do megabotany

    private static final List<BlockPos> PYLON_LOCATIONS = ImmutableList.of(
            new BlockPos(4, 1, 4),
            new BlockPos(4, 1, -4),
            new BlockPos(-4, 1, 4),
            new BlockPos(-4, 1, -4)
    );
    private static final TagKey<Block> BLACKLIST = BotaniaTags.Blocks.GAIA_BREAK_BLACKLIST;

    public static boolean shouldSpawn(Player player, Level world, BlockPos pos) {

        if (!(world.getBlockEntity(pos) instanceof BeaconBlockEntity) || !PlayerHelper.isTruePlayer(player)) {
            return false;
        }

        //check difficulty
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            if (!world.isClientSide) {
                player.sendSystemMessage(Component.translatable("botaniamisc.peacefulNoob").withStyle(ChatFormatting.RED));
            }
            return false;
        }

        //check pylons
        List<BlockPos> invalidPylonBlocks = checkPylons(world, pos);
        if (!invalidPylonBlocks.isEmpty()) {
            if (world.isClientSide) {
                warnInvalidBlocks(world, invalidPylonBlocks);
            } else {
                player.sendSystemMessage(Component.translatable("botaniamisc.needsCatalysts").withStyle(ChatFormatting.RED));
            }

            return false;
        }

        //check arena shape
        List<BlockPos> invalidArenaBlocks = checkArena(world, pos);
        if (!invalidArenaBlocks.isEmpty()) {
            if (world.isClientSide) {
                warnInvalidBlocks(world, invalidArenaBlocks);
            } else {
                XplatAbstractions.INSTANCE.sendToPlayer(player, new BotaniaEffectPacket(EffectType.ARENA_INDICATOR, pos.getX(), pos.getY(), pos.getZ()));

                player.sendSystemMessage(Component.translatable("botaniamisc.badArena").withStyle(ChatFormatting.RED));
            }

            return false;
        }
        return true;
    }

    @NotNull
    public static AABB getArenaBB(@NotNull BlockPos source) {
        double range = 15.0;
        return new AABB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range);
    }

    private static List<BlockPos> checkPylons(Level world, BlockPos beaconPos) {
        List<BlockPos> invalidPylonBlocks = new ArrayList<>();

        for (BlockPos coords : PYLON_LOCATIONS) {
            BlockPos pos_ = beaconPos.offset(coords);

            BlockState state = world.getBlockState(pos_);
            if (!state.is(BotaniaBlocks.gaiaPylon)) {
                invalidPylonBlocks.add(pos_);
            }
        }

        return invalidPylonBlocks;
    }

    private static void warnInvalidBlocks(Level world, Iterable<BlockPos> invalidPositions) {
        WispParticleData data = WispParticleData.wisp(0.5F, 1, 0.2F, 0.2F, 8, false);
        for (BlockPos pos_ : invalidPositions) {
            world.addParticle(data, pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5, 0, 0, 0);
        }
    }

    private static List<BlockPos> checkArena(Level world, BlockPos beaconPos) {
        List<BlockPos> trippedPositions = new ArrayList<>();
        int range = (int) Math.ceil(ARENA_RANGE);
        BlockPos pos;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (Math.abs(x) == 4 && Math.abs(z) == 4 || MathHelper.pointDistancePlane(x, z, 0, 0) > ARENA_RANGE) {
                    continue; // Ignore pylons and out of circle
                }

                boolean hasFloor = false;

                for (int y = -2; y <= ARENA_HEIGHT; y++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue; //the beacon
                    }

                    pos = beaconPos.offset(x, y, z);

                    BlockState state = world.getBlockState(pos);

                    boolean allowBlockHere = y < 0;
                    boolean isBlockHere = !state.getCollisionShape(world, pos).isEmpty() && !state.is(MegaBotanyTags.Blocks.GAIA_ILLEGAL_BLOCKS);

                    if (allowBlockHere && isBlockHere) //floor is here! good
                    {
                        hasFloor = true;
                    }

                    if (y == 0 && !hasFloor) //column is entirely missing floor or is invalid block
                    {
                        trippedPositions.add(pos.below());
                    }

                    if (!allowBlockHere && isBlockHere && !state.is(BLACKLIST)) //ceiling is obstructed in this column
                    {
                        trippedPositions.add(pos);
                    }
                }
            }
        }
        return trippedPositions;
    }

    public static boolean isBlockPosInsideArena(BlockPos source, BlockPos pos) {
        return MathHelper.pointDistanceSpace(
                pos.getX(), pos.getY(), pos.getZ(),
                source.getX(), source.getY(), source.getZ()
        ) <= ARENA_RANGE;
    }

    public static List<Player> getPlayersInsideArena(Level world, BlockPos pos) {
        return PlayerHelper.getRealPlayersIn(world, getArenaBB(pos));
    }

    public static List<Entity> getEntitiesInsideArena(Level world, BlockPos pos) {
        return world.getEntitiesOfClass(Entity.class, getArenaBB(pos), (entity) -> {
            if (entity instanceof GaiaGuardianIII || entity instanceof ItemEntity) {
                return false;
            }
            if (entity instanceof Projectile projectile) {
                return !world.getEntitiesOfClass(Entity.class, getArenaBB(pos)).contains(projectile.getOwner());
            }
            return true;
        });
    }

    public static void SpawnArenaParticle(Level level, BlockPos source) {
        for (int i = 0; i < 360; i += 8) {
            float r = 0.6F;
            float g = 0F;
            float b = 0.2F;
            float m = 0.15F;
            float mv = 0.35F;

            float rad = i * (float) Math.PI / 180F;
            double x = source.getX() + 0.5 - Math.cos(rad) * ARENA_RANGE;
            double y = source.getY() + 0.5;
            double z = source.getZ() + 0.5 - Math.sin(rad) * ARENA_RANGE;

            WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
            level.addParticle(data, x, y, z, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * mv, (float) (Math.random() - 0.5F) * m);
        }
    }

    public static void particles(Entity entity, BlockPos source) {
        Level level = entity.level();

        Vec3 pos = VecHelper.fromEntityCenter(entity).subtract(0, 0.2, 0);
        for (BlockPos arr : PYLON_LOCATIONS) {
            Vec3 pylonPos = new Vec3(source.getX() + arr.getX(), source.getY() + arr.getY(), source.getZ() + arr.getZ());
            double worldTime = entity.tickCount;
            worldTime /= 5;

            float rad = 0.75F + (float) Math.random() * 0.05F;
            double xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad;
            double zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad;

            Vec3 partPos = new Vec3(xp, pylonPos.y, zp);
            Vec3 mot = pos.subtract(partPos).scale(0.04);

            float r = 0.7F + (float) Math.random() * 0.3F;
            float g = (float) Math.random() * 0.3F;
            float b = 0.7F + (float) Math.random() * 0.3F;

            WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, r, g, b, 1);
            level.addParticle(data, partPos.x, partPos.y, partPos.z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
            WispParticleData data1 = WispParticleData.wisp(0.4F, r, g, b);
            level.addParticle(data1, partPos.x, partPos.y, partPos.z, (float) mot.x, (float) mot.y, (float) mot.z);
        }
    }

    public static void keepInsideArena(Entity entity, BlockPos source) {
        if (MathHelper.pointDistanceSpace(entity.getX(), entity.getY(), entity.getZ(), source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= ARENA_RANGE) {
            Vec3 sourceVector = new Vec3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
            Vec3 playerVector = VecHelper.fromEntityCenter(entity);
            Vec3 motion = sourceVector.subtract(playerVector).normalize();

            if (entity.getY() - source.getY() < 4)
                entity.setDeltaMovement(motion.x, 0.2, motion.z);
            else
                entity.setDeltaMovement(motion.x, motion.y, motion.z);
            entity.hurtMarked = true;
        }
    }

    public static void keepOutsideArena(Entity entity, BlockPos source) {
        if (MathHelper.pointDistanceSpace(entity.getX(), entity.getY(), entity.getZ(), source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) <= ARENA_RANGE) {
            Vec3 sourceVector = new Vec3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
            Vec3 playerVector = VecHelper.fromEntityCenter(entity);
            Vec3 motion = playerVector.subtract(sourceVector).normalize();

            entity.setDeltaMovement(motion.x, 0.2, motion.z);
            entity.hurtMarked = true;
        }
    }
}

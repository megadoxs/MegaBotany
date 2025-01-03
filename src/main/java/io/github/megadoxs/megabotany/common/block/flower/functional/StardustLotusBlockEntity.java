package io.github.megadoxs.megabotany.common.block.flower.functional;

import io.github.megadoxs.megabotany.api.block_entity.FlowerBindableFunctionalFlowerBlockEntity;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BlackLotusItem;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class StardustLotusBlockEntity extends FlowerBindableFunctionalFlowerBlockEntity<StardustLotusBlockEntity> {
    private static final int RANGE = 1;
    private static final int OPEN_MANA_COST = 100000;
    private static final int TELEPORT_MANA_COST = 10000;
    private static final int TICK_NANA_COST = 100;
    private final ArrayList<UUID> entitiesInside = new ArrayList<>();
    private final ArrayList<UUID> playersInside = new ArrayList<>();
    private static final String TAG_ACTIVATED = "activated";
    private static final String TAG_CONSUMED = "consumed";
    private static final String TAG_PLAYERS_INSIDE = "playersInside";
    private static final String TAG_ENTITIES_INSIDE = "entitiesInside";
    private static final String TAG_UUID = "uuid";

    private int consumed = 0;
    private boolean activated = false;

    private static final BlockPos[] LIVINGWOOD_LOCATIONS = {
            new BlockPos(2, 1, 2), new BlockPos(-2, 1, 2), new BlockPos(2, 1, -2), new BlockPos(-2, 1, -2),
            new BlockPos(2, 0, 2), new BlockPos(-2, 0, 2), new BlockPos(2, 0, -2), new BlockPos(-2, 0, -2),
            new BlockPos(2, -1, 2), new BlockPos(2, -1, 1), new BlockPos(2, -1, 0), new BlockPos(2, -1, -1), new BlockPos(2, -1, -2),
            new BlockPos(1, -1, 2), new BlockPos(1, -1, -2),
            new BlockPos(0, -1, 2), new BlockPos(0, -1, -2),
            new BlockPos(-1, -1, 2), new BlockPos(-1, -1, -2),
            new BlockPos(-2, -1, 2), new BlockPos(-2, -1, 1), new BlockPos(-2, -1, 0), new BlockPos(-2, -1, -1), new BlockPos(-2, -1, -2)
    };

    private static final BlockPos[] GLIMMERING_LIVINGWOOD_LOCATIONS = {
            new BlockPos(2, 2, 2), new BlockPos(-2, 2, 2), new BlockPos(2, 2, -2), new BlockPos(-2, 2, -2)
    };

    public StardustLotusBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.STARDUST_LOTUS, pos, state, StardustLotusBlockEntity.class);
    }

    public void teleportEntity(UUID entity) {
        entitiesInside.add(entity);
        setChanged();
    }

    public void teleportPlayer(UUID entity) {
        playersInside.add(entity);
        setChanged();
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (!activated && !getLevel().isClientSide && getMana() >= OPEN_MANA_COST / 100 && hasValidStructure() && consumed < OPEN_MANA_COST) {
            addMana(-OPEN_MANA_COST / 100);
            sync();
            consumed += OPEN_MANA_COST / 100;
            setChanged();
        }

        if (!activated && !getLevel().isClientSide && consumed >= OPEN_MANA_COST && hasValidStructure()) {
            for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                if (item.getItem().getItem() instanceof BlackLotusItem) {
                    item.getItem().shrink(1);
                    activated = true;
                    setChanged();
                    break;
                }
            }
        }

        if (activated) {
            if (!getLevel().isClientSide) {
                if (getMana() < TICK_NANA_COST || !hasValidStructure()) {
                    activated = false;
                    setChanged();
                    getLevel().sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
                    return;
                } else {
                    addMana(-TICK_NANA_COST);
                    sync();
                }

                if (isValidFlower() && getBlockFlower().activated && getMana() >= TELEPORT_MANA_COST) {
                    ArrayList<UUID> entities = new ArrayList<>();
                    for (LivingEntity livingEntity : getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                        entities.add(livingEntity.getUUID());
                        if (livingEntity instanceof Player && !playersInside.contains(livingEntity.getUUID()) && getMana() >= TELEPORT_MANA_COST)
                            getBlockFlower().teleportPlayer(livingEntity.getUUID());
                        else if (!(livingEntity instanceof Player) && !entitiesInside.contains(livingEntity.getUUID()) && getMana() >= TELEPORT_MANA_COST)
                            getBlockFlower().teleportEntity(livingEntity.getUUID());
                        else
                            continue;
                        addMana(-TELEPORT_MANA_COST);
                        sync();
                        Vec3 pos = Vec3.atCenterOf(flowerBindingPos);
                        livingEntity.teleportTo(getLevel().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(flowerBindingDimensionId))), pos.x, pos.y, pos.z, Set.of(), livingEntity.getYRot(), livingEntity.getXRot());
                    }
                    if (entitiesInside.removeIf(entity -> !entities.contains(entity)) || playersInside.removeIf(entity -> !entities.contains(entity) && getLevel().getServer().getPlayerList().getPlayer(entity) != null))
                        setChanged();
                }
            } else if (isValidFlowerBinding)
                getLevel().addParticle(SparkleParticleData.fake(10F, 1F, 0.1F, 1F, 10), getEffectivePos().getX() + 0.5, getEffectivePos().getY() + 1, getEffectivePos().getZ() + 0.5, 0, 0, 0);
        }

        if (getLevel().isClientSide && hasValidStructure()) {
            for (int i = 0; i < 360 * consumed / OPEN_MANA_COST; i += 15) {
                float rad = i * (float) Math.PI / 180F;
                double x = getEffectivePos().getX() + 0.5 - Math.cos(rad) * 2;
                double y = getEffectivePos().getY() + 0.2;
                double z = getEffectivePos().getZ() + 0.5 - Math.sin(rad) * 2;
                getLevel().addParticle(SparkleParticleData.fake(2F, 1F, 0.1F, 1F, 10), x, y, z, 0, 0, 0);
            }
        }
    }

    public boolean hasValidStructure() {
        for (BlockPos o : LIVINGWOOD_LOCATIONS)
            if (getLevel().getBlockState(getBlockPos().offset(o)).getBlock() != BotaniaBlocks.livingwood)
                return false;
        for (BlockPos o : GLIMMERING_LIVINGWOOD_LOCATIONS)
            if (getLevel().getBlockState(getBlockPos().offset(o)).getBlock() != BotaniaBlocks.livingwoodGlimmering)
                return false;
        return true;
    }

    @Override
    public int getMaxMana() {
        return 150000;
    }

    @Override
    public int getColor() {
        return 0x800080;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public ItemStack getFlowerHudIcon() {
        return new ItemStack(MegaBotanyFlowerBlocks.stardustLotus);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putBoolean(TAG_ACTIVATED, activated);
        cmp.putInt(TAG_CONSUMED, consumed);

        ListTag entityList = new ListTag();
        for (UUID entity : entitiesInside) {
            CompoundTag entityTag = new CompoundTag();
            entityTag.putUUID(TAG_UUID, entity);
            entityList.add(entityTag);
        }
        cmp.put(TAG_ENTITIES_INSIDE, entityList);

        ListTag playerList = new ListTag();
        for (UUID entity : playersInside) {
            CompoundTag entityTag = new CompoundTag();
            entityTag.putUUID(TAG_UUID, entity);
            playerList.add(entityTag);
        }
        cmp.put(TAG_PLAYERS_INSIDE, playerList);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        activated = cmp.getBoolean(TAG_ACTIVATED);
        consumed = cmp.getInt(TAG_CONSUMED);

        entitiesInside.clear();
        if (cmp.contains(TAG_ENTITIES_INSIDE, Tag.TAG_LIST)) {
            ListTag entityList = cmp.getList(TAG_ENTITIES_INSIDE, Tag.TAG_COMPOUND);
            for (int i = 0; i < entityList.size(); i++) {
                CompoundTag entityTag = entityList.getCompound(i);
                entitiesInside.add(entityTag.getUUID(TAG_UUID));
            }
        }

        playersInside.clear();
        if (cmp.contains(TAG_PLAYERS_INSIDE, Tag.TAG_LIST)) {
            ListTag playerList = cmp.getList(TAG_PLAYERS_INSIDE, Tag.TAG_COMPOUND);
            for (int i = 0; i < playerList.size(); i++) {
                CompoundTag entityTag = playerList.getCompound(i);
                playersInside.add(entityTag.getUUID(TAG_UUID));
            }
        }
    }
}

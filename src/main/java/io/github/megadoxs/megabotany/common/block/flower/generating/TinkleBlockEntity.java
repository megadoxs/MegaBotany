package io.github.megadoxs.megabotany.common.block.flower.generating;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyPOITypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.helper.PlayerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TinkleBlockEntity extends GeneratingFlowerBlockEntity {
    public static final int RANGE = 8;

    private final HashMap<ServerPlayer, Boolean> Oldplayers = new HashMap<>();

    public TinkleBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.TINKLE, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (getMana() < getMaxMana() && !getLevel().isClientSide) {
            //RANGE IN SQUARE
            List<ServerPlayer> players = getLevel().getEntitiesOfClass(ServerPlayer.class, new AABB(getEffectivePos()).inflate(RANGE));
            List<ServerPlayer> generators = new ArrayList<>();


            for (ServerPlayer player : players) {
                //RANGE IN CIRCLE
                if (getLevel() instanceof ServerLevel serverLevel) {
                    Optional<BlockPos> optional = serverLevel.getPoiManager().findClosest(
                            (holder) -> holder.get() == MegaBotanyPOITypes.TINKLE.get(),
                            player.blockPosition(),
                            8,
                            PoiManager.Occupancy.ANY
                    );

                    if (optional.isEmpty() || !optional.get().equals(getEffectivePos())) {
                        continue;
                    }
                }

                if (PlayerHelper.isTruePlayer(player) && Oldplayers.containsKey(player) && !Oldplayers.get(player) && player.isCrouching()) {
                    generators.add(player);
                }
            }
            Oldplayers.clear();
            players.forEach(player -> Oldplayers.put(player, player.isCrouching()));

            generators.forEach(player -> {
                addMana(10);
                sync();
            });
        }
    }

    @Override
    public int getMaxMana() {
        return 1000;
    }

    @Override
    public int getColor() {
        return 0xCCFF00;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
    }
}

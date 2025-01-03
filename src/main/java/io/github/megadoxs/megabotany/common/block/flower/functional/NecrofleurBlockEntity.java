package io.github.megadoxs.megabotany.common.block.flower.functional;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyPOITypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NecrofleurBlockEntity extends FunctionalFlowerBlockEntity {
    public static final int RANGE = 16;
    public static final int RANGE_MINI = 4;

    public NecrofleurBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NecrofleurBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.NECROFLEUR, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (getMana() > 0) {
            addMana(-50);
            sync();
        }
    }

    @SubscribeEvent
    public static void preventEffectHealing(LivingHealEvent event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            Optional<BlockPos> flower = serverLevel.getPoiManager().find(
                    (holder) -> holder.get() == MegaBotanyPOITypes.NECROFLEUR.get(),
                    (blockPos) -> ((NecrofleurBlockEntity) serverLevel.getBlockEntity(blockPos)).getMana() > 0,
                    event.getEntity().blockPosition(),
                    RANGE,
                    PoiManager.Occupancy.ANY
            );

            if (flower.isPresent()) {
                event.setCanceled(true);
                return;
            }

            Optional<BlockPos> chibi = serverLevel.getPoiManager().find(
                    (holder) -> holder.get() == MegaBotanyPOITypes.NECROFLEUR_CHIBI.get(),
                    (blockPos) -> ((NecrofleurBlockEntity.Mini) serverLevel.getBlockEntity(blockPos)).getMana() > 0,
                    event.getEntity().blockPosition(),
                    RANGE_MINI,
                    PoiManager.Occupancy.ANY
            );

            if (chibi.isPresent())
                event.setCanceled(true);
        }
    }

    public int getRange() {
        return RANGE;
    }

    @Override
    public int getMaxMana() {
        return 10000;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
    }

    public static class Mini extends NecrofleurBlockEntity {

        public Mini(BlockPos pos, BlockState state) {
            super(MegaBotanyFlowerBlocks.NECROFLEUR_CHIBI, pos, state);
        }

        @Override
        public int getRange() {
            return RANGE_MINI;
        }
    }
}

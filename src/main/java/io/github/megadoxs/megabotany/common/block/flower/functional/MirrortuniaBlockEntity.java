package io.github.megadoxs.megabotany.common.block.flower.functional;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyPOITypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MirrortuniaBlockEntity extends FunctionalFlowerBlockEntity { //TODO rework do something with the cancel effect, also make it work with all livingEntities
    private static final int RANGE = 16;
    private static final int MANA_COST = 1000;

    public MirrortuniaBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.MIRRORTUNIA, pos, state);
    }

    @SubscribeEvent
    public static void effectMirror(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getEffectInstance().isCurativeItem(Items.MILK_BUCKET.getDefaultInstance()) && !event.getEffectInstance().getEffect().isBeneficial()) {
            if (player.level() instanceof ServerLevel serverLevel) {
                Optional<BlockPos> optional = serverLevel.getPoiManager().findClosest(
                        (holder) -> holder.get() == MegaBotanyPOITypes.MIRRORTUNIA.get(),
                        (blockPos) -> ((MirrortuniaBlockEntity) serverLevel.getBlockEntity(blockPos)).getMana() > MANA_COST,
                        player.blockPosition(),
                        RANGE,
                        PoiManager.Occupancy.ANY
                );

                if (optional.isPresent() && serverLevel.getBlockEntity(optional.get()) instanceof MirrortuniaBlockEntity mirrortuniaBlockEntity) {
                    event.setResult(MobEffectEvent.Result.DENY);
                    mirrortuniaBlockEntity.addMana(-MANA_COST);
                    mirrortuniaBlockEntity.sync();
                }
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 2000;
    }

    @Override
    public int getColor() {
        return 0X4169E1;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
    }
}

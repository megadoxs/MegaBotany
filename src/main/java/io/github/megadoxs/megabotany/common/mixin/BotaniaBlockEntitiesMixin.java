package io.github.megadoxs.megabotany.common.mixin;

import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Mixin(BotaniaBlockEntities.class)
public class BotaniaBlockEntitiesMixin {

    //TODO do a better implement, apparently this is bad <clinit> maybe from manaSpreaderBlockEntity
    @ModifyArg(method = "type", at = @At(value = "INVOKE", target = "Lvazkii/botania/xplat/XplatAbstractions;createBlockEntityType(Ljava/util/function/BiFunction;[Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/entity/BlockEntityType;"), index = 1, remap = false)
    private static <T extends BlockEntity> Block[] args(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        List<Block> newblocks = new ArrayList<>(List.of(blocks));
        try {
            Method method = func.getClass().getMethod("apply", Object.class, Object.class);
            if (method.getReturnType().isInstance(ManaSpreaderBlockEntity.class)) {
                newblocks.add(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get());
                newblocks.add(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get());
            }
        } catch (Exception ignored) {
        }
        return newblocks.toArray(new Block[0]);
    }
}

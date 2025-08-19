package io.github.megadoxs.megabotany.common.network.C2SPacket;

import io.github.megadoxs.megabotany.common.item.equipment.bauble.PureDaisyPendant;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PureDaisyPendantRecipePacket {
    public BlockPos pos;

    public PureDaisyPendantRecipePacket(BlockPos pos) {
        this.pos = pos;
    }

    public static PureDaisyPendantRecipePacket decode(FriendlyByteBuf buf) {
        return new PureDaisyPendantRecipePacket(buf.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        PureDaisyPendant.applyRecipe(pos, ctx.get().getSender().serverLevel());
    }
}

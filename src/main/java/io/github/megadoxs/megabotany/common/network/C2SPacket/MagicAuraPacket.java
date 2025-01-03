package io.github.megadoxs.megabotany.common.network.C2SPacket;

import io.github.megadoxs.megabotany.common.item.equipment.bauble.JingweiFeather;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MagicAuraPacket {

    public MagicAuraPacket() {
    }

    public MagicAuraPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            JingweiFeather.trySpawnMagicAura(Objects.requireNonNull(context.getSender()));
        });
        return true;
    }
}

package io.github.megadoxs.megabotany.common.network.C2SPacket;

import io.github.megadoxs.megabotany.common.item.relic.Excaliber;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExcaliberLeftClickPacket {
    public ExcaliberLeftClickPacket() {
    }

    public ExcaliberLeftClickPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> Excaliber.trySpawnBurst(context.getSender(), context.getSender().getAttackStrengthScale(0)));
    }
}

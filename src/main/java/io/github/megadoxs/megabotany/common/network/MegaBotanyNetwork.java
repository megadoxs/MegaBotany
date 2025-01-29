package io.github.megadoxs.megabotany.common.network;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.network.C2SPacket.ExcaliberLeftClickPacket;
import io.github.megadoxs.megabotany.common.network.C2SPacket.MagicAuraPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MegaBotanyNetwork {

    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MegaBotany.MOD_ID, "network")).networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(MagicAuraPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(MagicAuraPacket::new).encoder(MagicAuraPacket::toBytes).consumerMainThread(MagicAuraPacket::handle).add();
        INSTANCE.messageBuilder(ExcaliberLeftClickPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ExcaliberLeftClickPacket::new).encoder(ExcaliberLeftClickPacket::toBytes).consumerMainThread(ExcaliberLeftClickPacket::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

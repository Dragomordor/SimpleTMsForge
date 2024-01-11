package git.dragomordor.simpletms.forge.network;

import git.dragomordor.simpletms.forge.config.SimpleTMsCommonConfig;
import git.dragomordor.simpletms.forge.util.OverlayMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModPacketHandler {
    public static final ModPacketHandler INSTANCE = new ModPacketHandler();



    public static void handleDisplayOverlayMessagePacket(OverlayMessage.DisplayOverlayMessagePacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.displayClientMessage(message.getMessage(), true);
        });
        context.setPacketHandled(true);
    }

    public static void encodeDisplayOverlayMessagePacket(OverlayMessage.DisplayOverlayMessagePacket message, FriendlyByteBuf buffer) {
        // Encode your packet data here
        buffer.writeComponent(message.getMessage());
    }

    public static OverlayMessage.DisplayOverlayMessagePacket decodeDisplayOverlayMessagePacket(FriendlyByteBuf buffer) {
        // Decode your packet data here
        return new OverlayMessage.DisplayOverlayMessagePacket(buffer.readComponent());
    }

    public static void handleServerCooldownTicksPacket(ServerCooldownTicksPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Update the client-side config with the server-configured cooldown ticks
            SimpleTMsCommonConfig.TM_COOLDOWN_TICKS.set(message.getCooldownTicks());
        });
        context.setPacketHandled(true);
    }

    public static void register() {
        // Register your packet handlers here
        ModNetwork.CHANNEL.registerMessage(0, OverlayMessage.DisplayOverlayMessagePacket.class,
                ModPacketHandler::encodeDisplayOverlayMessagePacket,
                ModPacketHandler::decodeDisplayOverlayMessagePacket,
                ModPacketHandler::handleDisplayOverlayMessagePacket);
    }

}

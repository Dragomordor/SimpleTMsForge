package git.dragomordor.simpletms.forge.util;

import git.dragomordor.simpletms.forge.network.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class OverlayMessage {
    public static void displayOverlayMessage(Player player, String message) {
        if (player instanceof ServerPlayer) {
            ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new DisplayOverlayMessagePacket(Component.translatable(message)));
        }
    }

    public static class DisplayOverlayMessagePacket {
        private final Component message;

        public DisplayOverlayMessagePacket(Component message) {
            this.message = message;
        }

        public Component getMessage() {
            return message;
        }

        public static void encode(DisplayOverlayMessagePacket message, FriendlyByteBuf buffer) {
            buffer.writeComponent(message.getMessage());
        }

        public static DisplayOverlayMessagePacket decode(FriendlyByteBuf buffer) {
            return new DisplayOverlayMessagePacket(buffer.readComponent());
        }
    }
}

package git.dragomordor.simpletms.forge.network;

import net.minecraft.network.FriendlyByteBuf;

// In the network package, create a new class (e.g., ServerCooldownTicksPacket)
public class ServerCooldownTicksPacket {
    private final int cooldownTicks;

    public ServerCooldownTicksPacket(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    // Encode the packet
    public static void encode(ServerCooldownTicksPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.cooldownTicks);
    }

    // Decode the packet
    public static ServerCooldownTicksPacket decode(FriendlyByteBuf buffer) {
        int cooldownTicks = buffer.readInt();
        return new ServerCooldownTicksPacket(cooldownTicks);
    }
}

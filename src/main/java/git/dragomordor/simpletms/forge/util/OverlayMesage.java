package git.dragomordor.simpletms.forge.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class OverlayMesage {
    public static void displayOverlayMessage(String message, Boolean bl) {
        Minecraft.getInstance().gui.setOverlayMessage(Component.translatable(message),bl);
    }
}

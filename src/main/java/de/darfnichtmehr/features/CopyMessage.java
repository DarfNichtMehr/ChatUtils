package de.darfnichtmehr.features;

import de.darfnichtmehr.ChatUtils;
import de.darfnichtmehr.util.Utils;
import net.labymod.ingamechat.renderer.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class CopyMessage {
    @SubscribeEvent
    public void onGuiClick(final GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!(event.gui instanceof GuiChat)
                || !ChatUtils.getInstance().getSettings().getCopyMessages()
                || ChatUtils.getInstance().getSettings().getCopyMessageKey() == -1
                || !Keyboard.isKeyDown(ChatUtils.getInstance().getSettings().getCopyMessageKey())
                || !Mouse.getEventButtonState()) return;

        final ChatLine clickedLine = Utils.getClickedChatLine(Mouse.getX(), Mouse.getY());
        if (clickedLine == null) ChatUtils.getInstance().getApi().displayMessageInChat(EnumChatFormatting.RED + "No message found!");
        else {
            GuiScreen.setClipboardString(EnumChatFormatting.getTextWithoutFormattingCodes(clickedLine.getMessage()).substring(ChatUtils.getInstance().getSettings().getStartCopyAt()));
            ChatUtils.getInstance().getApi().displayMessageInChat(EnumChatFormatting.GREEN + "Copied message to clipboard!");
        }
    }
}

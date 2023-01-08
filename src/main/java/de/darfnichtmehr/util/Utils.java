package de.darfnichtmehr.util;

import net.labymod.ingamechat.IngameChatManager;
import net.labymod.ingamechat.renderer.ChatLine;
import net.labymod.ingamechat.renderer.ChatRenderer;
import net.labymod.ingamechat.renderer.types.ChatRendererMain;
import net.labymod.ingamechat.renderer.types.ChatRendererSecond;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.List;

public class Utils {
    public static ChatLine getClickedChatLine(final int mouseX, final int mouseY) {
        final Minecraft mc = Minecraft.getMinecraft();
        final ChatRenderer chat = IngameChatManager.INSTANCE.getMain().isMouseOver() ? IngameChatManager.INSTANCE.getMain() : IngameChatManager.INSTANCE.getSecond();

        if (chat.isChatOpen()) {
            final ScaledResolution scaledresolution = new ScaledResolution(mc);
            final int scaleFactor = scaledresolution.getScaleFactor();
            final float chatScale = chat.getChatScale();
            final float chatWidth = chat.getChatWidth();

            int x = MathHelper.floor_float((float)(mouseX / scaleFactor - 3) / chatScale);
            if (chat instanceof ChatRendererSecond) x = ((int)(x - (scaledresolution.getScaledWidth() - chatWidth))+9);
            int y = MathHelper.floor_float((float)(mouseY / scaleFactor - 27) / chatScale);

            if (x >= 0 && y >= 0) {
                int lineCount = chat.getChatLines().size();
                if (x <= MathHelper.floor_float(chatWidth / chat.getChatScale()) && y < mc.fontRendererObj.FONT_HEIGHT * lineCount + lineCount) {
                    int lineIndex = y / mc.fontRendererObj.FONT_HEIGHT + chat.getScrollPos();
                    if (lineIndex >= 0 && lineIndex < lineCount) {
                        int counter = 0;
                        for (ChatLine backendLine : chat.getBackendComponents()) {
                            counter += getWrappedComponents(backendLine, chat).size();
                            if (counter > lineIndex) return backendLine;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static List<IChatComponent> getWrappedComponents(final ChatLine unwrappedLine, final ChatRenderer chat) {
        return GuiUtilRenderComponents.func_178908_a(((IChatComponent) unwrappedLine.getComponent()), MathHelper.floor_float(chat.getChatWidth() / chat.getChatScale()), Minecraft.getMinecraft().fontRendererObj, false,false);
    }

    public static String formatString(final String s) {
        return s.replace("&", "\u00a7");
    }
}

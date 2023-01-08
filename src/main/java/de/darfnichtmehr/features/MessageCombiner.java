package de.darfnichtmehr.features;

import de.darfnichtmehr.ChatUtils;
import de.darfnichtmehr.util.Utils;
import net.labymod.api.events.MessageModifyChatEvent;
import net.labymod.core.LabyModCore;
import net.labymod.ingamechat.IngameChatManager;
import net.labymod.ingamechat.renderer.ChatRenderer;
import net.labymod.ingamechat.renderer.MessageData;
import net.labymod.ingamechat.renderer.types.ChatRendererMain;
import net.labymod.ingamechat.renderer.types.ChatRendererSecond;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.HashMap;

public class MessageCombiner {

    private final ChatRendererMain chatMain = IngameChatManager.INSTANCE.getMain();
    private final ChatRendererSecond chatSecond = IngameChatManager.INSTANCE.getSecond();
    private final HashMap<ChatRenderer, HashMap<String, Object>> lastMessages = new HashMap<ChatRenderer, HashMap<String, Object>>(){{
        put(chatMain, new HashMap<String, Object>(){{
            put("lastMessage", null);
            put("counter", 1);
        }});

        put(chatSecond, new HashMap<String, Object>(){{
            put("lastMessage", null);
            put("counter", 1);
        }});
    }};

    public MessageCombiner() {
        ChatUtils.getInstance().getApi().getEventManager().register((MessageModifyChatEvent) componentObject -> {
            final IChatComponent component = ((IChatComponent) componentObject);
            if (!ChatUtils.getInstance().getSettings().getCombineMessages() || ChatUtils.getInstance().getSettings().getStartCombiningAt() > component.getUnformattedText().length()) return componentObject;

            final ChatRenderer chat = getChat(component);
            if (chat.getBackendComponents().size() == 0
                    || lastMessages.get(chat).get("lastMessage") == null
                    || !lastMessages.get(chat).get("lastMessage").equals(component.getUnformattedText().substring(ChatUtils.getInstance().getSettings().getStartCombiningAt()))) {
                lastMessages.get(chat).put("lastMessage", component.getUnformattedText().substring(ChatUtils.getInstance().getSettings().getStartCombiningAt()));
                lastMessages.get(chat).put("counter", 1);
            }
            else {
                lastMessages.get(chat).put("counter", ((Integer) lastMessages.get(chat).get("counter")) + 1);
                component.appendSibling(new ChatComponentText(String.format(Utils.formatString(ChatUtils.getInstance().getSettings().getCombinedMessageSuffix()), lastMessages.get(chat).get("counter"))));

                chat.getBackendComponents().remove(0);
                Minecraft.getMinecraft().ingameGUI.getChatGUI().refreshChat();
            }

            return component;
        });
    }

    private ChatRenderer getChat(final IChatComponent component) {
        MessageData messageData = IngameChatManager.INSTANCE.handleSwap(LabyMod.getInstance().getServerManager().handleChatMessage(component.getUnformattedText(), component.getFormattedText()), LabyModCore.getMinecraft().getChatComponent(component));
        return messageData.isDisplayInSecondChat() ? this.chatSecond : this.chatMain;
    }
}

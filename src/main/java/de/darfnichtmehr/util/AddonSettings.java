package de.darfnichtmehr.util;

import com.google.gson.JsonObject;
import de.darfnichtmehr.ChatUtils;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

import java.util.List;

public class AddonSettings {

    private boolean copyMessages;
    private int copyMessageKey;
    private int startCopyAt;
    private boolean combineMessages;
    private String combinedMessageSuffix;
    private int startCombiningAt;

    public void loadConfig(final JsonObject config) {
        this.copyMessages = config.has("copyMessages") && config.get("copyMessages").getAsBoolean();
        this.copyMessageKey = config.has("copyMessageKey") ? config.get("copyMessageKey").getAsInt() : -1;
        this.startCopyAt = config.has("startCopyAt") ? config.get("startCopyAt").getAsInt() : 0;
        this.combineMessages = config.has("combineMessages") && config.get("combineMessages").getAsBoolean();
        this.combinedMessageSuffix = config.has("combinedMessageSuffix") ? config.get("combinedMessageSuffix").getAsString() : " &7(%s)";
        this.startCombiningAt = config.has("startCombiningAt") ? config.get("startCombiningAt").getAsInt() : 0;
    }

    public void fillSettings(final List<SettingsElement> subSettings) {
        // Copy Message Settings
        subSettings.add(new HeaderElement("Copy Messages"));
        subSettings.add(new BooleanElement(
                "Enable Copy Messages",
                ChatUtils.getInstance(),
                new ControlElement.IconData(Material.LEVER),
                "copyMessages",
                this.combineMessages
        ));
        subSettings.add(new KeyElement(
                "Keybind (Click + Key)",
                ChatUtils.getInstance(),
                new ControlElement.IconData(Material.NAME_TAG),
                "copyMessageKey",
                this.copyMessageKey,
                false));

        // for some reason NumberElement doesn't have a constructor like the other elements, so callback has to be added like this
        final NumberElement startCopyAtElement = new NumberElement(
                "Start Copying At",
                new ControlElement.IconData(Material.SHEARS),
                this.startCopyAt
        ).addCallback(newValue -> {
            startCopyAt = newValue;
            ChatUtils.getInstance().getConfig().addProperty("startCopyAt", startCopyAt);
        });
        startCopyAtElement.setDescriptionText("Removes a certain amount of characters from the start of a copied message. Useful in case you have a prefix enabled (e.g. ChatTime), that you don't want to copy.");
        subSettings.add(startCopyAtElement);

        // Message Combiner Settings
        subSettings.add(new HeaderElement("Message Combiner"));
        subSettings.add(new BooleanElement(
                "Enable Message Combiner",
                ChatUtils.getInstance(),
                new ControlElement.IconData(Material.LEVER),
                "combineMessages",
                this.combineMessages
        ));
        final StringElement combinedSuffixElement = new StringElement(
                "Counter Format",
                ChatUtils.getInstance(),
                new ControlElement.IconData(Material.WORKBENCH),
                "combinedMessageSuffix",
                this.combinedMessageSuffix
        );
        combinedSuffixElement.setDescriptionText("Format the suffix with \"&\". \"%s\" is the placeholder for the counter. If you don't want the counter at all, leave the textbox empty.");
        subSettings.add(combinedSuffixElement);
        final NumberElement startCombiningAtElement = new NumberElement(
                "Start Combining At",
                new ControlElement.IconData(Material.SHEARS),
                this.startCombiningAt
        );
        startCombiningAtElement
                .addCallback(newValue -> {
                    startCombiningAt = newValue;
                    ChatUtils.getInstance().getConfig().addProperty("startCombiningAt", startCombiningAt);
                })
                .setDescriptionText("Compares Duplicate-Messages after a certain amount of characters. Useful if you have an addon like ChatTime enabled, since messages sent at different times technically won't be the same.");
        subSettings.add(startCombiningAtElement);
    }

    public boolean getCopyMessages() {
        return copyMessages;
    }
    
    public int getCopyMessageKey() {
        return copyMessageKey;
    }

    public int getStartCopyAt() {
        return startCopyAt;
    }

    public boolean getCombineMessages() {
        return combineMessages;
    }

    public String getCombinedMessageSuffix() {
        return combinedMessageSuffix;
    }

    public int getStartCombiningAt() {
        return startCombiningAt;
    }
}

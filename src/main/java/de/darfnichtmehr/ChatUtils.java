package de.darfnichtmehr;

import de.darfnichtmehr.features.CopyMessage;
import de.darfnichtmehr.features.MessageCombiner;
import de.darfnichtmehr.util.AddonSettings;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ChatUtils extends LabyModAddon {

    private static ChatUtils INSTANCE;
    private AddonSettings SETTINGS;
    private final Logger LOGGER = LogManager.getLogger("ChatUtils");

    @Override
    public void onEnable() {
        INSTANCE = this;
        SETTINGS = new AddonSettings();

        this.getApi().registerForgeListener(new CopyMessage());
        new MessageCombiner();

        LOGGER.info("--------------------------------");
        LOGGER.info("ChatUtils LabyMod Addon Enabled!");
        LOGGER.info("--------------------------------");
    }

    @Override
    public void loadConfig() {
        SETTINGS.loadConfig(this.getConfig());
    }

    @Override
    public void fillSettings(final List<SettingsElement> subSettings) {
        SETTINGS.fillSettings(subSettings);
    }

    public static ChatUtils getInstance() {
        return INSTANCE;
    }

    public AddonSettings getSettings() {
        return SETTINGS;
    }

}

package io.github.lukeeff.muteadvancements;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.lukeeff.muteadvancements.protocol.AdvancementListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MuteAdvancements extends JavaPlugin {

    ProtocolManager manager;
    AdvancementListener listener;

    @Override
    public void onEnable() {
        this.manager = ProtocolLibrary.getProtocolManager();
        this.listener = new AdvancementListener(this, manager);
    }

    @Override
    public void onDisable() {

    }

}

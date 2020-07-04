package io.github.lukeeff.muteadvancements.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.gson.*;
import io.github.lukeeff.muteadvancements.MuteAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AdvancementListener {

    private final ProtocolManager manager;
    private final MuteAdvancements plugin;

    public AdvancementListener(MuteAdvancements plugin, ProtocolManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        addListener();
    }

    private void addListener() {
        PacketAdapter adapter = getPacketAdapter();
        manager.addPacketListener(adapter);

    }

    private PacketAdapter getPacketAdapter() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(event.getPacket().getChatTypes().read(0).equals(EnumWrappers.ChatType.SYSTEM)) {
                    String advancement = event.getPacket().getChatComponents().getValues().get(0).getJson();
                    JsonObject object = new JsonParser().parse(advancement).getAsJsonObject();
                    if(isAdvancement(object) && isVanishedPlayer(object)) {
                        event.setCancelled(true);
                    }
            }
            }
        };
    }

    /**
     * Checks if the json is from an advancement.
     *
     * @param object the WrappedTextComponent as a json object.
     * @return true if it is from an advancement.
     * @author lukeeff
     */
    private boolean isAdvancement(JsonObject object) {
        final String field = "translate";
        final boolean hasTranslate = object.has(field);
        if(hasTranslate) {
            return object.getAsJsonPrimitive(field).getAsString().contains("chat.type.advancement");
        }
        return false;
    }

    /**
     * Checks if the player in json is vanished.
     *
     * @param object the WrappedTextComponent as a json object.
     * @return true if it is a vanished player.
     * @author lukeeff
     */
    private boolean isVanishedPlayer(JsonObject object) {
        final JsonObject with = object.getAsJsonArray("with").get(0).getAsJsonObject();
        final String insertionName = with.getAsJsonPrimitive("insertion").getAsString();
        final Player player = Bukkit.getPlayer(insertionName);
        return player.isFlying(); //Replace with isVanished.
    }
    
}

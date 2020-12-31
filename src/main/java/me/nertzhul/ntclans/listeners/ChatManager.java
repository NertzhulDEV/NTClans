package me.nertzhul.ntclans.listeners;

import me.nertzhul.ntclans.NTClans;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {
    private final NTClans plugin = NTClans.getInstance();
    private final String chatFormat = plugin.getConfig().getString("Clans.chat-format");

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChatEvent (AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if (plugin .getConfig().getBoolean("Clans.useCustomChatFormat")){
            if (plugin.clanManager.getClanByPlayer(player) != null){
                e.setFormat(plugin.chatUtils.sendMessage(player, chatFormat).replace("%message%", e.getMessage().replace("%", "%%")));
            }
        }
    }
}

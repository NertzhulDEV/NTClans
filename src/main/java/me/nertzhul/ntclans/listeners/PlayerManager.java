package me.nertzhul.ntclans.listeners;

import me.nertzhul.ntclans.NTClans;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerManager implements Listener {
    private final NTClans plugin = NTClans.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (plugin.clanManager.getClanByPlayer((Player) e.getDamager()) == plugin.clanManager.getClanByPlayer((Player) e.getEntity()))
                if (!plugin.getConfig().getBoolean("Clans.allowFriendlyFire"))
                    e.setCancelled(true);
        }
    }
}

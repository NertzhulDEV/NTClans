package me.nertzhul.ntclans.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nertzhul.ntclans.NTClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Placeholders extends PlaceholderExpansion {
    private final NTClans plugin = NTClans.getInstance();

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "ntclans";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        if (player == null)
            return "";
        if (plugin.clanManager.getClanByPlayer(player) != null) {
            if (placeholder.equals("clan_name")) {
                return plugin.clanManager.getClanByPlayer(player).getClanDisplayName();
            }
            if (placeholder.equals("clan_tag")) {
                return plugin.clanManager.getClanByPlayer(player).getClanTag();
            }
            if (placeholder.equals("clan_owner")) {
                return Bukkit.getPlayer(plugin.clanManager.getClanByPlayer(player).getClanOwner()).getDisplayName();
            }
            if (placeholder.equals("clan_members")) {
                List<String> members = new ArrayList<>();
                for (UUID uuid : plugin.clanManager.getClanByPlayer(player).getClanMembers()){
                    members.add(Bukkit.getPlayer(uuid).getDisplayName());
                }
                return members.toString();
            }
        }
        return null;
    }
}

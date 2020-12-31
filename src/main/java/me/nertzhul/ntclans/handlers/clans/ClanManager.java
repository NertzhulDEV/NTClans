package me.nertzhul.ntclans.handlers.clans;

import me.nertzhul.ntclans.NTClans;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClanManager {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration yamlDatabase = plugin.dataHandler.getDatabase();

    public void createClan(Player player, String[] args) {
        String clanName = args[0].toLowerCase();
        String clanDisplayName = clanName.substring(0, 1).toUpperCase() + clanName.substring(1);
        String clanTag = clanName.substring(0, 3).toUpperCase();
        String clanOwner = player.getUniqueId().toString();
        List<String> clanMembers =  new ArrayList<>(yamlDatabase.getStringList("Clans." + clanName + ".members"));
        clanMembers.add(clanOwner);

        yamlDatabase.set("Clans." + clanName + ".displayName", clanDisplayName);
        yamlDatabase.set("Clans." + clanName + ".tag", clanTag);
        yamlDatabase.set("Clans." + clanName + ".owner", clanOwner);
        yamlDatabase.set("Clans." + clanName + ".members", clanMembers);
        plugin.dataHandler.saveDatabase();
        plugin.reloadClans();
    }

    public void deleteClan(Player player) {
        String clan = getClanByPlayer(player).getClanName();
        if (plugin.dataHandler.getDatabase().getConfigurationSection("Clans") != null){
            for (String clanName : plugin.dataHandler.getDatabase().getConfigurationSection("Clans").getKeys(false)){
                if (clanName.equals(clan)) {
                    yamlDatabase.set("Clans." + clanName, null);
                    plugin.dataHandler.saveDatabase();
                    plugin.reloadClans();
                }
            }
        }
    }

    public Clan getClanByName (String name) {
        for (Clan clan : plugin.getClans()){
            if (clan.getClanName().equalsIgnoreCase(name)){
                return clan;
            }
        }
        return null;
    }

    public Clan getClanByPlayer (Player player) {
        for (Clan clan : plugin.getClans()){
            if (clan.getClanMembers().contains(player.getUniqueId())){
                return clan;
            }
        }
        return null;
    }

}

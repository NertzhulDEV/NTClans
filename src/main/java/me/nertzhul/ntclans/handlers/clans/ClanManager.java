package me.nertzhul.ntclans.handlers.clans;

import me.nertzhul.ntclans.NTClans;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanManager {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();
    private FileConfiguration database = plugin.dataHandler.getDatabase();

    public void createClan(Player player, String[] args) {
        String clanName = args[0].toLowerCase();
        String clanDisplayName = clanName.substring(0, 1).toUpperCase() + clanName.substring(1);
        String clanTag = clanName.substring(0, 3).toUpperCase();
        String clanOwner = player.getUniqueId().toString();
        List<String> clanMembers =  new ArrayList<>(database.getStringList("Clans." + clanName + ".members"));
        clanMembers.add(clanOwner);

        database.set("Clans." + clanName + ".displayName", clanDisplayName);
        database.set("Clans." + clanName + ".tag", clanTag);
        database.set("Clans." + clanName + ".owner", clanOwner);
        database.set("Clans." + clanName + ".members", clanMembers);
        plugin.dataHandler.saveDatabase();
        plugin.reloadClans();
    }

    public void deleteClan(Player player) {
        String clan = getClanByPlayer(player).getClanName();
        if (plugin.dataHandler.getDatabase().getConfigurationSection("Clans") != null){
            for (String clanName : plugin.dataHandler.getDatabase().getConfigurationSection("Clans").getKeys(false)){
                if (clanName.equals(clan)) {
                    database.set("Clans." + clanName, null);
                    plugin.dataHandler.saveDatabase();
                    plugin.reloadClans();
                }
            }
        }
    }

    public void joinClan(Player player, Clan clan){
        List<String> clanMembers =  new ArrayList<>(database.getStringList("Clans." + clan.getClanName() + ".members"));
        clanMembers.add(player.getUniqueId().toString());
        database.set("Clans." + clan.getClanName() + ".members", clanMembers);
        plugin.dataHandler.saveDatabase();
        plugin.reloadClans();

        for (UUID clanMember : clan.getClanMembers()){
            Player member = Bukkit.getPlayer(clanMember);
            if (member != null && member != player)
                member.sendMessage(plugin.chatUtils.sendMessage(player, messages.getString("CLAN.MESSAGE_CLAN_JOINED")));
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

package me.nertzhul.ntclans.handlers.clans;

import me.nertzhul.ntclans.NTClans;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clan {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration yamlDatabase = plugin.dataHandler.getDatabase();

    private final String clanName;
    private final String clanDisplayName;
    private final String clanTag;
    private final UUID clanOwner;
    private final List<UUID> clanMembers = new ArrayList<>();

    public Clan (String clanName){
        this.clanName = clanName;
        this.clanDisplayName = yamlDatabase.getString("Clans." + clanName + ".displayName");
        this.clanTag = yamlDatabase.getString("Clans." + clanName + ".tag");
        this.clanOwner = UUID.fromString(yamlDatabase.getString("Clans." + clanName + ".owner"));
        for (String uuid : yamlDatabase.getStringList("Clans." + clanName + ".members")){
            this.clanMembers.add(UUID.fromString(uuid));
        }
    }

    public String getClanName() {
        return clanName;
    }
    public String getClanDisplayName() {
        return clanDisplayName;
    }
    public String getClanTag() {
        return clanTag;
    }
    public UUID getClanOwner() {
        return clanOwner;
    }
    public List<UUID> getClanMembers() {
        return clanMembers;
    }
}

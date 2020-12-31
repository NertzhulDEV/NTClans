package me.nertzhul.ntclans.utils;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.handlers.clans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatUtils {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();
    private FileConfiguration yamlDatabase = plugin.dataHandler.getDatabase();

    private String formatColor (String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String formatMessage (Clan clan, String message) {
        List<String> members = new ArrayList<>();
        for (UUID memberUUID : clan.getClanMembers()) {
            if (memberUUID.equals(clan.getClanOwner())) {
                members.add("&c" + Bukkit.getOfflinePlayer(memberUUID).getName() + "&r");
                continue;
            }
            members.add(Bukkit.getOfflinePlayer(memberUUID).getName());
        }

        message = message.replace("%prefix%", messages.getString("GENERAL.PREFIX"));
        message = message.replace("%clan_name%", clan.getClanDisplayName());
        message = message.replace("%clan_tag%", clan.getClanTag());
        message = message.replace("%clan_owner%", "&c" + Bukkit.getOfflinePlayer(clan.getClanOwner()).getName());
        message = message.replace("%clan_members%", members.toString());
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public < E > String sendMessage(E param, String message) {
        if (param == null) {
            message = message.replace("%prefix%", messages.getString("GENERAL.PREFIX"));
            return formatColor(message);
        } else {
            if (param instanceof Player) {
                message = message.replace("%player%", ((Player) param).getName());
                return formatMessage(plugin.clanManager.getClanByPlayer((Player) param), message);
            } else if (param instanceof String) {
                return formatMessage(plugin.clanManager.getClanByName((String) param), message);
            }
        }
        return null;
    }

    public String sendCenteredMessage(String message) {
        message = formatColor(message);
        int messagePxSize = 0;
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 120 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }

    public String getHR (int count) {
        return formatColor("&3&m" + repeat("▬", count));
    }
    public String spacer(int count) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < count; i++)
            message.append("\n");
        return message.toString();
    }
    public String repeat (String text, int count) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < count; i++)
            finalString.append(text);
        return finalString.toString();
    }
}

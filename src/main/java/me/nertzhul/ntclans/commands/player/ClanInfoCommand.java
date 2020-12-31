package me.nertzhul.ntclans.commands.player;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import me.nertzhul.ntclans.handlers.clans.Clan;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClanInfoCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    public ClanInfoCommand() {
        super("info", new String[0], null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (plugin.clanManager.getClanByPlayer(player) != null) {
                    for (String string : messages.getStringList("CLAN.MESSAGE_INFO_LAYOUT")) {
                        string = string.replace("%clan_info_header%", plugin.chatUtils.getHR(30) + plugin.chatUtils.sendMessage(null, "&3[ &b&lCLAN INFO &3]") + plugin.chatUtils.getHR(31));
                        string = string.replace("%clan_info_footer%", plugin.chatUtils.getHR(80));
                        player.sendMessage(plugin.chatUtils.sendMessage(player, string));
                    }
                } else {
                    player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_NO_CLAN")));
                }
            } else if (args.length == 1) {
                if (plugin.clanManager.getClanByName(args[0]) != null) {
                    for (String string : messages.getStringList("CLAN.MESSAGE_INFO_LAYOUT")) {
                        string = string.replace("%clan_info_header%", plugin.chatUtils.getHR(30) + plugin.chatUtils.sendMessage(null, "&3[ &b&lCLAN INFO &3]") + plugin.chatUtils.getHR(31));
                        string = string.replace("%clan_info_footer%", plugin.chatUtils.getHR(80));
                        player.sendMessage(plugin.chatUtils.sendMessage(args[0], string));
                    }
                } else {
                    player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_NOT_FOUND")));
                }
            }
        }

        return false;
    }

    @Override
    public void complete(CommandSender sender, String alias, List<String> params, List<String> suggestions) {
        if (params.size() != 1)
            return;

        List<String> clanNames = new ArrayList<>();
        for (Clan clan : plugin.getClans())
            clanNames.add(clan.getClanName());
        suggestByParameter(clanNames.stream(), suggestions, params.isEmpty() ? null : params.get(params.size() - 1));
    }
}

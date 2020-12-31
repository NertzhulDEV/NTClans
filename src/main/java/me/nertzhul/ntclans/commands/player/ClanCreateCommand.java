package me.nertzhul.ntclans.commands.player;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ClanCreateCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    public ClanCreateCommand() {
        super("create", new String[0], null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (plugin.clanManager.getClanByPlayer(player) != null){
                player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_ALREADY_IN_CLAN")));
                return false;
            } else {
                if (args.length == 1) {
                    if (args[0].length() < 3 || !args[0].matches("[a-zA-Z0-9_\\-]+")){
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_CREATE_INVALID_NAME")));
                        return false;
                    }
                    try {
                        plugin.clanManager.createClan(player, args);
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_CREATE_SUCCESS")));
                    } catch (Exception e) {
                        Bukkit.getLogger().warning(e.getMessage());
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.MESSAGE_COMMAND_FAIL")));
                    }
                } else {
                    player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_CREATE_INVALID_NAME")));
                    return false;
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.chatUtils.sendMessage(null, "&cThis command can only be performed by a &6Player&c."));
        }

        return false;
    }
}

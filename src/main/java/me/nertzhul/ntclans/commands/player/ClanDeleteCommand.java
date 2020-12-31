package me.nertzhul.ntclans.commands.player;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ClanDeleteCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    public ClanDeleteCommand() {
        super("delete", new String[0], null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (plugin.clanManager.getClanByPlayer(player) == null){
                player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_NO_CLAN")));
                return true;
            } else if (!player.getUniqueId().equals(plugin.clanManager.getClanByPlayer(player).getClanOwner())){
                player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_DELETE_PERMISSION")));
                return true;
            } else {
                if (args.length == 0) {
                    try {
                        plugin.clanManager.deleteClan(player);
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_DELETE_SUCCESS")));
                    } catch (Exception e) {
                        Bukkit.getLogger().warning(e.getMessage());
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.MESSAGE_COMMAND_FAIL")));
                    }
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.chatUtils.sendMessage(null, "&cThis command can only be performed by a &6Player&c."));
        }

        return false;
    }
}

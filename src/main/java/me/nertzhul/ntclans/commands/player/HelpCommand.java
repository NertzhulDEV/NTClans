package me.nertzhul.ntclans.commands.player;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    public HelpCommand() {
        super("help", new String[0], null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;

            switch (args.length) {
                case 0:
                    if (player.hasPermission("ntclans.*") || player.hasPermission(this.getPermission())){
                        player.sendMessage(plugin.chatUtils.getHR(28) + plugin.chatUtils.sendMessage(null, "&3[ &b&lNTCLANS HELP &3]") + plugin.chatUtils.getHR(28));
                        player.sendMessage(plugin.chatUtils.spacer(1));
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_HELP")));
                        if (player.hasPermission("ntclans.*") || player.hasPermission("ntclans.create"))
                            player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_CREATE")));
                        if (player.hasPermission("ntclans.*") || player.hasPermission("ntclans.delete"))
                            player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_DELETE")));
                        if (player.hasPermission("ntclans.*") || player.hasPermission("ntclans.invite"))
                            player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_INVITE")));
                        if (player.hasPermission("ntclans.*") || player.hasPermission("ntclans.info"))
                            player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_INFO")));
                        if (player.hasPermission("ntclans.admin.*") || player.hasPermission("ntclans.admin.reload"))
                            player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_RELOAD")));
                        player.sendMessage(plugin.chatUtils.spacer(1));
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("GENERAL.ARGS_HINT")));
                        player.sendMessage(plugin.chatUtils.getHR(31) + plugin.chatUtils.sendMessage(null, "&3[ &b&lPAGE 1/1 &3]") + plugin.chatUtils.getHR(32));
                    }
                    break;
            }
        } else {
            sender.sendMessage(plugin.chatUtils.getHR(28) + plugin.chatUtils.sendMessage(null, "&3[ &b&lNTCLANS HELP &3]") + plugin.chatUtils.getHR(28));
            sender.sendMessage(plugin.chatUtils.spacer(1));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_HELP")));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_CREATE")));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_DELETE")));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_INVITE")));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_INFO")));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.DISPLAY_RELOAD")));
            sender.sendMessage(plugin.chatUtils.spacer(1));
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("GENERAL.ARGS_HINT")));
            sender.sendMessage(plugin.chatUtils.getHR(31) + plugin.chatUtils.sendMessage(null, "&3[ &b&lPAGE 1/1 &3]") + plugin.chatUtils.getHR(32));
        }
        return false;
    }
}

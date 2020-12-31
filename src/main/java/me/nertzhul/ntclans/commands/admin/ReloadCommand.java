package me.nertzhul.ntclans.commands.admin;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ReloadCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    public ReloadCommand() {
        super("reload", new String[0], "ntclans.admin.reload");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try {
            plugin.dataHandler.reloadFiles();
            plugin.reloadConfig();
            plugin.reloadClans();
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.MESSAGE_RELOAD_COMPLETE")));
        } catch (Exception e) {
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.MESSAGE_COMMAND_FAIL")));
            e.printStackTrace();
        }


        return false;
    }
}

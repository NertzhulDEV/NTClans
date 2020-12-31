package me.nertzhul.ntclans.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.admin.DebugCommand;
import me.nertzhul.ntclans.commands.admin.ReloadCommand;
import me.nertzhul.ntclans.commands.player.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Stream;

public class ClansCommand implements CommandExecutor, TabCompleter {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    private static final List<SubCommand> COMMANDS = (List<SubCommand>) ImmutableList.of(
            new HelpCommand(), new ClanCreateCommand(), new ClanDeleteCommand(), new ClanInviteCommand(),new ClanInfoCommand(), new DebugCommand(), new ReloadCommand());
    private final Map<String, SubCommand> commands;

    public ClansCommand() {
        ImmutableMap.Builder<String, SubCommand> commands = ImmutableMap.builder();
        for (SubCommand command : COMMANDS)
            command.getLabels().forEach(label -> commands.put(label.toString(), command));
        this.commands = (Map<String, SubCommand>)commands.build();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("GENERAL.HELP").replace("%version%", plugin.getDescription().getVersion())));
            return true;
        }
        String search = args[0].toLowerCase();
        SubCommand target = this.commands.get(search);
        if (target == null) {
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.MESSAGE_COMMAND_NOT_FOUND")));
            return true;
        }
        String permission = target.getPermission();
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("COMMAND.MESSAGE_NO_PERMISSION")));
            return true;
        }
        List<String> newArgs = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            newArgs.add(args[i]);
        }
        target.execute(sender, newArgs.toArray(new String[0]));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length > 1) {
            SubCommand target = this.commands.get(args[0].toLowerCase());
            if (target != null)
                target.complete( sender, args[0].toLowerCase(),
                        Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), suggestions);
            return suggestions;
        }
        Stream<String> targets = SubCommand.filterByPermission(sender, this.commands.values().stream()).map(SubCommand::getLabels).flatMap(Collection::stream);
        SubCommand.suggestByParameter(targets, suggestions, (args.length == 0) ? null : args[0]);
        return suggestions;
    }
}

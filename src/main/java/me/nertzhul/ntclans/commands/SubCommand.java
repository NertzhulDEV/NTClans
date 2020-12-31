package me.nertzhul.ntclans.commands;

import com.google.common.collect.ImmutableSet;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public abstract class SubCommand {
    private final String name;
    private final List<String> aliases;
    private final String permission;

    public SubCommand(String name, String[] aliases, String permission) {
        this.name = name;
        this.aliases = Arrays.asList(aliases);
        if (permission != null)
            this.permission = permission;
        else this.permission = "ntclans." + name;
    }

    public static Stream<SubCommand> filterByPermission(CommandSender sender, Stream<SubCommand> commands) {
        return commands.filter(target -> (target.getPermission() == null || sender.hasPermission(target.getPermission())));
    }
    public static void suggestByParameter(@NotNull Stream<String> possible, @NotNull List<String> suggestions, @Nullable String parameter) {
        if (parameter == null) {
            possible.forEach(suggestions::add);
        } else {
            possible.filter(suggestion -> suggestion.toLowerCase().startsWith(parameter.toLowerCase())).forEach(suggestions::add);
        }
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public void complete(CommandSender sender, String alias, List<String> params, List<String> suggestions) {}

    public String getName () {
        return name;
    }
    public final Set<String> getAliases() {
        return (Set<String>)ImmutableSet.copyOf(this.aliases);
    }
    public String getPermission() {
        return permission;
    }

    @NotNull
    public final List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        labels.add(this.name);
        labels.addAll(this.aliases);
        return labels;
    }
}

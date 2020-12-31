package me.nertzhul.ntclans.commands.player;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import me.nertzhul.ntclans.handlers.clans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClanInviteCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration messages = plugin.dataHandler.getMessages();

    public ClanInviteCommand() {
        super("invite", new String[0], null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_INVITE_INVALID")));
                return true;
            }
            switch (args[0]){
                case "accept":
                    if (plugin.getClanInvites().get(player) == null){
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_NO_INVITE")));
                        return true;
                    }
                    Player clanOwner = Bukkit.getPlayer(plugin.getClanInvites().get(player).getClanOwner());
                    plugin.clanManager.joinClan(player, plugin.getClanInvites().get(player));

                    player.sendMessage(plugin.chatUtils.sendMessage(plugin.getClanInvites().get(player).getClanName(), messages.getString("CLAN.MESSAGE_INVITE_INVITED_ACCEPTED")));
                    clanOwner.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_INVITE_ACCEPTED")));
                    plugin.getClanInvites().remove(player);
                    break;
                case "deny":
                    if (plugin.getClanInvites().get(player) == null){
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_NO_INVITE")));
                        return true;
                    }
                    Player clanOwner1 = Bukkit.getPlayer(plugin.getClanInvites().get(player).getClanOwner());

                    player.sendMessage(plugin.chatUtils.sendMessage(plugin.getClanInvites().get(player).getClanName(), messages.getString("CLAN.MESSAGE_INVITE_INVITED_DENIED")));
                    clanOwner1.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_INVITE_DENIED")));
                    plugin.getClanInvites().remove(player);
                    break;
                default:
                    if (plugin.clanManager.getClanByPlayer(player) == null) {
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_NO_CLAN")));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[0]) == null) {
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_INVITE_INVALID_PLAYER")));
                        return true;
                    }
                    if (plugin.clanManager.getClanByPlayer(Bukkit.getPlayer(args[0])) != null) {
                        player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_INVITE_ALREADY_IN_CLAN")));
                        return true;
                    }

                    Player invitedPlayer = Bukkit.getPlayer(args[0]);
                    plugin.clanInvites.put(invitedPlayer, plugin.clanManager.getClanByPlayer(player));
                    invitePlayer(invitedPlayer, plugin.clanManager.getClanByPlayer(player));
                    player.sendMessage(plugin.chatUtils.sendMessage(null, messages.getString("CLAN.MESSAGE_INVITE_SUCCESS")));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.chatUtils.sendMessage(null, "&cThis command can only be performed by a &6Player&c."));
        }
        return false;
    }

    public void invitePlayer(Player player, Clan clan) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getClanInvites().remove(player);
            }
        }, 5 * 60 * 20);
        player.sendMessage(plugin.chatUtils.sendMessage(clan.getClanName(), messages.getString("CLAN.MESSAGE_INVITE_INVITED")));
    }

    @Override
    public void complete(CommandSender sender, String alias, List<String> params, List<String> suggestions) {
        if (params.size() != 1)
            return;

        List<String> suggests = new ArrayList<>();
        if (plugin.clanManager.getClanByPlayer((Player) sender) != null){
            for (Player player : Bukkit.getOnlinePlayers())
                suggests.add(player.getName());
        } else {
            suggests.addAll(Arrays.asList("accept", "deny"));
        }
        suggestByParameter(suggests.stream(), suggestions, params.isEmpty() ? null : params.get(params.size() - 1));
    }
}

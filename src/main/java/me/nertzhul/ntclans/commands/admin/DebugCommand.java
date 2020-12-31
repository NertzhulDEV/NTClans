package me.nertzhul.ntclans.commands.admin;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.commands.SubCommand;
import me.nertzhul.ntclans.handlers.clans.Clan;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DebugCommand extends SubCommand {
    private final NTClans plugin = NTClans.getInstance();

    public DebugCommand() {
        super("debug", new String[0], "ntclans.admin.debug");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;

            if (plugin.getClans() == null){
                player.sendMessage(plugin.getPrefix() + "No clans found.");
            }

            TextComponent text = new TextComponent("§3|§7> §3Click a button to respond. ");
            TextComponent click = new TextComponent("§b[§nACCEPT§b]");
            TextComponent clickb = new TextComponent(" §7| ");
            TextComponent click2 = new TextComponent("§4[§nDENY§4]");
            click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text("§3Click to accept the request from '" + player.getName() + "'."))));
            click2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text("§3Click to deny the request from '" + player.getName() + "'."))));
            click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bc &aACCEPT"));
            click2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bc &cDENY"));

            text.addExtra(click);
            text.addExtra(clickb);
            text.addExtra(click2);

            player.spigot().sendMessage(text);

            for (Clan clan : plugin.getClans()){
                player.sendMessage(clan.getClanName());
                player.sendMessage(clan.getClanDisplayName());
                player.sendMessage(clan.getClanTag());
                player.sendMessage(Bukkit.getPlayer(clan.getClanOwner()).getName());
                for (UUID member : clan.getClanMembers()){
                    player.sendMessage("Member: " + Bukkit.getPlayer(member).getName());
                }
            }
        }

        return false;
    }
}

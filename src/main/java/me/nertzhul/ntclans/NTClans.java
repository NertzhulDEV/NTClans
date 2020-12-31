package me.nertzhul.ntclans;

import me.nertzhul.ntclans.commands.ClansCommand;
import me.nertzhul.ntclans.handlers.clans.Clan;
import me.nertzhul.ntclans.handlers.clans.ClanManager;
import me.nertzhul.ntclans.handlers.database.Database;
import me.nertzhul.ntclans.handlers.database.DatabaseManager;
import me.nertzhul.ntclans.listeners.ChatManager;
import me.nertzhul.ntclans.utils.ChatUtils;
import me.nertzhul.ntclans.utils.DataHandler;
import me.nertzhul.ntclans.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class NTClans extends JavaPlugin {
    public static NTClans instance;
    public final String prefix = "§7[§b" + getDescription().getName() + "§7] §r";
    public DataHandler dataHandler;
    public Database database;
    public DatabaseManager databaseManager;
    public ClanManager clanManager;
    public ChatUtils chatUtils;
    public ChatManager chatManager;
    public List<Clan> clans = new ArrayList<>();
    public HashMap<Player, Clan> clanInvites = new HashMap<>();

    public static NTClans getInstance() {
        return instance;
    }
    public String getPrefix() {
        return prefix;
    }
    public List<Clan> getClans() {
        return clans;
    }
    public HashMap<Player, Clan> getClanInvites() {
        return clanInvites;
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + " _   _ _____ _____  _       ___   _   _  _____");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "| \\ | |_   _/  __ \\| |     / _ \\ | \\ | |/  ___|");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "|  \\| | | | | /  \\/| |    / /_\\ \\|  \\| |\\ `--.");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "| . ` | | | | |    | |    |  _  || . ` | `--. \\");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "| |\\  | | | | \\__/\\| |____| | | || |\\  |/\\__/ /");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\\_| \\_/ \\_/  \\____/\\_____/\\_| |_/\\_| \\_/\\____/");
        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " Version " + getDescription().getVersion() + " - CreatedBy: Nertzhul#3788");

        instance = this;
        if (isUsingPlaceholderAPI()) {
            new Placeholders().register();
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + "§aPlaceHolderAPI Detected! Loading custom NTClans placeholders.");
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + "§aPlaceHolders available: %ntclans_clan_name%, %ntclans_clan_tag%, %ntclans_clan_owner%, %ntclans_clan_members%.");
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + "- PlaceholderAPI not found, only built-in placeholders will work!");
        }
        setupFiles();
        loadClasses();
        setupDatabase();
        reloadClans();

    }

    @Override
    public void onDisable() {
        instance = null;
        database.closeConnection();
    }

    private void setupFiles () {
        try {
            saveDefaultConfig();
        } catch (Exception ignored){
            getConfig().addDefault("Data.storage-method", "YAML");
            getConfig().addDefault("Data.enableMySQL", false);
            getConfig().addDefault("Data.host", "localhost");
            getConfig().addDefault("Data.port", 3306);
            getConfig().addDefault("Data.database", "minecraft");
            getConfig().addDefault("Data.username", "root");
            getConfig().addDefault("Data.password", "");
            getConfig().addDefault("Data.properties.useSSL", false);
            getConfig().addDefault("Data.properties.useUnicode", true);
            getConfig().addDefault("Data.properties.characterEncoding", "utf8");
            getConfig().addDefault("Data.properties.tablePrefix", "ntclans_");
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        for (String fileName : Arrays.asList("messages","database")) {
            File file = new File(getDataFolder() + File.separator + fileName + ".yml");
            if (!file.exists())
                this.saveResource(fileName + ".yml", false);
        }

    }
    public void loadClasses(){
        this.dataHandler = new DataHandler();
        this.database = new Database();
        this.databaseManager = new DatabaseManager();
        this.clanManager = new ClanManager();
        this.chatManager = new ChatManager();
        this.chatUtils = new ChatUtils();
        getCommand("ntclans").setExecutor((CommandExecutor) new ClansCommand());
        getCommand("ntclans").setTabCompleter((TabCompleter) new ClansCommand());
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
    }

    public void setupDatabase(){
        database.databaseStartup();
        if (database.isConnected()){
            databaseManager.createTables();
        }
    }

    public void reloadClans () {
        this.clans.clear();
        if (database.getStorageMethod().equalsIgnoreCase("yaml")){
            if (dataHandler.getDatabase().getConfigurationSection("Clans") != null){
                for (String clanName : Objects.requireNonNull(dataHandler.getDatabase().getConfigurationSection("Clans")).getKeys(false)){
                    Clan clan = new Clan(clanName);
                    this.clans.add(clan);
                }
            }
        }
    }

    public boolean isUsingPlaceholderAPI() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}

package me.nertzhul.ntclans.utils;

import me.nertzhul.ntclans.NTClans;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataHandler {
    private final NTClans plugin = NTClans.getInstance();

    public File messagesFile;
    public File databaseFile;

    public FileConfiguration messages;
    public FileConfiguration database;

    public DataHandler() {
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        this.databaseFile = new File(plugin.getDataFolder(), "database.yml");
        this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);
        this.database = YamlConfiguration.loadConfiguration(this.databaseFile);

    }

    public void reloadFiles() {
        try {
            this.messages.load(this.messagesFile);
            this.database.load(this.databaseFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessages () {
        return messages;
    }
    public FileConfiguration getDatabase() {
        return database;
    }
    public void saveDatabase() {
        try {
            this.database.save(this.databaseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package me.nertzhul.ntclans.handlers.database;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.utils.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final NTClans plugin = NTClans.getInstance();
    private final String prefix = plugin.getPrefix();
    private FileConfiguration yamlDatabase = plugin.dataHandler.getDatabase();

    public Connection connection = null;
    public Connection getConnection() {
        return connection;
    }

    private final String storageMethod = plugin.getConfig().getString("Data.storage-method").toLowerCase();
    public String getStorageMethod() {
        return storageMethod;
    }

    private final String host = plugin.getConfig().getString("Data.host");
    private final String port = plugin.getConfig().getString("Data.port");
    private final String database = plugin.getConfig().getString("Data.database");
    private final String username = plugin.getConfig().getString("Data.username");
    private final String password = plugin.getConfig().getString("Data.password");
    private final String useSSL = plugin.getConfig().getString("Data.properties.useSSL");

    public void databaseStartup(){
        switch (storageMethod) {
            case "mysql":
                openMySQLConnection();
                break;
            case "sqlite":
                openSQLiteConnection();
                break;
            default:
                setupYamlDatabase();
                break;
        }
    }

    public void setupYamlDatabase(){
        Bukkit.getConsoleSender().sendMessage(prefix + "§eUsing default YAML database.");
    }

    // Remote database
    public boolean isConnected(){
        return connection != null;
    }

    public void openMySQLConnection() {
        if (plugin.getConfig().getBoolean("Data.enableMySQL")) {
            String URL = "jdbc:mysql://" + host + ":" + port + "/" + database+ "?useSSL=" + useSSL;
            try {
                Bukkit.getConsoleSender().sendMessage(prefix + "§eAttempting connection to the MySQL database.");
                connection = DriverManager.getConnection(URL, username, password);
                Bukkit.getConsoleSender().sendMessage(prefix + "§aConnected to the MySQL database successfully!");
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(prefix + "§cConnection to the MySQL database has failed!");
                Bukkit.getConsoleSender().sendMessage(prefix + "§eSwitching storage mode to SQLite!");
                plugin.getConfig().set("Data.storage-method", "SQLite");
                plugin.saveConfig();
                openSQLiteConnection();
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(prefix + "§eMake sure you also enable the MySQL connection in your config.");
            Bukkit.getConsoleSender().sendMessage(prefix + "§eSwitching storage mode to SQLite!");
            plugin.getConfig().set("Data.storage-method", "SQLite");
            plugin.saveConfig();
            openSQLiteConnection();
        }

    }

    public void openSQLiteConnection(){
        File file = new File(plugin.getDataFolder(), "database.db");
        String URL = "jdbc:sqlite:" + file;
        try {
            Bukkit.getConsoleSender().sendMessage(prefix + "§eAttempting connection to the SQLite database.");
            connection = DriverManager.getConnection(URL);
            Bukkit.getConsoleSender().sendMessage(prefix + "§aConnected to the SQLite database successfully!");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§cConnection to the SQLite database has failed!");
            Bukkit.getConsoleSender().sendMessage(prefix + "§eSwitching storage mode to YAML!");
            plugin.getConfig().set("Data.storage-method", "YAML");
            plugin.saveConfig();
        }
    }

    public void closeConnection(){
        if (isConnected()){
            try {
                Bukkit.getConsoleSender().sendMessage(prefix + "§eAttempting to close the database connection.");
                connection.close();
                Bukkit.getConsoleSender().sendMessage(prefix + "§aConnection to the database successfully closed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

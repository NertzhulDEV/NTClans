package me.nertzhul.ntclans.handlers.database;

import me.nertzhul.ntclans.NTClans;
import me.nertzhul.ntclans.utils.DataHandler;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private final NTClans plugin = NTClans.getInstance();
    private FileConfiguration yamlDatabase = plugin.dataHandler.getDatabase();

    public void createTables(){
        PreparedStatement ps;
        try {
            ps = plugin.database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("data.table-prefix") + "clans "
            + "(NAME varchar(50) PRIMARY KEY,TAG char(5),OWNER char(36),MEMBERS varchar(100))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

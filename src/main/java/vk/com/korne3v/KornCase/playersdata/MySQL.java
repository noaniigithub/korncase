package vk.com.korne3v.KornCase.playersdata;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vk.com.korne3v.KornCase.Main;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class MySQL {

    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static String port;
    public static Connection con;

    public MySQL(final String user, final String pass, final String host2, final String dB) {
    }

    public static void connect() {
        if (!isConnected()) {
            try {
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] MySQL connecting..");
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] Host: "+MySQL.host);
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] Port: "+MySQL.port);
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] Database: "+MySQL.database);
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] Password: §c"+ RandomStringUtils.random(MySQL.password.length(),"*"));
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] User: "+MySQL.username);
                MySQL.con = DriverManager.getConnection("jdbc:mysql://" + MySQL.host + ":" + MySQL.port + "/" + MySQL.database + "?user=" + MySQL.username + "&password=" + MySQL.password + "&autoReconnect=false");
                Bukkit.getConsoleSender().sendMessage("§a[MySQL] MySQL connected!");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        if (isConnected()) {
            try {
                MySQL.con.close();
                Bukkit.getConsoleSender().sendMessage("§c[MySQL] MySQL disconnected");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return (MySQL.con != null);
    }

    public static void createTable() {
        if (isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS CASES (UUID VARCHAR(100),COUNT int)");
                Bukkit.getConsoleSender().sendMessage("§c[MySQL] MySQL Table created");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(final String qry) {
        if (isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(qry);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(final String qry) {
        ResultSet rs = null;
        try {
            final Statement st = MySQL.con.createStatement();
            rs = st.executeQuery(qry);
        }
        catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }

    public static File getMySQLFile() {
        return new File(Main.getInstance().getDataFolder(), "mysql.yml");
    }

    public static FileConfiguration getMySQLFileConfiguration() {
        return YamlConfiguration.loadConfiguration(getMySQLFile());
    }

    public static void setStandardMySQL() {
        final FileConfiguration cfg = getMySQLFileConfiguration();
        cfg.options().copyDefaults(true);
        cfg.addDefault("username", "korncase");
        cfg.addDefault("password", "password");
        cfg.addDefault("database", "database");
        cfg.addDefault("host", "localhost");
        cfg.addDefault("port", "3306");
        try {
            cfg.save(getMySQLFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readMySQL() {
        final FileConfiguration cfg = getMySQLFileConfiguration();
        MySQL.username = cfg.getString("username");
        MySQL.password = cfg.getString("password");
        MySQL.database = cfg.getString("database");
        MySQL.host = cfg.getString("host");
        MySQL.port = cfg.getString("port");
    }
}


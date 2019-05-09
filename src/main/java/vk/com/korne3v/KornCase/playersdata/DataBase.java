package vk.com.korne3v.KornCase.playersdata;

import org.bukkit.Bukkit;
import vk.com.korne3v.KornCase.Main;

import java.sql.*;

public class DataBase {

    private static Connection con;

    public static void connect() {
        if (!isConnected()) {
            String url = Main.getInstance().getConfig().getString("Database.Url");
            Bukkit.getConsoleSender().sendMessage("§b["+Main.getInstance().getDescription().getName()+"] §fDataBase connecting..");
            Bukkit.getConsoleSender().sendMessage("§b["+Main.getInstance().getDescription().getName()+"] §eUrl: §6" + url);
            try {
                DataBase.con = DriverManager.getConnection(url);
                DataBase.createTable();
                Bukkit.getConsoleSender().sendMessage("§b["+Main.getInstance().getDescription().getName()+"] §aDatabase connected!");
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage("§b["+Main.getInstance().getDescription().getName()+"] §c Error connect check your URL:");
                Bukkit.getConsoleSender().sendMessage("§b["+Main.getInstance().getDescription().getName()+"] §c " + url);
            }
        }
    }
    public static void close() {
        if (isConnected()) {
            try {
                DataBase.con.close();
                Bukkit.getConsoleSender().sendMessage("§c  Database disconnected!");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return (DataBase.con != null);
    }

    public static void createTable() {
        if (isConnected()) {
            try {
                DataBase.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS CASES (UUID VARCHAR(100),COUNT int)");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(String qry) {
        if (isConnected()) {
            try {
                DataBase.con.createStatement().executeUpdate(qry);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet get(String qry) {
        ResultSet rs = null;
        try {
            final Statement st = DataBase.con.createStatement();
            rs = st.executeQuery(qry);
        }
        catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }
}


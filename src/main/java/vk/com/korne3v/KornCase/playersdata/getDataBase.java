package vk.com.korne3v.KornCase.playersdata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class getDataBase {

    public static boolean playerExists(final String uuid) {
        try {
            final ResultSet rs = MySQL.getResult("SELECT * FROM CASES WHERE UUID='" + uuid + "'");
            return rs.next() && rs.getString("UUID") != null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createPlayer(final String uuid) {
        if (!playerExists(uuid)) {
            MySQL.update("INSERT INTO CASES (UUID, COUNT) VALUES ('" + uuid + "', '0');");
        }
    }

    public static Integer getCases(final String uuid) {
        Integer i = 0;
        if (playerExists(uuid)) {
            try {
                final ResultSet rs = MySQL.getResult("SELECT * FROM CASES WHERE UUID='" + uuid + "'");
                if (rs.next()) {
                    rs.getInt("COUNT");
                }
                i = rs.getInt("COUNT");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public static void setCases(final String uuid, final Integer coins) {
        if (playerExists(uuid)) {
            MySQL.update("UPDATE CASES SET COUNT='" + coins + "' WHERE UUID='" + uuid + "'");
        }
    }

    public static void addCases(final String uuid, final Integer coins) {
        if (playerExists(uuid)) {
            setCases(uuid, getCases(uuid) + coins);
        }
    }

    public static void removeCases(final String uuid, final Integer coins) {
        if (playerExists(uuid)) {
            setCases(uuid, getCases(uuid) - coins);
        }
    }
}

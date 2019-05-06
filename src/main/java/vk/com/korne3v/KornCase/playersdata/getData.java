package vk.com.korne3v.KornCase.playersdata;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import vk.com.korne3v.KornCase.Main;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class getData {

    private static File file = new File(Main.getInstance().getDataFolder(),"data.yml");
    private static YamlConfiguration config;

    public static void fileLoad(){
        if (!file.exists()) {
            if (Main.getInstance().getResource("data.yml") != null) {
                Main.getInstance().saveResource("data.yml", false);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static YamlConfiguration getData() {
        return config;
    }
    
    public static void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean check(UUID uuid){
        if(config.getBoolean("BaseDataEnabled")){
            if(getDataBase.playerExists(uuid.toString())) {
                return true;
            }
        } else {
            if(config.getConfigurationSection("players." + Bukkit.getPlayer(uuid).getName()) != null){
                return true;
            }
        }
        return false;
    }

    public static void create(UUID uuid){
        if(config.getBoolean("BaseDataEnabled")){
            if(!getDataBase.playerExists(uuid.toString())) {
                getDataBase.createPlayer(uuid.toString());
            }
        }
        else {
            if(config.getConfigurationSection("players." + Bukkit.getPlayer(uuid).getName()) == null){
                config.createSection("players."+Bukkit.getPlayer(uuid).getName());
                save();
            }
        }
    }

    public static Integer getCases(UUID uuid){
        if(config.getBoolean("BaseDataEnabled"))
            return getDataBase.getCases(uuid.toString());
        else
            return config.getInt("players."+ Bukkit.getPlayer(uuid).getName());
    }

    public static void addCases(UUID uuid,int count){
        if(config.getBoolean("BaseDataEnabled"))
            getDataBase.addCases(uuid.toString(),count);
        else
            config.set("players."+ Bukkit.getPlayer(uuid).getName(),getCases(uuid)+count);
        save();
    }

    public static void removeCases(UUID uuid,int count){
        if(config.getBoolean("BaseDataEnabled"))
            getDataBase.removeCases(uuid.toString(),count);
        else
            config.set("players."+ Bukkit.getPlayer(uuid).getName(),getCases(uuid)-count);
        save();
    }

    public static void setCases(UUID uuid,int count){
        if(config.getBoolean("BaseDataEnabled"))
            getDataBase.setCases(uuid.toString(),count);
        else
            config.set("players."+ Bukkit.getPlayer(uuid).getName(),count);
        save();
    }
}

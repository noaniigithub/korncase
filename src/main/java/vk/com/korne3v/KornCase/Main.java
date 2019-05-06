package vk.com.korne3v.KornCase;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import vk.com.korne3v.KornCase.api.CasesPlaceHolders;
import vk.com.korne3v.KornCase.events.EventManager;
import vk.com.korne3v.KornCase.playersdata.MySQL;
import vk.com.korne3v.KornCase.playersdata.getData;
import vk.com.korne3v.KornCase.system.Case;
import vk.com.korne3v.KornCase.system.CaseLoader;

import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("§a["+this.getDescription().getName()+"] Selected language = "+Utils.getLang());

        if(Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
            if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("HolographicDisplays"));
                Bukkit.getConsoleSender().sendMessage("§a[" + this.getDescription().getName() + "] HolographicDisplays loaded!");
            }
        }else {
            Bukkit.getConsoleSender().sendMessage("§c[" + this.getDescription().getName() + "] plugin §lHolographicDisplays§c not found..");
            Bukkit.getConsoleSender().sendMessage("§c[" + this.getDescription().getName() + "] please download: §ehttps://dev.bukkit.org/projects/holographic-displays");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if(getConfig().getBoolean("PlaceholdersEnabled"))
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("PlaceholderAPI"));
                    Bukkit.getConsoleSender().sendMessage("§a[" + this.getDescription().getName() + "] PlaceholderAPI loaded!");
                }
                new CasesPlaceHolders().register();
            }else{
                Bukkit.getConsoleSender().sendMessage("§c["+this.getDescription().getName()+"] plugin §lPlaceholderAPI§c not found..");
                Bukkit.getConsoleSender().sendMessage("§c["+this.getDescription().getName()+"] please download: §ehttps://www.spigotmc.org/resources/placeholderapi.6245/");
                Bukkit.getPluginManager().disablePlugin(this);
            }

        this.getCommand("case").setExecutor(new CommandManager());

        this.saveDefaultConfig();

        new CaseLoader();
        getData.fileLoad();

        if(getConfig().getBoolean("BaseDataEnabled"))
            ConnectMySQL();

        Bukkit.getPluginManager().registerEvents(new EventManager(),this);
    }

    public void ConnectMySQL() {
        MySQL.setStandardMySQL();
        MySQL.readMySQL();
        MySQL.connect();
        MySQL.createTable();
    }

    public void DisconnectMySQL(){
        try {
            MySQL.getMySQLFileConfiguration().save(MySQL.getMySQLFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MySQL.close();
    }

    public static void ReLoad(){
        Bukkit.getConsoleSender().sendMessage("§a["+instance.getDescription().getName()+"] START PLUGIN RELOADING..");
        instance.saveDefaultConfig();
        CaseLoader.save();
        Bukkit.getPluginManager().disablePlugin(instance);
        Bukkit.getPluginManager().enablePlugin(instance);
    }

    @Override
    public void onDisable() {
        if(getConfig().getBoolean("BaseDataEnabled"))
            DisconnectMySQL();
        for(Case CASE : CaseLoader.getMapCases.values())
            if(CASE.getHologram() != null)
                CASE.getHologram().delete();
    }

    public static Main getInstance() {
        return instance;
    }
}

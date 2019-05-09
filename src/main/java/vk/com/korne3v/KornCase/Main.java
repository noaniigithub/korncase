package vk.com.korne3v.KornCase;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import vk.com.korne3v.KornCase.api.CasesPlaceHolders;
import vk.com.korne3v.KornCase.events.EventManager;
import vk.com.korne3v.KornCase.netminecraftserver.NMS;
import vk.com.korne3v.KornCase.playersdata.DataBase;
import vk.com.korne3v.KornCase.playersdata.getData;
import vk.com.korne3v.KornCase.system.Case;
import vk.com.korne3v.KornCase.system.CaseLoader;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("§3---------------------------§r");
        Bukkit.getConsoleSender().sendMessage("                             ");
        Bukkit.getConsoleSender().sendMessage("§b          KornCase        §r");
        Bukkit.getConsoleSender().sendMessage("§7      plugin by korne3v    ");
        Bukkit.getConsoleSender().sendMessage("                             ");
        Bukkit.getConsoleSender().sendMessage("§8 ------------------------- §r");
        Bukkit.getConsoleSender().sendMessage("                             ");
        Bukkit.getConsoleSender().sendMessage("§f  Version: §a" + Main.getInstance().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§f  Core: §6" + Bukkit.getName() + " " +Bukkit.getBukkitVersion());
        NMS.configSoundAndEffectsFix();

        this.saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage("§f  Selected language: §a"+Utils.getLang());

        if(Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
            if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("HolographicDisplays"));
                Bukkit.getConsoleSender().sendMessage("§a  + §fHolographicDisplays loaded!");
            }
        }else {
            Bukkit.getConsoleSender().sendMessage("§c  plugin HolographicDisplays not found..");
            Bukkit.getConsoleSender().sendMessage("§c  please download: §7https://dev.bukkit.org/projects/holographic-displays");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if(getConfig().getBoolean("PlaceholdersEnabled"))
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("PlaceholderAPI"));
                    new CasesPlaceHolders().register();
                    Bukkit.getConsoleSender().sendMessage("§a  + §fPlaceholderAPI loaded!");
                }
            }else{
                Bukkit.getConsoleSender().sendMessage("§c  plugin PlaceholderAPI not found..");
                Bukkit.getConsoleSender().sendMessage("§c  please download: §7https://www.spigotmc.org/resources/placeholderapi.6245/");
            }

        this.getCommand("case").setExecutor(new CommandManager());

        new CaseLoader();
        getData.fileLoad();

        if(getConfig().getBoolean("Database.Enabled"))
            Bukkit.getScheduler().runTaskLaterAsynchronously(this, Main::Connect, 5);

        Bukkit.getPluginManager().registerEvents(new EventManager(),this);
        Bukkit.getConsoleSender().sendMessage("§f                            ");
        Bukkit.getConsoleSender().sendMessage("§8 ------------------------- §r");
        Bukkit.getConsoleSender().sendMessage("§7      vk.com/korne3v     ");
        Bukkit.getConsoleSender().sendMessage("§3---------------------------§r");
    }

    public static void Connect() {
        DataBase.connect();
    }

    public static void ReLoad() {
        Bukkit.getConsoleSender().sendMessage("§b["+instance.getDescription().getName()+"] Starts plugin reload..");
        CaseLoader.configLoad();
        instance.saveDefaultConfig();
        CaseLoader.save();
        CaseLoader.clear();
        Bukkit.getPluginManager().disablePlugin(instance);
        Bukkit.getPluginManager().enablePlugin(instance);
    }

    @Override
    public void onDisable() {
        for(Case CASE : CaseLoader.getMapCases.values())
            if(CASE.getHologram() != null)
                CASE.getHologram().delete();
    }

    public static Main getInstance() {
        return instance;
    }
}

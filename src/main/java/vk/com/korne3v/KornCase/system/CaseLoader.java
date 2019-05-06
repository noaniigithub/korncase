package vk.com.korne3v.KornCase.system;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import vk.com.korne3v.KornCase.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaseLoader {

    public static HashMap<Block,Case> getMapCases = new HashMap<>();

    private static File file = new File(Main.getInstance().getDataFolder(),"case.yml");
    private static YamlConfiguration config;

    public CaseLoader(){
        if (!file.exists()) {
            if (Main.getInstance().getResource("case.yml") != null) {
                Main.getInstance().saveResource("case.yml", false);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    private void load(){
        for(String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            Location location = getLocation(key);

            if (location.getWorld() == null) {
                if (Main.getInstance().getConfig().getBoolean("CreateWorld"))
                    WorldCreator.name(location.getWorld().getName()).createWorld();
            }

            List<ItemCase> ic = new ArrayList<>();
            for (String itemkey : section.getConfigurationSection("ITEMS").getKeys(false)) {
                ic.add(new ItemCase(section.getItemStack("ITEMS." + itemkey + ".ITEM"), section.getString("ITEMS." + itemkey + ".NAME"), section.getString("ITEMS." + itemkey + ".REWARD"), section.getDouble("ITEMS." + itemkey + ".CHANCE")));
            }
            if (location.getWorld() != null && location.getBlock().getType() == Material.ENDER_CHEST) {
                Case CASE = new Case(location, ic);
                CASE.createHologram();
                getMapCases.put(location.getBlock(), CASE);
            }
        }
    }

    public static void removeItem(ItemCase itemCase){
        for(Case CASE : getMapCases.values()){
            if(CASE.getItemCases().contains(itemCase)) {
                CASE.removeItemCases(itemCase);
                return;
            }
        }
        save();
    }

    public static void create(String key){
        ConfigurationSection section = config.createSection(key);
        section.set("LOCATION.WORLD",Bukkit.getWorlds().get(0));
        section.set("LOCATION.X",0);
        section.set("LOCATION.Y",0);
        section.set("LOCATION.Z",0);
        save();
        addItem(key,new ItemStack(Material.STONE),95);
        addItem(key,new ItemStack(Material.DIAMOND),5);
    }

    public static void addItem(String key,ItemStack itemStack,double chance) {
        ConfigurationSection section = config.getConfigurationSection(key);
        String itemkey = itemStack.getType().toString();
        section.set("ITEMS." + itemkey + ".NAME", itemStack.getType().name());
        section.set("ITEMS." + itemkey + ".REWARD", "give %player% " + itemStack.getType().name() + " " + itemStack.getAmount() + " " + itemStack.getData());
        section.set("ITEMS." + itemkey + ".ITEM", itemStack);
        section.set("ITEMS." + itemkey + ".CHANCE", chance);
        save();
    }

    public static void setNameItem(String key,String itemkey,String name) {
        ConfigurationSection section = config.getConfigurationSection(key);
        section.set("ITEMS." + itemkey + ".NAME", name);
        save();
    }

    public static void setRewardItem(String key,String itemkey,String reward) {
        ConfigurationSection section = config.getConfigurationSection(key);
        section.set("ITEMS." + itemkey + ".REWARD", reward);
        save();
    }

    public static void setChanceItem(String key,String itemkey,double chance) {
        ConfigurationSection section = config.getConfigurationSection(key);
        section.set("ITEMS." + itemkey + ".CHANCE", chance);
        save();
    }

    public static void setItemStackInItemCase(String key,String itemkey,ItemStack itemStack) {
        ConfigurationSection section = config.getConfigurationSection(key);
        section.set("ITEMS." + itemkey + ".ITEM", itemStack);
        save();
    }

    public static Location getLocation(String key){
        ConfigurationSection section = config.getConfigurationSection(key);
        String w = section.getString("LOCATION.WORLD");
        int x = section.getInt("LOCATION.X");
        int y = section.getInt("LOCATION.Y");
        int z = section.getInt("LOCATION.Z");
        return new Location(Bukkit.getWorld(w),x,y,z);
    }

    public static void setLocation(String key,Location location){
        ConfigurationSection section = config.getConfigurationSection(key);
        section.set("LOCATION.WORLD",location.getWorld().getName());
        section.set("LOCATION.X",location.getX());
        section.set("LOCATION.Y",location.getY());
        section.set("LOCATION.Z",location.getZ());
        save();
    }

    public static void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package vk.com.korne3v.KornCase.system;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import vk.com.korne3v.KornCase.Main;
import vk.com.korne3v.KornCase.Utils;

import java.util.List;

public class Case {

    private Main main;
    private Location location;
    private List<ItemCase> itemCases;
    private Hologram hologram;
    private boolean work;

    public Case(Location location,List<ItemCase> itemCases) {
        this.main = Main.getInstance();
        this.work = false;
        this.location = location;
        this.itemCases = itemCases;
    }

    public void createHologram(){
        this.hologram = HologramsAPI.createHologram(main, location.clone().add(0.5, 2.0, 0.5));
        for(String str : main.getConfig().getStringList("language."+ Utils.getLang()+".case_hologram")) {
            if(str.startsWith("item:")) {
                this.hologram.appendItemLine(new ItemStack(Material.valueOf(str.replace("item:","").replace(" ","").toUpperCase())));
            }else{
                this.hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',str));
            }
        }
    }

    public List<ItemCase> getItemCases() {
        return itemCases;
    }

    public void removeItemCases(ItemCase itemCase){
        this.itemCases.remove(itemCase);
    }

    public void setItemCases(List<ItemCase> itemCases) {
        this.itemCases = itemCases;
    }

    public void addItemCases(ItemCase itemCase){
        this.itemCases.add(itemCase);
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public Location getLocation() {
        return location;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }
}

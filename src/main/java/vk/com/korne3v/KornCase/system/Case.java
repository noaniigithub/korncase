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
    private int face;

    public Case(int face,Location location,List<ItemCase> itemCases) {
        this.main = Main.getInstance();
        this.work = false;
        this.location = location;
        this.itemCases = itemCases;
        this.face = face;
        createHologram();
    }

    private void createHologram(){
        this.hologram = HologramsAPI.createHologram(main, location.clone().add(0.5, 2.0, 0.5));
        for(String str : main.getConfig().getStringList("language."+ Utils.getLang()+".case_hologram")) {
            if(str.startsWith("item:")) {
                this.hologram.appendItemLine(new ItemStack(Material.valueOf(str.replace("item:","").replace(" ","").toUpperCase())));
            }else{
                this.hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',str));
            }
        }
    }

    public void deleteHologram(){
        this.hologram.delete();
    }

    public void holo_hide(){
        deleteHologram();
    }

    public void holo_show(){
        createHologram();
    }



    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
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

package vk.com.korne3v.KornCase.system;

import org.bukkit.inventory.ItemStack;

public class ItemCase {

    private ItemStack itemStack;
    private String name;
    private String reward;
    private double chance;

    public ItemCase(ItemStack itemStack,String name,String reward,double chance){
        this.itemStack = itemStack;
        this.name = name;
        this.reward = reward;
        this.chance = chance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public String getReward() {
        return reward;
    }

    public double getChance() {
        return chance;
    }
}

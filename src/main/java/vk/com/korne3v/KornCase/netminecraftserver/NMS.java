package vk.com.korne3v.KornCase.netminecraftserver;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import vk.com.korne3v.KornCase.Main;

public class NMS {

    public static void configSoundAndEffectsFix(){
        String[] version = Bukkit.getBukkitVersion().split("-");
        if(version[0].equalsIgnoreCase("1.8")){
            ConfigurationSection effects = Main.getInstance().getConfig().getConfigurationSection("Settings.Effects");
            ConfigurationSection sounds = Main.getInstance().getConfig().getConfigurationSection("Settings.Sounds");

            //Effects:
            effects.set("playerParticle","SMOKE");

            //Sounds
            sounds.set("circledSound","CLICK");
            sounds.set("finalSound","WITHER_DEATH");
            sounds.set("preCloseSound","SUCCESSFUL_HIT");
            sounds.set("noFoundCase","VILLAGER_NO");
            sounds.set("caseOpen","LEVEL_UP");
            sounds.set("guiClickUP","ITEM_BREAK");
            sounds.set("guiClickDown","SUCCESSFUL_HIT");
        }
    }

    public static void playChestAnimation(Block block,int state){
        String[] version = Bukkit.getBukkitVersion().split("-");
        if(version[0].contains("1.14"))
            v1_14.playChestAnimation(block, state);
        if(version[0].contains("1.13"))
            v1_13.playChestAnimation(block, state);
        if(version[0].contains("1.12"))
            v1_12.playChestAnimation(block, state);
        if(version[0].contains("1.11"))
            v1_11.playChestAnimation(block, state);
        if(version[0].contains("1.10"))
            v1_10.playChestAnimation(block, state);
        if(version[0].contains("1.9"))
            v1_9.playChestAnimation(block, state);
        if(version[0].contains("1.8"))
            v1_8.playChestAnimation(block, state);
    }

}

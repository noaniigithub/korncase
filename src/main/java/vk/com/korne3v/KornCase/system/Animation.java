package vk.com.korne3v.KornCase.system;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import vk.com.korne3v.KornCase.Main;
import vk.com.korne3v.KornCase.Utils;
import vk.com.korne3v.KornCase.netminecraftserver.NMS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Animation {

    public static void play(Player player, Case CASE,List<ItemCase> items) {
        Block block = CASE.getLocation().getBlock();
        CASE.setWork(true);
        items.addAll(CASE.getItemCases());
        CASE.holo_hide();
        NMS.playChestAnimation(block,1);
        List<Hologram> holoiterator = new ArrayList<>();
        player.closeInventory();
        player.getOpenInventory().close();

        Utils.checkNearbyPlayers(player,CASE);

        final ItemCase[] last = new ItemCase[1];

        final int[] count = {0};

        if(Utils.getSettings("effect_speed"))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1,1),true);

        float exp = player.getExp();
        int level = player.getLevel();

        if(Utils.getSettings("level_change")) {
            player.setLevel(0);
            player.setExp(0);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if(Utils.getSettings("level_change")) {
                    player.setLevel(count[0]);
                    player.giveExp(count[0]);
                }
                playEffect(player.getLocation().clone().add(0,2.2,0),Utils.getParticleEffect("playerParticle"));
                new Thread(() -> {
                    try {
                        CASE.getLocation().getWorld().playEffect(CASE.getLocation(),Utils.getParticleEffect("circledParticle"),1);
                        Thread.sleep(150);
                        ItemCase itemCase = Utils.getRandom(items);
                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Hologram holo = HologramsAPI.createHologram(Main.getInstance(), CASE.getLocation().clone().add(0.5, 2.0, 0.5));
                            holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', itemCase.getName()));
                            holo.appendItemLine(itemCase.getItemStack());
                            playSound(block, Utils.getSoundEffect("circledSound"), 20f);
                            holoiterator.add(holo);
                        });
                        Thread.sleep(750);
                        if (count[0] != 5) {
                            last[0] = itemCase;
                            holoiterator.get(count[0]).teleport(CASE.getLocation().clone().add(0.5, -0.3, 0.5));
                            holoiterator.get(count[0]).delete();
                            count[0]++;
                            if (Utils.getSettings("break_effect"))
                                playEffect(block.getLocation(), Effect.STEP_SOUND, Material.ENDER_CHEST);
                            Thread.sleep(250);
                        }else{
                            playEffect(block.getLocation(), Utils.getParticleEffect("finalParticle"));
                            Thread.sleep(500);
                            playSound(block, Utils.getSoundEffect("finalSound"),1f);
                            Thread.sleep(2000);
                            if(Utils.getSettings("break_effect"))
                                playEffect(block.getLocation(), Effect.STEP_SOUND, Material.ENDER_CHEST);
                            if(Utils.getSettings("circle_effect")) {
                                AnimaionCircle(holoiterator.get(count[0]), CASE, CASE.getFace());
                                Thread.sleep(3000);
                            }
                            holoiterator.get(count[0]).teleport(CASE.getLocation().clone().add(0.5, -0.3, 0.5));
                            holoiterator.get(count[0]).delete();
                            CASE.getLocation().getWorld().playEffect(CASE.getLocation(),Utils.getParticleEffect("preCloseParticle"),1);
                            Thread.sleep(1500);
                            NMS.playChestAnimation(block,0);
                            Thread.sleep(1000);
                            player.playSound(player.getLocation(),Utils.getSoundEffect("preCloseSound"),1f,30f);
                            Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player,ChatColor.translateAlternateColorCodes('&',Utils.getText("broadcast").replace("%prefix%",Utils.getText("prefix")).replace("%reward%",last[0].getName()))));
                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                CASE.holo_show();
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), last[0].getReward().replace("%player%", player.getName()).replaceAll("/", ""));
                            });
                            Thread.sleep(250);
                            CASE.setWork(false);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                if (count[0] == 5) {
                    if(Utils.getSettings("level_change")) {
                        player.setLevel(level);
                        player.setExp(exp);
                    }
                    Utils.spawnFireWork(CASE.getLocation().clone().add(0.5,1.65,0.5));
                    if(Utils.getSettings("effect_speed"))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,2,1),true);
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

    public static void AnimaionCircle(Hologram holo,Case CASE,int face) {
        new Thread(() -> {
            try {
                for (Iterator<Location> locationIterator = Utils.getCircleUp(face,CASE.getLocation(), 1.3, 360).iterator(); locationIterator.hasNext(); ) {
                    Location loc = locationIterator.next();
                    holo.teleport(loc.clone().add(0.5, 1.0, 0.5));
                    Thread.sleep(5L);
                }
                Thread.sleep(720L);
            } catch (InterruptedException ex) {
            }
        }).start();
    }

    public static void playSound(Block block, Sound sound, float pitch){
        block.getWorld().playSound(block.getLocation(),sound, 1f,pitch);
    }

    public static void playEffect(Location location, Effect effect){
        location.getWorld().playEffect(location,effect, 0);
    }

    public static void playEffect(Location location, Effect effect, Material data){
        location.getWorld().playEffect(location,effect,data);
    }

}

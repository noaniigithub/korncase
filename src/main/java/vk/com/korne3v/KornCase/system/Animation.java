package vk.com.korne3v.KornCase.system;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import vk.com.korne3v.KornCase.Main;
import vk.com.korne3v.KornCase.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Animation {

    public static void play(Player player, Case CASE,List<ItemCase> items) {
        Block block = CASE.getLocation().getBlock();
        CASE.setWork(true);
        items.addAll(CASE.getItemCases());
        CASE.getHologram().getVisibilityManager().setVisibleByDefault(false);
        playChestAnimation(block, 1);
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
                playEffect(player.getLocation().add(0,2.2,0),Utils.getParticleEffect("circledItemParticle"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CASE.getLocation().getWorld().playEffect(CASE.getLocation(),Utils.getParticleEffect("circledParticle"),1);
                            Thread.sleep(150);
                            //playEffect(block.getLocation().clone().add(0.5, 1.3, 0.5), Effect.SMALL_SMOKE);
                            ItemCase itemCase = Utils.getRandom(items);
                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                Hologram holo = HologramsAPI.createHologram(Main.getInstance(), CASE.getLocation().clone().add(0.5, 2.0, 0.5));
                                holo.appendTextLine(ChatColor.translateAlternateColorCodes('&',itemCase.getName()));
                                holo.appendItemLine(itemCase.getItemStack());
                                playSound(block, Utils.getSoundEffect("circledSound"), 20f);
                                holoiterator.add(holo);
                            });
                            last[0] = itemCase;
                            Thread.sleep(750);
                            if (count[0] != 5) {
                                holoiterator.get(count[0]).teleport(CASE.getLocation().clone().add(0.5, -0.3, 0.5));
                                holoiterator.get(count[0]).delete();
                                count[0]++;
                                if(Utils.getSettings("break_effect"))
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
                                    if (block.isBlockFacePowered(BlockFace.NORTH) || block.isBlockFacePowered(BlockFace.SOUTH)) {
                                        AnimaionCircle(holoiterator.get(count[0]), CASE, 0);
                                    } else {
                                        AnimaionCircle(holoiterator.get(count[0]), CASE, 1);
                                    }
                                    Thread.sleep(3000);
                                }
                                holoiterator.get(count[0]).teleport(CASE.getLocation().clone().add(0.5, -0.3, 0.5));
                                holoiterator.get(count[0]).delete();
                                CASE.getLocation().getWorld().playEffect(CASE.getLocation(),Utils.getParticleEffect("preCloseParticle"),1);
                                Thread.sleep(1500);
                                playChestAnimation(block, 0);
                                CASE.getHologram().getVisibilityManager().resetVisibilityAll();
                                CASE.getHologram().getVisibilityManager().setVisibleByDefault(true);
                                Thread.sleep(1000);
                                CASE.setWork(false);
                                player.playSound(player.getLocation(),Utils.getSoundEffect("preCloseSound"),1f,30f);
                                Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player,ChatColor.translateAlternateColorCodes('&',Utils.getText("broadcast").replace("%prefix%",Utils.getText("prefix")).replace("%reward%",last[0].getName()))));
                                executeCommand(last[0].getReward(),player);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                if (count[0] == 5) {
                    playSound(block, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, -50f);
                    if(Utils.getSettings("level_change")) {
                        player.setLevel(level);
                        player.setExp(exp);
                    }
                    if(Utils.getSettings("effect_speed"))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,2,1),true);
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

    public static void playChestAnimation(final Block chest, final int state) {
        final Location loc = chest.getLocation();
        ((CraftWorld)loc.getWorld()).getHandle().playBlockAction(new BlockPosition(chest.getX(), chest.getY(), chest.getZ()), CraftMagicNumbers.getBlock(chest), 1, state);
    }

    public static void executeCommand(String command,Player player){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%",player.getName()));
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

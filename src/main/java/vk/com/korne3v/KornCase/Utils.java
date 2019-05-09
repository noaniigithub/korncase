package vk.com.korne3v.KornCase;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import vk.com.korne3v.KornCase.system.Case;
import vk.com.korne3v.KornCase.system.ItemCase;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static ItemCase getRandom(List<ItemCase> items) {
        final Random random = new Random();
        double chance = ThreadLocalRandom.current().nextDouble(100);
        for (ItemCase key : items) {
            if (chance <= key.getChance()) {
                if(random.nextInt(2) == 0)
                    return key;
            }
        }
        return items.get(random.nextInt(items.size()));
    }

    public static boolean getSettings(String key){
        return Main.getInstance().getConfig().getBoolean("Settings."+key);
    }

    public static Effect getParticleEffect(String key){
        return Effect.valueOf(Main.getInstance().getConfig().getString("Settings.Effects."+key).toUpperCase());
    }

    public static Sound getSoundEffect(String key){
        return Sound.valueOf(Main.getInstance().getConfig().getString("Settings.Sounds."+key).toUpperCase());
    }

    public static String getPermission(String key){
        return Main.getInstance().getConfig().getString("Permissions."+key);
    }

    public static String getLang(){
        return Main.getInstance().getConfig().getString("language.selected");
    }

    public static String getText(String key){
        return ChatColor.translateAlternateColorCodes('&',Main.getInstance().getConfig().getString("language."+getLang()+"."+key));
    }

    public static void checkNearbyPlayers(Player player, Case CASE) {
        new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                if (CASE.isWork()) {
                    try {
                        Thread.sleep(5);
                        for (Iterator<Player> it = getNearbyPlayers(CASE.getLocation(), 4).iterator(); it.hasNext(); ) {
                            Player p = it.next();
                            if (player != p && p.hasPermission(getPermission("knockback_bypass"))) {
                                Vector from = new Vector(CASE.getLocation().getX(), CASE.getLocation().getY(), CASE.getLocation().getZ());
                                Vector to = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
                                Vector vector = to.subtract(from);
                                vector.setX(vector.getX() * 0.2);
                                vector.setY(0.6);
                                vector.setZ(vector.getZ() * 0.2);
                                p.setVelocity(vector);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    this.cancel();
                }

            }
        }).start();
    }

    public static Collection<Player> getNearbyPlayers(final Location location, final int radius) {
        final ArrayList<Player> pl = new ArrayList<Player>();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(location) <= radius) {
                pl.add(p);
            }
        }
        return pl;
    }

    public static List<Location> getCircleUp(int face,Location center,double radius,int amount) {
        World world = center.getWorld();
        double increment = 6.283185307179586 / amount;
        List<Location> locations = new ArrayList<>();
        if(face == 0) {
            for (int i = 0; i < amount; ++i) {
                final double angle = i * increment;
                final double x = center.getX() + radius * Math.sin(angle);
                final double y = center.getY() + radius * Math.cos(angle);
                locations.add(new Location(world, x, y, center.getZ()));
            }
        }else{
            for (int i = 0; i < amount; ++i) {
                final double angle = i * increment;
                final double y = center.getY() + radius * Math.cos(angle);
                final double z = center.getZ() + radius * Math.sin(angle);
                locations.add(new Location(world, center.getX(), y, z));
            }
        }
        return locations;
    }

    public static void spawnFireWork(Location location){
        Firework fireWork = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
        fireworkMeta.setPower(0);
        fireworkMeta.addEffect(FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.GREEN)
                .withFade(Color.PURPLE)
                .flicker(false)
                .trail(false)
                .build()
        );
        fireWork.setFireworkMeta(fireworkMeta);
    }
}

package vk.com.korne3v.KornCase.netminecraftserver;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class v1_10 {

    public static void playChestAnimation(final Block chest, final int state) {
        Location loc = chest.getLocation();
        ((org.bukkit.craftbukkit.v1_10_R1.CraftWorld)loc.getWorld()).getHandle().playBlockAction(new net.minecraft.server.v1_10_R1.BlockPosition(chest.getX(), chest.getY(), chest.getZ()), org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers.getBlock(chest), 1, state);
    }

}

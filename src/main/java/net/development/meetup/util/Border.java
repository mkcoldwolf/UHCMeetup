package net.development.meetup.util;

import net.development.meetup.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Border {

    public static void setWalls(String w, int radius, boolean force) {
        World world = Bukkit.getWorld(w);
        int posX = radius;
        int negX = 0 - radius;
        int posZ = radius;
        int negZ = 0 - radius;
        final List<Location> locations1 = new ArrayList<>();
        final List<Location> locations2 = new ArrayList<>();
        final List<Location> locations3 = new ArrayList<>();
        final List<Location> locations4 = new ArrayList<>();
        final List<Location> locations5 = new ArrayList<>();
        final List<Location> locations6 = new ArrayList<>();
        final List<Location> locations7 = new ArrayList<>();
        final List<Location> locations8 = new ArrayList<>();
        for (int t = posX; t >= 0; t--) {
            locations1.add(world.getBlockAt(t, world.getHighestBlockYAt(t, posZ), posZ).getLocation());
        }
        for (int t = negX; t <= 0; t++) {
            locations2.add(world.getBlockAt(t, world.getHighestBlockYAt(t, posZ), posZ).getLocation());
        }
        for (int t = posX; t >= 0; t--) {
            locations3.add(world.getBlockAt(t, world.getHighestBlockYAt(t, negZ), negZ).getLocation());
        }
        for (int t = negX; t <= -0; t++) {
            locations4.add(world.getBlockAt(t, world.getHighestBlockYAt(t, negZ), negZ).getLocation());
        }
        for (int t = posZ; t >= 0; t--) {
            locations5.add(world.getBlockAt(posX, world.getHighestBlockYAt(posX, t), t).getLocation());
        }
        for (int t = negZ; t <= -0; t++) {
            locations6.add(world.getBlockAt(posX, world.getHighestBlockYAt(posX, t), t).getLocation());
        }
        for (int t = posZ; t >= 0; t--) {
            locations7.add(world.getBlockAt(negX, world.getHighestBlockYAt(negX, t), t).getLocation());
        }
        for (int t = negZ; t <= -0; t++) {
            locations8.add(world.getBlockAt(negX, world.getHighestBlockYAt(negX, t), t).getLocation());
        }
        int delay = 40;
        if (force)
            delay = 1;
        new BukkitRunnable() {
            int y = 0;
            int time = 0;

            @Override
            public void run() {
                if (!locations1.isEmpty()) {
                    for (Location loc : locations1)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations2.isEmpty()) {
                    for (Location loc : locations2)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations3.isEmpty()) {
                    for (Location loc : locations3)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations4.isEmpty()) {
                    for (Location loc : locations4)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations5.isEmpty()) {
                    for (Location loc : locations5)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations6.isEmpty()) {
                    for (Location loc : locations6)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations7.isEmpty()) {
                    for (Location loc : locations7)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (!locations8.isEmpty()) {
                    for (Location loc : locations8)
                        loc.add(0, y, 0).getBlock().setType(Material.BEDROCK);
                }
                if (y == 0)
                    y++;
                if (time < 3)
                    time++;
                else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.get(), 0L, delay);

    }

}

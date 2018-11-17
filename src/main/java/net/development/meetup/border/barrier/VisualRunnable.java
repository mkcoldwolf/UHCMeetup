package net.development.meetup.border.barrier;

import net.development.meetup.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class VisualRunnable extends BukkitRunnable {

    private VisualiseHandler handler;
    private static Map<String, int[]> map = new HashMap<>();

    public VisualRunnable() {
        this.handler = VisualiseHandler.getInstance();
    }

    public static void removePlayer(Player p) {
        map.remove(p.getName());
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Location to = p.getLocation();
            int toX = to.getBlockX();
            int toY = to.getBlockY();
            int toZ = to.getBlockZ();
            if (map.containsKey(p.getName())) {
                int[] loc = map.get(p.getName());
                if (loc[0] != toX || loc[1] != toY || loc[2] != toZ) {
                    map.put(p.getName(), new int[]{toX, toY, toZ});
                    handler.handlePositionChanged(p, p.getWorld(), toX, toY, toZ);
                }
            } else {
                map.put(p.getName(), new int[]{toX, toY, toZ});
                handler.handlePositionChanged(p, p.getWorld(), toX, toY, toZ);
            }
        }
    }

    public static void init(Main plugin) {
        long delay = 2;
        new VisualRunnable().runTaskTimerAsynchronously(plugin, 20l, delay);
    }

}

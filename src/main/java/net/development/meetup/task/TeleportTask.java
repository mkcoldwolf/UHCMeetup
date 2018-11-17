package net.development.meetup.task;

import net.development.meetup.Main;
import net.development.meetup.options.generateScatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeleportTask extends BukkitRunnable {

    private List<UUID> NEED_TELEPORT = new ArrayList<>();

    public TeleportTask() {
        NEED_TELEPORT.addAll(Main.getGM().players);
        if (Main.TeamMode) generateScatter.setTeamLocation();
    }

    @Override
    public void run() {
        if (NEED_TELEPORT.isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(Main.get().getLang().translate(p, "scatterFinish"));
                for (String s : Main.get().getLang().translateArrays(p, "Rules")) {
                    p.sendMessage(s);
                }
            }
            cancel();
            return;
        }
        Player p = Bukkit.getPlayer(NEED_TELEPORT.remove(0));
        if (p == null)
            return;
        Main.getAM().sendPlayers(p);
        Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
            @Override
            public void run() {
                Main.getKM().setItem(p);
            }
        }, 5L);
    }

}

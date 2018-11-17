package net.development.meetup.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DebugTask extends BukkitRunnable {
    public DebugTask(Player p) {
        this.p = p;
    }

    int run = 0;
    Player p;

    @Override
    public void run() {
        if (run >= 10 || p == null) {
            this.cancel();
            return;
        }
        p.sendMessage(String.valueOf(run));
        run++;
    }
}

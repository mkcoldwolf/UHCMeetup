package net.development.meetup.task;

import net.development.meetup.enums.Status;
import net.development.meetup.options.checkWin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    public static int time = 0;

    @Override
    public void run() {
        if (Status.isState(Status.FINISH)) {
            cancel();
        }
        time++;
        if (time == 5) {
            checkWin.checkWins();
        }
    }


}

package mitw.meetup.task;

import mitw.meetup.UHCMeetup;
import mitw.meetup.enums.GameStatus;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    public static int time = 0;

    @Override
    public void run() {
        if (GameStatus.is(GameStatus.FINISH)) {
            cancel();
            return;
        }
        time++;
        if (time == 5) {
            UHCMeetup.getInstance().getGameManager().checkWins();
        }
    }


}

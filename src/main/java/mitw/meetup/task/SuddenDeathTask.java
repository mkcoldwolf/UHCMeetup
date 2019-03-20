package mitw.meetup.task;

import lombok.Getter;
import mitw.meetup.UHCMeetup;
import mitw.meetup.enums.GameStatus;
import mitw.meetup.util.Util;
import net.development.mitw.utils.RV;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SuddenDeathTask extends BukkitRunnable {

    @Getter
    private static boolean timerStarted = false;
    @Getter
    private static int countdown = 180;
    private boolean start = false;

    public SuddenDeathTask() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            UHCMeetup.getInstance().getLanguage().send(player, "sudden_death_countdown", RV.o("%0", Util.getTimeHora(countdown)));
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        }

        timerStarted = true;

        this.runTaskTimer(UHCMeetup.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        if (!GameStatus.is(GameStatus.PVP)) {
            cancel();
            return;
        }
        if (start) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getWorld().strikeLightning(player.getLocation());
            }
            return;
        }
        if (countdown - 1 == 0) {
            start = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                UHCMeetup.getInstance().getLanguage().send(player, "sudden_death_start");
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            }
            timerStarted = false;
        } else {
            countdown--;
            if (countdown == 120 || countdown == 60 || countdown == 30 || countdown == 10 || countdown <= 5) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UHCMeetup.getInstance().getLanguage().send(player, "sudden_death_countdown", RV.o("%0", Util.getTimeHora(countdown)));
                    player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
                }
            }
        }
    }
}

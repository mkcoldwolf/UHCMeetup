package mitw.meetup.task;

import mitw.meetup.border.BorderBuilder;
import mitw.meetup.UHCMeetup;
import mitw.meetup.util.SoundUtil;
import mitw.meetup.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PVPBorderTask extends BukkitRunnable {

    public static int sec = 0;
    public static List<Integer> list = new ArrayList<>();

    public PVPBorderTask() {
        sec = 20;
        list.add(100);
        list.add(50);
        list.add(25);
    }

    @Override
    public void run() {
        sec--;
        if (sec == 0) {
            int s = list.remove(0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "Shirnk").replace("<border>", s + ""));
            }
            UHCMeetup.getInstance().getGameManager().setBorder(s);
            new BorderBuilder(Bukkit.getWorld("world"), s, 4).start(true);
            if (list.isEmpty()) {
                cancel();
            }
            sec = 120;
        } else if (sec == 60 || sec == 30 || sec == 15 || sec == 10 || sec <= 5 && sec > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "InShirnk")
                        .replace("<border>", list.get(0) + "").replaceAll("<time>", Util.getTimeHora(sec)));
            }
            if (sec <= 5 && sec > 0) {
                SoundUtil.PlaySoundAll(Sound.NOTE_PLING);
            } else {
                SoundUtil.PlaySoundAll(Sound.NOTE_STICKS);
            }

        }

    }

}

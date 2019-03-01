package mitw.meetup.task;

import mitw.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeleportTask extends BukkitRunnable {

    private List<UUID> NEED_TELEPORT = new ArrayList<>();

    public TeleportTask() {
        NEED_TELEPORT.addAll(UHCMeetup.getInstance().getGameManager().players);
        if (UHCMeetup.TeamMode) {
            UHCMeetup.getInstance().getGameManager().setTeamLocation();
        }
    }

    @Override
    public void run() {
        if (NEED_TELEPORT.isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "scatterFinish"));
                for (String s : UHCMeetup.getInstance().getLanguage().translateArrays(p, "Rules")) {
                    p.sendMessage(s);
                }
            }
            cancel();
            return;
        }
        Player p = Bukkit.getPlayer(NEED_TELEPORT.remove(0));
        if (p == null)
            return;
        UHCMeetup.getInstance().getArenaManager().sendPlayers(p);
        Bukkit.getScheduler().runTaskLater(UHCMeetup.getInstance(), new Runnable() {
            @Override
            public void run() {
                UHCMeetup.getInstance().getKitManager().setItem(p);
            }
        }, 5L);
    }

}

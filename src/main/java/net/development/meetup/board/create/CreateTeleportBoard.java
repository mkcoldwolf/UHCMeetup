package net.development.meetup.board.create;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.task.ReleaseTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CreateTeleportBoard extends BukkitRunnable {

    @Override
    public void run() {
        if (!Status.isState(Status.TELEPORT)) {
            cancel();
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            updateScore(p);
        }
    }

    public void updateScore(Player p) {
        if (ScoreHelper.hasScore(p)) {
            ScoreHelper helper = ScoreHelper.getByPlayer(p);
            List<String> setboardList = new ArrayList<>();
            for (String s : Main.get().getLang().translateArrays(p, "TPList")) {
                setboardList.add(placeholder(p, s));
            }
            helper.setSlotsFromList(setboardList);
        } else {
            Bukkit.getScheduler().runTask(Main.get(), new Runnable() {
                @Override
                public void run() {
                    ScoreHelper helper = ScoreHelper.createScore(p);
                    helper.setTitle(Lang.Title);
                }
            });
        }
    }

    public static String placeholder(Player p, String s) {
        String withVars = s
                .replaceAll("<players>", "" + Main.getGM().players.size()).replaceAll("&", "§")
                .replaceAll("<starting>", (ReleaseTask.RELEASE_TIME + 1) + "");
        return withVars;
    }

}

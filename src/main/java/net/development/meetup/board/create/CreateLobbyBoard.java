package net.development.meetup.board.create;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.scenarios.ScenariosEnable;
import net.development.meetup.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CreateLobbyBoard extends BukkitRunnable {

    @Override
    public void run() {
        if (!Status.isState(Status.WAITING)) {
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
            for (String s : Main.get().getLang().translateArrays(p, "LobbyList")) {
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
        String withVars = s.replaceAll("<players>", "" + Main.getGM().players.size()).replaceAll("&", "§")
                .replaceAll("<starting>", "" + Tools.getStartingText(p))
                .replaceAll("<s1>", ScenariosEnable.Timebomb.size() + "/3")
                .replaceAll("<s2>", ScenariosEnable.NoClean.size() + "/3")
                .replaceAll("<s3>", ScenariosEnable.BowLess.size() + "/3")
                .replaceAll("<s4>", ScenariosEnable.RodLess.size() + "/3")
                .replaceAll("<s5>", ScenariosEnable.FireLess.size() + "/3")
                .replaceAll("<s6>", ScenariosEnable.AirDrops.size() + "/3")
                .replaceAll("<s7>", ScenariosEnable.IronKing.size() + "/3");
        ;
        return withVars;
    }
}
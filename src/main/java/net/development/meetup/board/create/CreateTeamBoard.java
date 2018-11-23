package net.development.meetup.board.create;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.player.UHCPlayer;
import net.development.meetup.task.GameTask;
import net.development.meetup.task.PVPBorderTask;
import net.development.meetup.util.Tools;

public class CreateTeamBoard extends BukkitRunnable {

	@Override
	public void run() {
		if (!Status.isState(Status.PVP)) {
			cancel();
			return;
		}
		for (final Player p : Bukkit.getOnlinePlayers()) {
			updateScore(p);
		}
	}

	public void updateScore(final Player p) {
		if (ScoreHelper.hasScore(p)) {
			final ScoreHelper helper = ScoreHelper.getByPlayer(p);
			final List<String> setboardList = new ArrayList<>();
			for (final String s : Main.get().getLang().translateArrays(p, "TeamList")) {
				if (s.equals("<noclean>")) {
					if (Main.getGM().getData.get(p.getUniqueId()).isNoClean()) {
						setboardList.add(placeholder(p, Main.get().getLang().translate(p, "noClean")));
						continue;
					} else {
						continue;
					}
				}
				setboardList.add(placeholder(p, s));
			}
			helper.setSlotsFromList(setboardList);
		} else {
			Bukkit.getScheduler().runTask(Main.get(), () -> {
				final ScoreHelper helper = ScoreHelper.createScore(p);
				helper.setTitle(Lang.Title);
			});
		}
	}

	public static String placeholder(final Player p, String s) {
		final UHCPlayer up = Main.getGM().getData.get(p.getUniqueId());
		String untiShrink = "&7|&c " + Tools.UntiShrinkTime(PVPBorderTask.sec);
		if (PVPBorderTask.list.isEmpty()) {
			untiShrink = "";
		}
		s = s.replaceAll("<players>", "" + Main.getGM().players.size()).replaceAll("&", "§")
				.replaceAll("<border>", "" + Main.getGM().getBorder() + "").replaceAll("<format>", untiShrink)
				.replaceAll("<time>", Tools.getTimeHora(GameTask.time)).replaceAll("<kills>", up.getKills() + "")
				.replaceAll("<teamKills>", up.isInTeam() ? up.getTeam().kills + "" : up.getKills() + "")
				.replaceAll("<max>", Main.getGM().max + "")
				.replaceAll("<cleanTime>", new DecimalFormat("0.0").format(up.getNocleanTimer()) + "s");
		return s;
	}

}

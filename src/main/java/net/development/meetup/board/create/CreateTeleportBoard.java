package net.development.meetup.board.create;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.task.ReleaseTask;

public class CreateTeleportBoard extends BukkitRunnable {

	@Override
	public void run() {
		if (!Status.isState(Status.TELEPORT)) {
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
			for (final String s : Main.get().getLang().translateArrays(p, "TPList")) {
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

	public static String placeholder(final Player p, final String s) {
		final String withVars = s
				.replaceAll("<players>", "" + Main.getGM().players.size()).replaceAll("&", "§")
				.replaceAll("<starting>", (ReleaseTask.RELEASE_TIME + 1) + "s");
		return withVars;
	}

}

package net.development.meetup.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.task.LobbyTask;

public class Tools {

	public static int getCenter(final Player player) {
		final Location location = new Location(Bukkit.getWorld("world"), 0.0, player.getLocation().getY(), 0.0);
		final int n = (int) Math.floor(player.getLocation().distance(location));
		return n;
	}

	public static String getTime(int o) {
		final int i = ++o / 60;
		final int j = o - i * 60;
		String str = null;
		str = i <= 0 ? (j < 10 ? "" + j : "" + j)
				: (i < 10 && i > 0 ? (j < 10 ? String.valueOf(i) + ":0" + j : String.valueOf(i) + ":" + j)
						: (j < 10 ? String.valueOf(i) + ":0" + j : String.valueOf(i) + ":" + j));
		return str;
	}

	public static String getTimeHora(final int o) {
		String timer;
		final int totalSecs = o;
		final int hours = totalSecs / 3600;
		final int minutes = totalSecs % 3600 / 60;
		final int seconds = totalSecs % 60;
		if (totalSecs >= 3600) {
			timer = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			timer = String.format("%02d:%02d", minutes, seconds);
		}
		return timer;
	}

	public static String UntiShrinkTime(final int o) {
		final int totalSecs = o;
		final int hours = totalSecs / 3600;
		int minutes = totalSecs / 60;
		final int seconds = totalSecs;
		String str = null;

		if (o >= 3600) {
			str = "" + hours + "h";
		}
		if (o < 3600 && o >= 60) {
			minutes = minutes + 1;
			str = "" + (minutes) + "m";
		}
		if (o < 60) {
			str = "" + seconds + "s";
		}
		return str;
	}

	public static String getShrinkTime(final Player p, int o) {
		final int i = ++o / 60;
		final int j = o - i * 60;
		final String seg = Main.get().getLang().translate(p, "s1");
		final String segs = Main.get().getLang().translate(p, "s2");
		final String min = Main.get().getLang().translate(p, "m1");
		final String mins = Main.get().getLang().translate(p, "m2");
		String str = null;
		str = i <= 0 ? (j < 2 ? seg : segs) : (i < 2 && i > 0 ? (j < 10 ? min : min) : (j < 10 ? mins : mins));
		return str;
	}

	public static String colored(final String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static String getStartingText(final Player p) {
		if (LobbyTask.start)
			return LobbyTask.i + Main.get().getLang().translate(p, "s2");
		return "15" + Main.get().getLang().translate(p, "s2");
	}

}

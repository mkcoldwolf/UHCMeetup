package net.development.meetup.task;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Main;
import net.development.meetup.util.Border;
import net.development.meetup.util.PlaySound;
import net.development.meetup.util.Tools;

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
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(Main.get().getLang().translate(p, "Shirnk").replace("<border>", s + ""));
			}
			Main.getGM().setBorder(s);
			if (list.isEmpty()) {
				cancel();
			}
			sec = 120;
		} else if (sec == 60 || sec == 30 || sec == 15 || sec == 10 || sec <= 5 && sec > 0) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(Main.get().getLang().translate(p, "InShirnk")
						.replace("<border>", list.get(0) + "").replaceAll("<time>", Tools.getTimeHora(sec)));
			}
			if (sec <= 5 && sec > 0) {
				PlaySound.PlaySoundAll(Sound.NOTE_PLING);
			} else {
				PlaySound.PlaySoundAll(Sound.NOTE_STICKS);
			}

		}if(sec == 6)Border.setWalls("world", list.get(0), false);

	}

}

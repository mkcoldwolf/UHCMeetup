package net.development.meetup.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DebugTask extends BukkitRunnable {

	public DebugTask(final Player p) {
		this.p = p;
	}

	private int run = 0;
	private final Player p;

	@Override
	public void run() {
		if (run >= 10 || p == null) {
			this.cancel();
			return;
		}
		p.sendMessage(String.valueOf(run));
		run++;
	}
}

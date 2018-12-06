package net.development.meetup.task;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Main;
import net.development.meetup.board.BoardSetup;
import net.development.meetup.enums.Status;
import net.development.meetup.manager.GameManager;
import net.development.meetup.player.UHCTeam;
import net.development.meetup.util.PlaySound;

public class LobbyTask extends BukkitRunnable {

	public static int i = 0;
	public static Boolean start = false;

	public LobbyTask() {
		i = 30;
		start = true;
	}

	@Override
	public void run() {
		i--;
		if (Main.getGM().players.size() < Main.get().getConfig().getInt("Min-Player")) {
			Bukkit.broadcastMessage("§c人數不足導致遊戲無法無法開始");
			PlaySound.PlaySoundAll(Sound.NOTE_STICKS);
			start = false;
			GameManager.isInDown = false;
			this.cancel();
		}
		if (i == 0) {
			start = false;
			PlaySound.PlaySoundAll(Sound.NOTE_PLING);
			Main.getGM().max = Main.getGM().players.size();
			Status.setState(Status.TELEPORT);
			BoardSetup.setup();
			UHCTeam.fillTeams();
			new TeleportTask().runTaskTimer(Main.get(), 20L, 5L);
			new ReleaseTask().runTaskTimer(Main.get(), 20L, 20L);
			cancel();
		} else {
			PlaySound.PlaySoundAll(Sound.NOTE_STICKS);
			if (i > 5 && i % 5 == 0) {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(Main.get().getLang().translate(p, "SCATTER_STARTING").replace("<time>", i + ""));
				}
			}
			if (i <= 5) {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(Main.get().getLang().translate(p, "SCATTER_STARTING").replace("<time>", i + ""));
				}
			}
		}
	}

}

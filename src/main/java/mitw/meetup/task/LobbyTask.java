package mitw.meetup.task;

import mitw.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import mitw.meetup.enums.GameStatus;
import mitw.meetup.manager.GameManager;
import mitw.meetup.player.UHCTeam;
import mitw.meetup.util.SoundUtil;

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
		if (UHCMeetup.getInstance().getGameManager().players.size() < UHCMeetup.getInstance().getConfig().getInt("Min-Player")) {
			Bukkit.broadcastMessage("§c人數不足導致遊戲無法無法開始");
			SoundUtil.PlaySoundAll(Sound.NOTE_STICKS);
			start = false;
			GameManager.isInDown = false;
			this.cancel();
		}
		if (i == 0) {
			start = false;
			SoundUtil.PlaySoundAll(Sound.NOTE_PLING);
			UHCMeetup.getInstance().getGameManager().max = UHCMeetup.getInstance().getGameManager().players.size();
			GameStatus.set(GameStatus.TELEPORT);
			UHCTeam.fillTeams();
			new TeleportTask().runTaskTimer(UHCMeetup.getInstance(), 20L, 5L);
			new ReleaseTask().runTaskTimer(UHCMeetup.getInstance(), 20L, 20L);
			cancel();
		} else {
			SoundUtil.PlaySoundAll(Sound.NOTE_STICKS);
			if (i > 5 && i % 5 == 0) {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "SCATTER_STARTING").replace("<time>", i + ""));
				}
			}
			if (i <= 5) {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "SCATTER_STARTING").replace("<time>", i + ""));
				}
			}
		}
	}

}

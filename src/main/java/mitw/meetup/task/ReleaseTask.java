package mitw.meetup.task;

import java.util.UUID;

import mitw.meetup.UHCMeetup;
import mitw.meetup.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import mitw.meetup.enums.GameStatus;
import mitw.meetup.util.SoundUtil;
import mitw.meetup.util.SitUtil;

public class ReleaseTask extends BukkitRunnable {

	public static int RELEASE_TIME = 0;

	public ReleaseTask() {
		for (Scenario scenario : Scenario.values()) {
			if (scenario.getVotes() > 2) {
				scenario.setActive(true);
			}
		}
		RELEASE_TIME = 15;
		if (!UHCMeetup.TeamMode)
			return;
	}

	@Override
	public void run() {
		if (RELEASE_TIME <= 0) {
			for (final UUID u : UHCMeetup.getInstance().getGameManager().players) {
				final Player p = Bukkit.getPlayer(u);
				if (p == null) {
					continue;
				}
				for (final PotionEffect pot : p.getActivePotionEffects()) {
					p.removePotionEffect(pot.getType());
				}
				SitUtil.removeHorses(p);
			}
			Bukkit.getWorld("world").setPVP(true);
			GameStatus.set(GameStatus.PVP);

			Bukkit.getOnlinePlayers().forEach(player -> UHCMeetup.getInstance().getPlayerManager().setColoredTag(player));

			new GameTask().runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
			new PVPBorderTask().runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
			SoundUtil.PlaySoundAll(Sound.ENDERDRAGON_GROWL);
			cancel();
		} else {
			if (RELEASE_TIME == 15 || RELEASE_TIME == 10 || RELEASE_TIME <= 5) {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "STARTING").replace("<time>", RELEASE_TIME + ""));
				}
			}
			RELEASE_TIME--;
		}
	}

}

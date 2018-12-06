package net.development.meetup.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Main;
import net.development.meetup.board.BoardSetup;
import net.development.meetup.enums.Status;
import net.development.meetup.scenarios.ScenariosEnable;
import net.development.meetup.util.PlaySound;
import net.development.meetup.util.Sit;

public class ReleaseTask extends BukkitRunnable {

	public static int RELEASE_TIME = 0;

	public ReleaseTask() {
		if (ScenariosEnable.IronKing.size() >= 3) {
			ScenariosEnable.IronKingE = true;
		}
		RELEASE_TIME = 15;
		if (!Main.TeamMode)
			return;
	}

	@Override
	public void run() {
		if (RELEASE_TIME <= 0) {
			if (ScenariosEnable.Timebomb.size() >= 3) {
				ScenariosEnable.TimebombE = true;
			}
			if (ScenariosEnable.NoClean.size() >= 3) {
				ScenariosEnable.NoCleanE = true;
			}
			if (ScenariosEnable.BowLess.size() >= 3) {
				ScenariosEnable.BowLessE = true;
			}
			if (ScenariosEnable.RodLess.size() >= 3) {
				ScenariosEnable.RodLessE = true;
			}
			if (ScenariosEnable.FireLess.size() >= 3) {
				ScenariosEnable.FireLessE = true;
			}
			if (ScenariosEnable.AirDrops.size() >= 3) {
				ScenariosEnable.AirDropsE = true;
				AirDropsTask.init();
			}
			for (final UUID u : Main.getGM().players) {
				final Player p = Bukkit.getPlayer(u);
				if (p == null) {
					continue;
				}
				for (final PotionEffect pot : p.getActivePotionEffects()) {
					p.removePotionEffect(pot.getType());
				}
				Sit.removeHorses(p);
			}
			Bukkit.getWorld("world").setPVP(true);
			Status.setState(Status.PVP);
			BoardSetup.setup();
			new GameTask().runTaskTimer(Main.get(), 0L, 20L);
			new PVPBorderTask().runTaskTimer(Main.get(), 0L, 20L);
			PlaySound.PlaySoundAll(Sound.ENDERDRAGON_GROWL);
			cancel();
		} else {
			if (RELEASE_TIME == 15 || RELEASE_TIME == 10 || RELEASE_TIME <= 5) {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(Main.get().getLang().translate(p, "STARTING").replace("<time>", RELEASE_TIME + ""));
				}
			}
			RELEASE_TIME--;
		}
	}

}

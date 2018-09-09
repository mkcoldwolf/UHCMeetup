package net.development.meetup.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Main;
import net.development.meetup.UHCPlayer;
import net.development.meetup.UHCTeam;
import net.development.meetup.board.BoardSetup;
import net.development.meetup.enums.Status;
import net.development.meetup.options.TeamGUI;
import net.development.meetup.scenarios.ScenariosEnable;
import net.development.meetup.util.PlaySound;
import net.development.meetup.util.Sit;

public class ReleaseTask extends BukkitRunnable {

	public static int RELEASE_TIME = 0;

	public ReleaseTask() {
		if (ScenariosEnable.IronKing.size() >= 3)
			ScenariosEnable.IronKingE = true;
		RELEASE_TIME = 15;
		if(!Main.TeamMode)return;
		loop: for(Player p : Bukkit.getOnlinePlayers()) {
			UHCPlayer up = Main.getGM().getData.get(p.getUniqueId());
			if(!up.isInTeam()) {
				for(int i = 0;i <= 27; i++) {
					UHCTeam team = TeamGUI.getInstance().teams.get(i);
					if(team.p1 == null) {
						team.p1 = p;
						up.setTeam(team, i);
						p.sendMessage("§a你加入了隊伍 "+(i+1)+" !");
						continue loop;
					}else if(team.p2 == null) {
						team.p2 = p;
						up.setTeam(team, i);
						p.sendMessage("§a你加入了隊伍 "+(i+1)+" !");
						continue loop;
					}
				}
			}
		}
	}

	@Override
	public void run() {
		if (RELEASE_TIME <= 0) {
			if (ScenariosEnable.Timebomb.size() >= 3) {
				ScenariosEnable.TimebombE = true;
			}
			if (ScenariosEnable.NoClean.size() >= 3)
				ScenariosEnable.NoCleanE = true;
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
			for (UUID u : Main.getGM().players) {
				Player p = Bukkit.getPlayer(u);
				if (p == null)
					continue;
				for (PotionEffect pot : p.getActivePotionEffects())
					p.removePotionEffect(pot.getType());
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
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(Main.get().getLang().translate(p, "STARTING").replace("<time>", RELEASE_TIME + ""));
				}
			}
			RELEASE_TIME--;
		}
	}

}

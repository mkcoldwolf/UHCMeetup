package net.development.meetup.options;

import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.player.UHCTeam;
import net.development.meetup.task.FireworkTask;
import net.development.meetup.util.PlaySound;
import net.development.mitw.utils.RV;
import net.development.mitw.uuid.UUIDCache;

public class checkWin {

	public static void checkWins() {
		if (Main.TeamMode) {
			if (TeamGUI.getInstance().getLastTeam() != null) {
				final UHCTeam win = TeamGUI.getInstance().getLastTeam();

				for (final UUID uuid : win.members) {
					final Player player = Bukkit.getPlayer(uuid);
					new FireworkTask(player).runTaskTimer(Main.get(), 10L, 20L);
				}
				PlaySound.PlaySoundAll(Sound.WITHER_DEATH);

				Main.get().getLang().send("win_team", RV.o("0", (win.id + 1) + ""));
				Main.get().getLang().send("team_members", RV.o("0", win.members.stream().map(UUIDCache::getName).collect(Collectors.joining(", "))));

				Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						Main.getGM().sendToServer(p);
					}
					Bukkit.getScheduler().runTaskLater(Main.get(), () -> Bukkit.shutdown(), 20);
				}, 300L);
			}
			return;
		}
		if (!Main.getGM().players.isEmpty() && Main.getGM().players.size() == 1) {
			final Player winner = Bukkit.getPlayer(Main.getGM().players.get(0));
			new FireworkTask(winner).runTaskTimer(Main.get(), 10L, 20L);
			PlaySound.PlaySoundAll(Sound.WITHER_DEATH);

			Main.get().getLang().send("win", RV.o("0", winner.getName()));

			Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					Main.getGM().sendToServer(p);
				}
				Bukkit.getScheduler().runTaskLater(Main.get(), () -> Bukkit.shutdown(), 20L);
			}, 300L);
		} else if (Main.getGM().players.isEmpty()) {
			Bukkit.shutdown();
		}
	}

}

package net.development.meetup.options;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.player.UHCTeam;
import net.development.meetup.task.FireworkTask;
import net.development.meetup.util.PlaySound;
import net.development.mitw.uuid.UUIDCache;
import net.md_5.bungee.api.ChatColor;

public class checkWin {

	public static void checkWins() {
		if (Main.TeamMode) {
			if (TeamGUI.getInstance().getLastTeam() != null) {
				final UHCTeam win = TeamGUI.getInstance().getLastTeam();

				final Player player1 = Bukkit.getPlayer(win.p1);
				final Player player2 = Bukkit.getPlayer(win.p2);

				if (player1 != null) {
					new FireworkTask(player1).runTaskTimer(Main.get(), 10L, 20L);
				}
				if (player2 != null) {
					new FireworkTask(player2).runTaskTimer(Main.get(), 10L, 20L);
				}
				PlaySound.PlaySoundAll(Sound.WITHER_DEATH);
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&l隊伍" + (win.id + 1) + " &b贏了這場遊戲!!"));
				Bukkit.broadcastMessage("§f隊伍成員: §6" + (win.p1 != null ? UUIDCache.getName(win.p1) : "") + (win.p2 != null ? win.p1 != null ? ", " + UUIDCache.getName(win.p2) : UUIDCache.getName(win.p2) : ""));
				Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						Main.getGM().sendToServer(p);
					}
					Bukkit.getScheduler().runTaskLater(Main.get(), () -> Bukkit.shutdown(), 20);
				}, 300);
			}
			return;
		}
		if (!Main.getGM().players.isEmpty() && Main.getGM().players.size() == 1) {
			final Player winner = Bukkit.getPlayer(Main.getGM().players.get(0));
			new FireworkTask(winner).runTaskTimer(Main.get(), 10L, 20L);
			PlaySound.PlaySoundAll(Sound.WITHER_DEATH);
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&l" + winner.getName() + " &b贏了這場遊戲!!"));
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

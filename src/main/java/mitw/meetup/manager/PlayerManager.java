package mitw.meetup.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import mitw.meetup.Lang;
import mitw.meetup.UHCMeetup;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.player.UHCTeam;
import net.development.mitw.uuid.UUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import mitw.meetup.util.Util;
import net.development.mitw.utils.ItemBuilder;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@RequiredArgsConstructor
public class PlayerManager {

	private final UHCMeetup plugin;
	
	public void setIngame(final Player p) {
		plugin.getGameManager().players.add(p.getUniqueId());
	}

	public void setPlayerSpec(final Player p, final boolean lateHide) {

		plugin.getGameManager().spectators.add(p.getUniqueId());

		if (lateHide) {
			Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () -> {
				if (!p.isOnline()) {
					return;
				}
				for (final Player p1 : Bukkit.getOnlinePlayers()) {
					if (p1 != null && p1 != p) {
						p1.hidePlayer(p);
						if (plugin.getGameManager().spectators.contains(p1.getUniqueId())) {
							p.hidePlayer(p1);
						}
					}
				}

			}, 20L);
		} else {
			for (final Player p1 : Bukkit.getOnlinePlayers()) {
				if (p1 != null && p1 != p) {
					p1.hidePlayer(p);
					if (plugin.getGameManager().spectators.contains(p1.getUniqueId())) {
						p.hidePlayer(p1);
					}
				}
			}
		}

		p.setGameMode(GameMode.CREATIVE);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!p.isOnline()) {
					return;
				}
				p.getInventory().clear();
				p.getInventory().setItem(0, new ItemBuilder(Material.SLIME_BALL).name(plugin.getInstance().getLanguage().translate(p, "spec1")).build());
				p.getInventory().setItem(8, new ItemBuilder(Material.BED).name(plugin.getInstance().getLanguage().translate(p, "spec2")).build());
				p.getInventory().setItem(7, new ItemBuilder(Material.DIAMOND_SWORD).name(plugin.getInstance().getLanguage().translate(p, "reTurnToPractice")).build());
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));
				p.updateInventory();
				setColoredTag(p);
			}
		}.runTaskLater(plugin.getInstance(), 5L);
	}

	public void setPlayerDBMode(final Player p) {
		final UUID u = p.getUniqueId();
		PlayerProfile playerProfile = plugin.getGameManager().getProfile(u);
		if (playerProfile.isDebug()) {
			playerProfile.setDebug(false);
			p.sendMessage(Util.colored(Lang.PREFIX + plugin.getInstance().getLanguage().translate(p, "cleandb_off")));
			p.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).durability(8).name(plugin.getInstance().getLanguage().translate(p, "dbmodeoff")).build());
		} else {
			playerProfile.setDebug(true);
			p.sendMessage(Util.colored(Lang.PREFIX + plugin.getInstance().getLanguage().translate(p, "cleandb_on")));
			p.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).durability(10).name(plugin.getInstance().getLanguage().translate(p, "dbmodeon")).build());
		}
	}

	public void setSpectateTag(Player player) {

		Scoreboard scoreboard = player.getScoreboard();
		if (UHCMeetup.getInstance().getGameManager().spectators.contains(player.getUniqueId())) {
			Team spectateTeam = scoreboard.getTeam("9spectate");
			if (spectateTeam == null) {
				spectateTeam = scoreboard.registerNewTeam("9spectate");
			}
			spectateTeam.setPrefix("§8");
			spectateTeam.addEntry(player.getName());
		}
	}

	public void setColoredTag(Player player) {

		Scoreboard scoreboard = player.getScoreboard();
		boolean spectating = false;

		if (UHCMeetup.getInstance().getGameManager().spectators.contains(player.getUniqueId())) {
			Team spectateTeam = scoreboard.getTeam("9spectate");
			if (spectateTeam == null) {
				spectateTeam = scoreboard.registerNewTeam("9spectate");
			}
			spectateTeam.setPrefix("§8");
			spectateTeam.addEntry(player.getName());
			spectating = true;
		}

		if (UHCMeetup.TeamMode) {
			final UHCTeam uTeam = UHCMeetup.getInstance().getGameManager().getProfile(player.getUniqueId()).getTeam();

			final Map<Integer, Team> teams = new HashMap<>();

			for (final UHCTeam team : TeamManager.getInstance().teams) {
				Team t;
				if (!teams.containsKey(team.id)) {
					final String teamName = uTeam != null ? team.id == uTeam.id ? "1team" + team.id : "team" + team.id : "team" + team.id;
					t = scoreboard.getTeam(teamName);
					if (t == null) {
						t = scoreboard.registerNewTeam(teamName);
					}
					t.setPrefix((uTeam != null ? (uTeam.id == team.id ? "§a" : "§c") : "§c") + "[" + (team.id + 1) + "]");
					teams.put(team.id, t);
				} else {
					t = teams.get(team.id);
				}
				for (final UUID uuid : team.members) {

					if (!UHCMeetup.getInstance().getGameManager().getProfile(uuid).isAlive()) {
						continue;
					}

					t.addEntry(UUIDCache.getName(uuid));
				}
			}
		} else {

			if (!spectating) {

				Team team1 = scoreboard.getTeam("1team");
				if (team1 == null) {
					team1 = scoreboard.registerNewTeam("1team");
				}
				team1.setPrefix(ChatColor.GREEN.toString());
				team1.addEntry(player.getName());

			}

			Team team2 = scoreboard.getTeam("2team");
			if (team2 == null) {
				team2 = scoreboard.registerNewTeam("2team");
			}
			team2.setPrefix(ChatColor.RED.toString());

			for (final Player playerA : Bukkit.getOnlinePlayers()) {

				if (playerA != player) {

					team2.addEntry(playerA.getName());

				}

			}

		}


	}

}

package mitw.meetup.manager;

import java.util.*;
import java.util.stream.Collectors;

import mitw.meetup.UHCMeetup;
import mitw.meetup.enums.GameStatus;
import mitw.meetup.player.UHCTeam;
import mitw.meetup.task.FireworkTask;
import mitw.meetup.task.LobbyTask;
import mitw.meetup.util.SoundUtil;
import net.development.mitw.utils.RV;
import net.development.mitw.uuid.UUIDCache;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import mitw.meetup.border.worldborder.Config;
import mitw.meetup.player.PlayerProfile;
import net.development.mitw.utils.ItemBuilder;

public class GameManager {

	public List<UUID> players = new ArrayList<>();
	public List<UUID> spectators = new ArrayList<>();
	public Map<UUID, Boolean> debugModePlayers = new HashMap<>();
	public int max;
	private static int border;
	public static Boolean isInDown = false;
	public Map<UUID, PlayerProfile> profiles = new HashMap<>();
	public Location spawn;
	public boolean broadcasted;

	public int getBorder() {
		return border;
	}

	public int setBorder(final int i) {
		border = i;
		Config.setBorder("world", i, 0, 0);
		return border;
	}

	public boolean isBroadcasted() {
		return broadcasted;
	}

	public void openBroadcast() {
		broadcasted = true;
	}

	public void setCleanPlayerLobby(final Player p, final GameMode gameMode) {
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.setExp(0.0f);
		p.setTotalExperience(0);
		p.setLevel(0);
		p.setFireTicks(0);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setGameMode(gameMode);
		for (final PotionEffect potionEffect : p.getActivePotionEffects()) {
			p.removePotionEffect(potionEffect.getType());
		}
	}

	public void sendToLobby(final Player p) {
		p.teleport(spawn);
		new BukkitRunnable() {
			@Override
			public void run() {
				p.getInventory().setItem(0, new ItemBuilder(Material.PAINTING).name(UHCMeetup.getInstance().getLanguage().translate(p, "vote")).build());
				p.getInventory().setItem(7,
						(debugModePlayers.containsKey(p.getUniqueId()) && debugModePlayers.get(p.getUniqueId()) == true) ?
								new ItemBuilder(Material.INK_SACK).durability(10).name(UHCMeetup.getInstance().getLanguage().translate(p, "dbmodeon")).build()
								: new ItemBuilder(Material.INK_SACK).durability(8).name(UHCMeetup.getInstance().getLanguage().translate(p, "dbmodeoff")).build());
				p.getInventory().setItem(8, new ItemBuilder(Material.BED).name(UHCMeetup.getInstance().getLanguage().translate(p, "spec2")).build());
				if (UHCMeetup.TeamMode) {
					p.getInventory().setItem(1, new ItemBuilder(Material.GOLD_SWORD).name(UHCMeetup.getInstance().getLanguage().translate(p, "teamchoose")).build());
				}
				p.getInventory().setItem(4, new ItemBuilder(Material.SIGN).name(UHCMeetup.getInstance().getLanguage().translate(p, "stats")).build());
				p.updateInventory();
			}
		}.runTaskLater(UHCMeetup.getInstance(), 5L);
	}

	public void checkStart() {
		if (players.size() >= UHCMeetup.getInstance().getConfig().getInt("Min-Player")) {
			if (isInDown == true)
				return;
			new LobbyTask().runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
			isInDown = true;
		}

	}

	public void sendToServer(final Player p) {

		final String server = "waiting";

		final ByteArrayDataOutput out = ByteStreams.newDataOutput();

		out.writeUTF("Connect");
		out.writeUTF(server);

		p.sendPluginMessage(UHCMeetup.getInstance(), "BungeeCord", out.toByteArray());

	}

	public void sendToPractice(final Player p) {
		final String server = "duel";

		final ByteArrayDataOutput out = ByteStreams.newDataOutput();

		out.writeUTF("Connect");
		out.writeUTF(server);

		p.sendPluginMessage(UHCMeetup.getInstance(), "BungeeCord", out.toByteArray());

	}

	public void broadcastNoclean() {

		if (UHCMeetup.TeamMode) {

			if (TeamManager.getInstance().getTeamAlive() < 4 && players.size() > 1) {

				for (final Player player : Bukkit.getOnlinePlayers()) {
					for (final String a : UHCMeetup.getInstance().getLanguage().translateArrays(player, "ppl5BroadCast" + (UHCMeetup.TeamMode ? "_team" : ""))) {
						player.sendMessage(a);
					}
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
				}

				return;

			}

			return;

		}

		if (players.size() <= 5 && players.size() > 1) {

			for (final Player player : Bukkit.getOnlinePlayers()) {
				for (final String a : UHCMeetup.getInstance().getLanguage().translateArrays(player, "ppl5BroadCast" + (UHCMeetup.TeamMode ? "_team" : ""))) {
					player.sendMessage(a);
				}
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
			}

		}

	}

	public void checkWins() {
		if (UHCMeetup.TeamMode) {
			if (TeamManager.getInstance().getLastTeam() != null) {
				final UHCTeam win = TeamManager.getInstance().getLastTeam();

				for (final UUID uuid : win.members) {
					final Player player = Bukkit.getPlayer(uuid);
					new FireworkTask(player).runTaskTimer(UHCMeetup.getInstance(), 10L, 20L);

                    //勝利次數增加1
                    PlayerProfile profile = UHCMeetup.getInstance().getGameManager().getProfile(uuid);
                    profile.addWins();
				}
				SoundUtil.PlaySoundAll(Sound.WITHER_DEATH);

				GameStatus.set(GameStatus.FINISH);

				UHCMeetup.getInstance().getLanguage().send("win_team", RV.o("0", (win.id + 1) + ""));
				UHCMeetup.getInstance().getLanguage().send("team_members", RV.o("0", win.members.stream().map(UUIDCache::getName).collect(Collectors.joining(", "))));

				Bukkit.getScheduler().runTaskLater(UHCMeetup.getInstance(), () -> {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						sendToServer(p);
					}
					Bukkit.getScheduler().runTaskLater(UHCMeetup.getInstance(), () -> Bukkit.shutdown(), 20);
				}, 300L);
			}
			return;
		}
		if (!players.isEmpty() && players.size() == 1) {
			final Player winner = Bukkit.getPlayer(players.get(0));
			new FireworkTask(winner).runTaskTimer(UHCMeetup.getInstance(), 10L, 20L);
			SoundUtil.PlaySoundAll(Sound.WITHER_DEATH);

            //勝利次數增加1
            PlayerProfile profile = getProfile(winner.getUniqueId());
			profile.addWins();

			GameStatus.set(GameStatus.FINISH);

			UHCMeetup.getInstance().getLanguage().send("win", RV.o("0", winner.getName()));

			Bukkit.getScheduler().runTaskLater(UHCMeetup.getInstance(), () -> {
				for (final Player p : Bukkit.getOnlinePlayers()) {
					sendToServer(p);
				}
				Bukkit.getScheduler().runTaskLater(UHCMeetup.getInstance(), Bukkit::shutdown, 20L);
			}, 300L);
		} else if (players.isEmpty()) {
			Bukkit.shutdown();
		}
	}

	public Location generateLocations() {
		Random rand = new Random();
		Location loc;
		World w = Bukkit.getWorld("world");
		int radius = this.getBorder();
		while (true) {
			int x = (int) (rand.nextDouble() * radius * 2.0D - radius);
			int z = (int) (rand.nextDouble() * radius * 2.0D - radius);
			int y = w.getHighestBlockYAt(x, z);
			loc = new Location(w, x, y, z);
			if (!loc.getBlock().getType().equals(Material.STATIONARY_WATER)
					&& !loc.clone().add(0, 1, 0).getBlock().getType().equals(Material.STATIONARY_WATER)
					&& !loc.clone().add(0, 2, 0).getBlock().getType().equals(Material.STATIONARY_WATER))
				break;
		}
		loc.add(0, 0.5, 0);
		w.loadChunk(w.getChunkAt(loc));
		return loc;

	}

	public void setTeamLocation() {
		for (UHCTeam team : TeamManager.getInstance().teams) {
			team.scatter = generateLocations();
		}
	}

	public PlayerProfile getProfile(UUID uuid) {
		return profiles.get(uuid);
	}

}

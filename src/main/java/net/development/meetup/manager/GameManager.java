package net.development.meetup.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.development.meetup.Main;
import net.development.meetup.border.Config;
import net.development.meetup.options.TeamGUI;
import net.development.meetup.player.UHCPlayer;
import net.development.meetup.task.LobbyTask;
import net.development.mitw.utils.ItemBuilder;

public class GameManager {

	public List<UUID> players = new ArrayList<>();
	public List<UUID> spectators = new ArrayList<>();
	public Map<UUID, Boolean> debugModePlayers = new HashMap<>();
	public int max;
	private static int border;
	public static Boolean isInDown = false;
	public Map<UUID, UHCPlayer> getData = new HashMap<>();
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
				p.getInventory().setItem(0, new ItemBuilder(Material.PAINTING).name(Main.get().getLang().translate(p, "vote")).build());
				p.getInventory().setItem(7,
						(debugModePlayers.containsKey(p.getUniqueId()) && debugModePlayers.get(p.getUniqueId()) == true) ?
								new ItemBuilder(Material.INK_SACK).durability(10).name(Main.get().getLang().translate(p, "dbmodeon")).build()
								: new ItemBuilder(Material.INK_SACK).durability(8).name(Main.get().getLang().translate(p, "dbmodeoff")).build());
				p.getInventory().setItem(8, new ItemBuilder(Material.BED).name(Main.get().getLang().translate(p, "spec2")).build());
				if (Main.TeamMode) {
					p.getInventory().setItem(1, new ItemBuilder(Material.GOLD_SWORD).name(Main.get().getLang().translate(p, "teamchoose")).build());
				}
				p.updateInventory();
			}
		}.runTaskLater(Main.get(), 5L);
	}

	public void checkStart() {
		if (players.size() >= Main.get().getConfig().getInt("Min-Player")) {
			if (isInDown == true)
				return;
			new LobbyTask().runTaskTimer(Main.get(), 0L, 20L);
			isInDown = true;
		}

	}

	public void sendToServer(final Player p) {

		final String server = "waiting";

		final ByteArrayDataOutput out = ByteStreams.newDataOutput();

		out.writeUTF("Connect");
		out.writeUTF(server);

		p.sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());

	}

	public void sendToPractice(final Player p) {
		final String server = "duel";

		final ByteArrayDataOutput out = ByteStreams.newDataOutput();

		out.writeUTF("Connect");
		out.writeUTF(server);

		p.sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());

	}

	public void broadcastNoclean() {

		if (Main.TeamMode) {

			if (TeamGUI.getInstance().getTeamAlive() < 4 && Main.getGM().players.size() > 1) {

				for (final Player player : Bukkit.getOnlinePlayers()) {
					for (final String a : Main.get().getLang().translateArrays(player, "ppl5BroadCast" + (Main.TeamMode ? "_team" : ""))) {
						player.sendMessage(a);
					}
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
				}

				return;

			}

			return;

		}

		if (Main.getGM().players.size() <= 5 && Main.getGM().players.size() > 1) {

			for (final Player player : Bukkit.getOnlinePlayers()) {
				for (final String a : Main.get().getLang().translateArrays(player, "ppl5BroadCast" + (Main.TeamMode ? "_team" : ""))) {
					player.sendMessage(a);
				}
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
			}

		}

	}

}

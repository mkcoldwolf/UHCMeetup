package net.development.meetup.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Main;
import net.development.meetup.manager.KitManager;
import net.development.meetup.manager.PlayerManager;
import net.development.meetup.options.checkWin;
import net.development.meetup.player.UHCPlayer;
import net.development.meetup.scenarios.ScenariosEnable;
import net.development.meetup.task.DebugTask;
import net.development.mitw.utils.RV;

public class DeathListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		final UHCPlayer profile = Main.getGM().getData.get(p.getUniqueId());

		p.setHealth(20.0);

		Main.getGM().players.remove(p.getUniqueId());

		if (!ScenariosEnable.TimebombE) {
			p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.EXP_BOTTLE, 24));
			p.getWorld().dropItemNaturally(p.getLocation(), KitManager.goldenHead(1));
		}

		e.setDeathMessage(null);
		executeDeathEvent(e);
		Main.getGM().getData.get(p.getUniqueId()).setAlive(false);

		PlayerManager.setPlayerSpec(p, true);
		((CraftPlayer)p).getHandle().setFakingDeath(true);



		final Player killer = p.getKiller();

		checkWin.checkWins();
		Main.getGM().broadcastNoclean();

		if (killer == null) {
			Main.get().getLang().send("death", RV.o("{0}", p.getName()), RV.o("{1}", profile.getKills() + ""));
			return;
		} else {
			if (Main.getGM().debugModePlayers.containsKey(killer.getUniqueId()) && Main.getGM().debugModePlayers.get(killer.getUniqueId()) == true) {
				new DebugTask(killer).runTaskTimerAsynchronously(Main.get(), 0, 20 * 1);
			}
		}

		final UHCPlayer killerProfile = Main.getGM().getData.get(killer.getUniqueId());

		killerProfile.addKills();
		if (Main.TeamMode) {
			killerProfile.getTeam().kills++;
		}
		Main.get().getLang().send("death_by_player", RV.o("{0}", p.getName()), RV.o("{1}", profile.getKills() + ""), RV.o("{2}", killer.getName()), RV.o("{3}", killerProfile.getKills() + ""));

	}

	private void executeDeathEvent(final PlayerDeathEvent e) {
		if (ScenariosEnable.TimebombE) {
			final Player player = e.getEntity();
			final Location loc = e.getEntity().getLocation();
			player.getLocation().getBlock().setType(Material.CHEST);
			player.getLocation().add(1.0, 0.0, 0.0).getBlock().setType(Material.CHEST);
			player.getLocation().add(0.0, 1.0, 0.0).getBlock().setType(Material.AIR);
			player.getLocation().add(1.0, 1.0, 0.0).getBlock().setType(Material.AIR);
			final Chest chest = (Chest) player.getLocation().getBlock().getState();
			chest.getBlockInventory().setItem(3, player.getInventory().getBoots());
			for (final ItemStack item : e.getDrops()) {
				if (item.getType() == null || item.getType().equals(Material.AIR)) {
					continue;
				}
				chest.getInventory().addItem(item);
			}
			chest.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 24));
			chest.getInventory().addItem(KitManager.goldenHead(1));
			e.getDrops().clear();
			new BukkitRunnable() {
				Integer it = 30;

				@Override
				public void run() {

					if (it >= 1 && it <= 5) {
						loc.getWorld().playSound(loc, Sound.LEVEL_UP, 2.0f, 10.0f);
					}
					if (it <= 0) {
						loc.getBlock().setType(Material.AIR);
						loc.add(1.0, 0.0, 0.0).getBlock().setType(Material.AIR);
						loc.getWorld().createExplosion(loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5,
								3.5f, false, true);
						loc.getWorld().strikeLightning(loc);
						cancel();
						return;
					}
					--it;
				}
			}.runTaskTimer(Main.get(), 20, 20);

		} else {

			for (final ItemStack itemStack : e.getDrops()) {
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), itemStack);
			}

			e.getDrops().clear();

		}
		if (ScenariosEnable.NoCleanE) {
			if (e.getEntity().getKiller() != null) {
				final Player killer = e.getEntity().getKiller();
				killer.sendMessage(Main.get().getLang().translate(killer, "noCleanStart"));
				final UHCPlayer uk = Main.getGM().getData.get(killer.getUniqueId());
				if (uk != null) {
					if (uk.isNoClean())
						return;
					uk.setNoClean(true);
					uk.countDown();
				}
			}
		}

	}

}

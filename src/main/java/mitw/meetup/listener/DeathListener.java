package mitw.meetup.listener;

import lombok.RequiredArgsConstructor;
import mitw.meetup.UHCMeetup;
import mitw.meetup.manager.KitManager;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.scenarios.Scenario;
import mitw.meetup.task.DebugTask;
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

import net.development.mitw.utils.RV;

@RequiredArgsConstructor
public class DeathListener implements Listener {

	private final UHCMeetup plugin;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		final PlayerProfile profile = plugin.getGameManager().getProfile(p.getUniqueId());

		//死亡數增加1
		profile.addDeaths();

		p.setHealth(20.0);

		plugin.getGameManager().players.remove(p.getUniqueId());

		if (!Scenario.TimeBomb.isActive()) {
			p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.EXP_BOTTLE, 24));
			p.getWorld().dropItemNaturally(p.getLocation(), KitManager.goldenHead(1));
		}

		e.setDeathMessage(null);
		executeDeathEvent(e);
		profile.setAlive(false);

		plugin.getPlayerManager().setPlayerSpec(p, true);

		((CraftPlayer)p).getHandle().setFakingDeath(true);
		((CraftPlayer)p).getHandle().getDataWatcher().watch(6, 0.0F);

		final Player killer = p.getKiller();

		plugin.getGameManager().checkWins();
		plugin.getGameManager().broadcastNoclean();

		if (killer == null) {
			plugin.getLanguage().send("death", RV.o("{0}", p.getName()), RV.o("{1}", profile.getKills() + ""));
			return;
		} else {
			if (plugin.getGameManager().debugModePlayers.containsKey(killer.getUniqueId()) && plugin.getGameManager().debugModePlayers.get(killer.getUniqueId())) {
				new DebugTask(killer).runTaskTimerAsynchronously(plugin, 0L, 20L);
			}
		}

		final PlayerProfile killerProfile = plugin.getGameManager().getProfile(killer.getUniqueId());

		//擊殺數增加1
		killerProfile.addKills();
		killerProfile.addGlobalKills();
		if (UHCMeetup.TeamMode) {
			killerProfile.getTeam().kills++;
		}
		plugin.getLanguage().send("death_by_player", RV.o("{0}", p.getName()), RV.o("{1}", profile.getKills() + ""), RV.o("{2}", killer.getName()), RV.o("{3}", killerProfile.getKills() + ""));

	}

	private void executeDeathEvent(final PlayerDeathEvent e) {
		if (Scenario.TimeBomb.isActive()) {
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
			}.runTaskTimer(plugin, 20, 20);

		} else {

			for (final ItemStack itemStack : e.getDrops()) {
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), itemStack);
			}

			e.getDrops().clear();

		}
		if (Scenario.NoClean.isActive()) {
			if (e.getEntity().getKiller() != null) {
				final Player killer = e.getEntity().getKiller();
				killer.sendMessage(plugin.getLanguage().translate(killer, "noCleanStart"));
				final PlayerProfile uk = plugin.getGameManager().getProfile(killer.getUniqueId());
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

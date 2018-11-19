package net.development.meetup.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.util.Tools;
import net.development.mitw.utils.ItemBuilder;

public class PlayerManager {

	public static void setPlayerSolo(final Player p) {
		Main.getGM().players.add(p.getUniqueId());
	}

	public static void setPlayerSpec(final Player p) {

		Bukkit.getScheduler().runTaskLater(Main.get(), () -> {

			for (final Player p1 : Bukkit.getOnlinePlayers()) {
				if (p1 != null && p1 != p) {
					p1.hidePlayer(p);
					if (Main.getGM().spectators.contains(p1.getUniqueId())) {
						p.hidePlayer(p1);
					}
				}
			}

		}, 20L);

		p.setGameMode(GameMode.CREATIVE);
		new BukkitRunnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.getInventory().setItem(0, new ItemBuilder(Material.SLIME_BALL).name(Main.get().getLang().translate(p, "spec1")).build());
				p.getInventory().setItem(8, new ItemBuilder(Material.BED).name(Main.get().getLang().translate(p, "spec2")).build());
				p.getInventory().setItem(7, new ItemBuilder(Material.DIAMOND_SWORD).name(Main.get().getLang().translate(p, "reTurnToPractice")).build());
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));
				p.updateInventory();
			}
		}.runTaskLater(Main.get(), 5L);
		Main.getGM().spectators.add(p.getUniqueId());
	}

	public static void setPlayerDBMode(final Player p) {
		final UUID u = p.getUniqueId();
		if (Main.getGM().debugModePlayers.containsKey(u) && Main.getGM().debugModePlayers.get(u) == true) {
			Main.getGM().debugModePlayers.put(u, false);
			p.sendMessage(Tools.colored(Lang.PREFIX + Main.get().getLang().translate(p, "cleandb_off")));
			p.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).data(8).name(Main.get().getLang().translate(p, "dbmodeoff")).build());
		} else {
			Main.getGM().debugModePlayers.put(u, true);
			p.sendMessage(Tools.colored(Lang.PREFIX + Main.get().getLang().translate(p, "cleandb_on")));
			p.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).data(10).name(Main.get().getLang().translate(p, "dbmodeon")).build());
		}
	}

}

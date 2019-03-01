package mitw.meetup.manager;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import mitw.meetup.Lang;
import mitw.meetup.UHCMeetup;
import mitw.meetup.board.Board;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import mitw.meetup.util.Util;
import net.development.mitw.utils.ItemBuilder;

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
				plugin.getSidebarManager().getPlayerBoards().get(p.getUniqueId()).setColoredTag();
			}
		}.runTaskLater(plugin.getInstance(), 5L);
	}

	public void setPlayerDBMode(final Player p) {
		final UUID u = p.getUniqueId();
		if (plugin.getGameManager().debugModePlayers.containsKey(u) && plugin.getGameManager().debugModePlayers.get(u) == true) {
			plugin.getGameManager().debugModePlayers.put(u, false);
			p.sendMessage(Util.colored(Lang.PREFIX + plugin.getInstance().getLanguage().translate(p, "cleandb_off")));
			p.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).durability(8).name(plugin.getInstance().getLanguage().translate(p, "dbmodeoff")).build());
		} else {
			plugin.getGameManager().debugModePlayers.put(u, true);
			p.sendMessage(Util.colored(Lang.PREFIX + plugin.getInstance().getLanguage().translate(p, "cleandb_on")));
			p.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).durability(10).name(plugin.getInstance().getLanguage().translate(p, "dbmodeon")).build());
		}
	}

}

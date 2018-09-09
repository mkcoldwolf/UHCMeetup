package net.development.meetup.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Main;

public class PlayerManager {
	
	public static void setPlayerSolo(Player p) {
		Main.getGM().players.add(p.getUniqueId());
	}

	public static void setPlayerSpec(Player p) {
		for(Player p1 : Bukkit.getOnlinePlayers()) {
			if(p1 != null && p1 != p) {
				p1.hidePlayer(p);
				if(Main.getGM().spectators.contains(p1.getUniqueId())) {
					p.hidePlayer(p1);
				}
			}
		}
		p.setGameMode(GameMode.CREATIVE);
		new BukkitRunnable() {
			@Override
			public void run() {
				p.getInventory().clear();
				p.getInventory().setItem(0, KitManager.spec1);
				p.getInventory().setItem(8, KitManager.spec2);
				p.getInventory().setItem(7, KitManager.retunToArenaPvP);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));
				p.updateInventory();
			}
		}.runTaskLater(Main.get(), 5L);
		Main.getGM().spectators.add(p.getUniqueId());
	}

}

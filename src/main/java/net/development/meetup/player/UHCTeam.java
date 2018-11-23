package net.development.meetup.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.development.meetup.Main;

public class UHCTeam {

	public UUID p1;
	public UUID p2;
	public int id;
	public Location scatter;
	public int kills;

	public UHCTeam(final Player p1, final Player p2, final int id) {
		this.p1 = p1 == null ? null : p1.getUniqueId();
		this.p2 = p2 == null ? null : p2.getUniqueId();
		this.id = id;
	}

	public boolean isAlive() {
		return (Bukkit.getPlayer(p1) != null && Main.getGM().players.contains(p1)) || (Bukkit.getPlayer(p2) != null && Main.getGM().players.contains(p2));
	}

}

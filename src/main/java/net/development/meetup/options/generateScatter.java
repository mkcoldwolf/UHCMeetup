package net.development.meetup.options;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import net.development.meetup.Main;
import net.development.meetup.UHCTeam;

public class generateScatter {

	public static Location generateNewScatter() {
		Random rand = new Random();
		Location loc = null;
		World w = Bukkit.getWorld("world");
		int radius = Main.getGM().getBorder();
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
	
	public static void setTeamLocation() {
		for(UHCTeam team : TeamGUI.getInstance().teams) {
			team.scatter = generateNewScatter();
		}
	}

}

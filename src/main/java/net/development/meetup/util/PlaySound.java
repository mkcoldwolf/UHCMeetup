package net.development.meetup.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.development.meetup.Main;


public class PlaySound {
	
	public static void PlayerSound(Player p, Sound sound, float level, float level2) {
		p.playSound(p.getLocation(), sound, level, level2);
	}
	
	public static void PlayerSound(Player p, Sound sound, float level) {
		PlayerSound(p, sound, level, 1f);
	}
	
	public static void PlayerSound(Player p, Sound sound) {
		PlayerSound(p, sound, 1F, 1F);
	}
	
	public static void PlaySoundAll(Sound sound, float level, float level2) {
		Main.get().getOnlinePlayers().forEach(u -> {
			Player p = Bukkit.getPlayer(u);
			if(p != null)
				PlayerSound(p, sound, level, level2);	
		});
	}
	
	public static void PlaySoundAll(Sound sound) {
		Main.get().getOnlinePlayers().forEach(u -> {
			Player p = Bukkit.getPlayer(u);
			if(p != null)
				PlayerSound(p, sound, 1f, 1f);	
		});
	}

}

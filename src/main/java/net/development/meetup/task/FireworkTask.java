package net.development.meetup.task;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FireworkTask extends BukkitRunnable{
	
	private int time = 0;
	private Player p = null;
	private Builder builder = FireworkEffect.builder();
	
	public FireworkTask(Player p) {
		time = 0;
		this.p = p;
	}

	@Override
	public void run() {
		this.SpawnFireWorks(p.getLocation());
		if(time == 10) {
			time = 0;
			cancel();
		}
		time++;
	}
	
	private void SpawnFireWorks(Location loc) {
    	Firework fw = p.getWorld().spawn(loc, Firework.class);
    	FireworkMeta fwm = fw.getFireworkMeta();
    	
    	fwm.addEffect(builder.flicker(true).withColor(Color.ORANGE).build());
    	fwm.addEffect(builder.trail(true).build());
    	fwm.addEffect(builder.withFade(Color.AQUA).build());
    	fwm.setPower(1);
    	fw.setFireworkMeta(fwm);
    }

}
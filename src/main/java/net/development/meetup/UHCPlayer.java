package net.development.meetup;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UHCPlayer {

	private UUID player;
	private float NoClean;
	private Boolean isNoClean;
	private int kills;
	private boolean isAlive;
	
	private UHCTeam teamIn = null;
	private int teamP = 0;

	public UHCPlayer(Player p) {
		this.player = p.getUniqueId();
		this.isNoClean = false;
		this.kills = 0;
		this.isAlive = true;
	}

	public Player getPlayerData() {
		return Bukkit.getPlayer(player);
	}

	public Boolean isNoClean() {
		return isNoClean;
	}

	public Boolean setNoClean(Boolean var1) {
		isNoClean = var1;
		return isNoClean;
	}

	public float getNoCleanTimer() {
		return NoClean;
	}

	public float setNoCleanTimer(Integer var1) {
		NoClean = var1;
		return NoClean;
	}

	public int getKills() {
		return kills;
	}

	public int addKills() {
		this.kills++;
		return kills;
	}

	public int setKills(Integer var1) {
		this.kills = var1;
		return kills;
	}

	public void countDown() {
		Player p = Bukkit.getPlayer(player);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!isNoClean || p == null || !p.isOnline()) {
					cancel();
					return;
				}
				if (NoClean <= 0.1) {
					isNoClean = false;
					if (Bukkit.getPlayer(player) != null)
						getPlayerData().sendMessage(Main.get().getLang().translate(p, "noCleanStop"));
					cancel();
					return;
				}
				NoClean -= 0.1;
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 2L);
	}

	public boolean isAlive() {
		return this.isAlive;

	}

	public boolean setAlive(Boolean var1) {
		this.isAlive = var1;
		return this.isAlive;
	}
	
	public boolean isInTeam() {
		return teamIn != null;
	}
	
	public UHCTeam getTeam() {
		return teamIn;
	}
	
	public int getTeamMemeberLoc() {
		return teamP;
	}
	
	public void setTeam(UHCTeam t, int i) {
		this.teamIn = t;
		this.teamP = i;
	}

}

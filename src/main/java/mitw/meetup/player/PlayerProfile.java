package mitw.meetup.player;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import mitw.meetup.impl.NocleanTimer;
import mitw.meetup.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;

public class PlayerProfile {

	private final UUID player;
	private int kills;
	private boolean isNoClean;
	private boolean isAlive;

	@Getter
	@Setter
	private Scenario voted;

	private UHCTeam teamIn = null;
	private int teamP = 0;

	public PlayerProfile(final Player p) {
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

	public Boolean setNoClean(final Boolean var1) {
		isNoClean = var1;
		return isNoClean;
	}

	public int getKills() {
		return kills;
	}

	public int addKills() {
		this.kills++;
		return kills;
	}

	public int setKills(final Integer var1) {
		this.kills = var1;
		return kills;
	}

	public float getNocleanTimer() {
		return Mitw.getInstance().getTimerManager().getTimer(NocleanTimer.class).getRemaining(player) / 1000.0F;
	}

	public void countDown() {

		final Player player = this.getPlayerData();

		if (player == null)
			return;

		Mitw.getInstance().getTimerManager().getTimer(NocleanTimer.class).setCooldown(player, player.getUniqueId());

	}

	public boolean isAlive() {
		return this.isAlive;

	}

	public boolean setAlive(final Boolean var1) {
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

	public void setTeam(final UHCTeam t, final int i) {
		this.teamIn = t;
		this.teamP = i;
	}

}

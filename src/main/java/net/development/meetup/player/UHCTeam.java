package net.development.meetup.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.options.TeamGUI;

public class UHCTeam {

	public List<UUID> members = new ArrayList<>();
	public int id;
	public Location scatter;
	public int kills;

	public UHCTeam(final int id) {
		this.id = id;
	}

	public boolean isAlive() {
		for (final UUID uuid : members) {
			if (Main.getGM().players.contains(uuid))
				return true;
		}
		return false;
	}

	public static void fillTeams() {
		loop: for (final Player p : Main.get().getServer().getOnlinePlayers()) {
			final UHCPlayer up = Main.getGM().getData.get(p.getUniqueId());
			if (!up.isInTeam()) {
				for (int i = 0; i < 27; i++) {
					final UHCTeam team = TeamGUI.getInstance().teams.get(i);
					if (team.members.size() < 2) {
						team.members.add(p.getUniqueId());
						up.setTeam(team, i);
						p.sendMessage("§a你加入了隊伍 " + (i + 1) + " !");
						continue loop;
					}
				}
			}
		}
	}

}

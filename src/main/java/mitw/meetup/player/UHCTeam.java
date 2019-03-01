package mitw.meetup.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mitw.meetup.UHCMeetup;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import mitw.meetup.manager.TeamManager;

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
			if (UHCMeetup.getInstance().getGameManager().players.contains(uuid))
				return true;
		}
		return false;
	}

	public static void fillTeams() {
		loop: for (final Player p : UHCMeetup.getInstance().getServer().getOnlinePlayers()) {
			final PlayerProfile up = UHCMeetup.getInstance().getGameManager().getProfile(p.getUniqueId());
			if (!up.isInTeam()) {
				for (int i = 0; i < 27; i++) {
					final UHCTeam team = TeamManager.getInstance().teams.get(i);
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

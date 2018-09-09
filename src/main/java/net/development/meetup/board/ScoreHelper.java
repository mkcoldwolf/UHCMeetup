package net.development.meetup.board;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.development.meetup.Main;
import net.development.meetup.UHCPlayer;
import net.development.meetup.enums.Status;

/**
 *
 * @author crisdev333
 *
 */
public class ScoreHelper {

	public static HashMap<UUID, ScoreHelper> players = new HashMap<>();

	public static boolean hasScore(Player player) {
		return players.containsKey(player.getUniqueId());
	}

	public static ScoreHelper createScore(Player player) {
		return new ScoreHelper(player);
	}

	public static ScoreHelper getByPlayer(Player player) {
		return players.get(player.getUniqueId());
	}

	public static ScoreHelper removeScore(Player player) {
		return players.remove(player.getUniqueId());
	}
	
	private final UUID uuid;

	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private final Objective objective2_1_7 = scoreboard.registerNewObjective("h18", "dummy");
	private final Objective objective3 = scoreboard.registerNewObjective("h1", "health");
	private Objective sidebar;

	private ScoreHelper(Player player) {
		uuid = player.getUniqueId();
		sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		// Create Teams
		for (int i = 1; i <= 15; i++) {
			Team team = scoreboard.registerNewTeam("SLOT_" + i);
			team.addEntry(genEntry(i));
		}
		if (Status.isState(Status.PVP)) {
			setColoredTag();
			objective2_1_7.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			updateHealth();
			objective3.setDisplayName("§c\u2764");
			objective3.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		player.setScoreboard(scoreboard);
		players.put(player.getUniqueId(), this);
	}

	private void updateHealth() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					Damageable d = (Damageable)p;
    		    	if (objective2_1_7.getScore(p.getName()) == null) {
    		    		objective2_1_7.getScore(p.getName()).setScore((int)d.getHealth());
    		    	} else {
    		    		objective2_1_7.getScore(p.getName()).setScore((int)d.getHealth());
    		    	}
				}
			}
		}.runTaskTimerAsynchronously(Main.get(), 10L, 10L);
	}

	public void setTitle(String title) {
		title = ChatColor.translateAlternateColorCodes('&', title);
		sidebar.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);
	}
	
	@SuppressWarnings("deprecation")
	public void setColoredTag() {
		UHCPlayer up = Main.getGM().getData.get(uuid);
		Team st = scoreboard.getTeam("team");
		if(st == null)st = scoreboard.registerNewTeam("team");
		st.setPrefix("§a");
		st.setAllowFriendlyFire(false);
		if(up.isInTeam()) {
			if(up.getTeam().p1 != null)st.addPlayer(up.getTeam().p1);
			if(up.getTeam().p2 != null)st.addPlayer(up.getTeam().p2);
		}else {
			st.addPlayer(Bukkit.getPlayer(uuid));
		}
	}

	public void setSlot(int slot, String text) {
		Team team = scoreboard.getTeam("SLOT_" + slot);
		String entry = genEntry(slot);
		if (!scoreboard.getEntries().contains(entry)) {
			sidebar.getScore(entry).setScore(slot);
		}

		text = ChatColor.translateAlternateColorCodes('&', text);
		String[] ts = this.splitStringLine(text);
		team.setPrefix(ts[0]);
		team.setSuffix(ts[1]);
	}

	public void removeSlot(int slot) {
		String entry = genEntry(slot);
		if (scoreboard.getEntries().contains(entry)) {
			scoreboard.resetScores(entry);
		}
	}

	public void setSlotsFromList(List<String> list) {
		while (list.size() > 15) {
			list.remove(list.size() - 1);
		}

		int slot = list.size();

		if (slot < 15) {
			for (int i = (slot + 1); i <= 15; i++) {
				removeSlot(i);
			}
		}

		for (String line : list) {
			setSlot(slot, line);
			slot--;
		}
	}

	private String genEntry(int slot) {
		return ChatColor.values()[slot].toString();
	}

	private String[] splitStringLine(String string) {

		StringBuilder prefix = new StringBuilder(string.substring(0, string.length() >= 16 ? 16 : string.length()));
		StringBuilder suffix = new StringBuilder(string.length() > 16 ? string.substring(16) : "");

		if ((prefix.toString().length() > 1) && (prefix.charAt(prefix.length() - 1) == '§')) {
			prefix.deleteCharAt(prefix.length() - 1);
			suffix.insert(0, '§');
		}

		String last = "";

		int i = 0;

		while (i < prefix.toString().length()) {

			char c = prefix.toString().charAt(i);

			if (c == '§') {
				if (i < prefix.toString().length() - 1) {
					last = last + "§" + prefix.toString().charAt(i + 1);
				}
			}

			++i;
		}

		String s2 = "" + suffix;

		if (prefix.length() > 14) {
			s2 = !last.isEmpty() ? String.valueOf(last) + s2 : "§" + s2;
		}

		return new String[] { prefix.toString().length() > 16 ? prefix.toString().substring(0, 16) : prefix.toString(),

				s2.toString().length() > 16 ? s2.toString().substring(0, 16) : s2.toString() };

	}

}
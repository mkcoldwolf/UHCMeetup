package mitw.meetup.board;

import java.util.*;

import mitw.meetup.UHCMeetup;
import mitw.meetup.manager.TeamManager;
import mitw.meetup.player.UHCTeam;
import net.development.mitw.uuid.UUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;
import net.development.mitw.utils.StringUtil;

@Getter
public class Board {

	private final BoardAdapter adapter;
	private final Player player;
	private final UUID uuid;
	private final List<BoardEntry> entries = new ArrayList<>();
	private final Set<BoardTimer> timers = new HashSet<>();
	private final Set<String> keys = new HashSet<>();
	private Scoreboard scoreboard;
	private Objective objective;
	private final Map<String, Team> prefixs = new HashMap<String, Team>() {
		private static final long serialVersionUID = 1L;

		@Override
		public Team put(String key, final Team value) {
			key = StringUtil.replace(key, "§", "");
			return super.put(key, value);
		}

		@Override
		public Team get(final Object key) {
			if (key instanceof String) {
				String string = (String) key;
				string = StringUtil.replace(string, "§", "");
				return super.get(string);
			}
			return super.get(key);
		}
	};

	public Board(final JavaPlugin plugin, final Player player, final BoardAdapter adapter) {
		this.adapter = adapter;
		this.player = player;
		this.uuid = player.getUniqueId();

		this.init(plugin);
	}

	private void init(final JavaPlugin plugin) {
		if (!this.player.getScoreboard().equals(plugin.getServer().getScoreboardManager().getMainScoreboard())) {
			this.scoreboard = this.player.getScoreboard();
		} else {
			this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
		}

		this.objective = this.scoreboard.registerNewObjective("Default", "dummy");

		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.objective.setDisplayName(this.adapter.getTitle(player));

		scoreboard.registerNewObjective("tabhealth", "health").setDisplaySlot(DisplaySlot.PLAYER_LIST);

		final Objective belowObjective = scoreboard.registerNewObjective("belowNameHealth", "health");
		belowObjective.setDisplayName("§c§l\u2764");
		belowObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);

	}

	public void setSpectateTag() {
		if (UHCMeetup.getInstance().getGameManager().spectators.contains(uuid)) {
			Team spectateTeam = scoreboard.getTeam("9spectate");
			if (spectateTeam == null) {
				spectateTeam = scoreboard.registerNewTeam("9spectate");
			}
			spectateTeam.setPrefix("§8");
			spectateTeam.addEntry(UUIDCache.getName(uuid));
		}
	}

	public void setColoredTag() {

		boolean spectating = false;

		if (UHCMeetup.getInstance().getGameManager().spectators.contains(uuid)) {
			Team spectateTeam = scoreboard.getTeam("9spectate");
			if (spectateTeam == null) {
				spectateTeam = scoreboard.registerNewTeam("9spectate");
			}
			spectateTeam.setPrefix("§8");
			spectateTeam.addEntry(UUIDCache.getName(uuid));
			spectating = true;
		}

		if (UHCMeetup.TeamMode) {
			final UHCTeam uTeam = UHCMeetup.getInstance().getGameManager().getProfile(uuid).getTeam();

			final Map<Integer, Team> teams = new HashMap<>();

			for (final UHCTeam team : TeamManager.getInstance().teams) {
				Team t;
				if (!teams.containsKey(team.id)) {
					final String teamName = uTeam != null ? team.id == uTeam.id ? "1team" + team.id : "team" + team.id : "team" + team.id;
					t = scoreboard.getTeam(teamName);
					if (t == null) {
						t = scoreboard.registerNewTeam(teamName);
					}
					t.setPrefix((uTeam != null ? (uTeam.id == team.id ? "§a" : "§c") : "§c") + "[" + (team.id + 1) + "]");
					teams.put(team.id, t);
				} else {
					t = teams.get(team.id);
				}
				for (final UUID uuid : team.members) {

					if (!UHCMeetup.getInstance().getGameManager().getProfile(uuid).isAlive()) {
						continue;
					}

					t.addEntry(UUIDCache.getName(uuid));
				}
			}
		} else {

			if (!spectating) {

				Team team1 = scoreboard.getTeam("1team");
				if (team1 == null) {
					team1 = scoreboard.registerNewTeam("1team");
				}
				team1.setPrefix(ChatColor.GREEN.toString());
				team1.addEntry(UUIDCache.getName(uuid));

			}

			Team team2 = scoreboard.getTeam("2team");
			if (team2 == null) {
				team2 = scoreboard.registerNewTeam("2team");
			}
			team2.setPrefix(ChatColor.RED.toString());

			for (final Player player : Bukkit.getOnlinePlayers()) {

				if (player.getUniqueId() != uuid) {

					team2.addEntry(player.getName());

				}

			}

		}


	}

	public String getNewKey(final BoardEntry entry) {
		for (final ChatColor color : ChatColor.values()) {
			String colorText = color + "" + ChatColor.WHITE;

			if (entry.getText().length() > 16) {
				final String sub = entry.getText().substring(0, 16);
				colorText = colorText + ChatColor.getLastColors(sub);
			}

			if (!keys.contains(colorText)) {
				keys.add(colorText);
				return colorText;
			}
		}

		throw new IndexOutOfBoundsException("No more keys available!");
	}

	public List<String> getBoardEntriesFormatted() {
		final List<String> toReturn = new ArrayList<>();

		for (final BoardEntry entry : new ArrayList<>(entries)) {
			toReturn.add(entry.getText());
		}

		return toReturn;
	}

	public BoardEntry getByPosition(final int position) {
		for (int i = 0; i < this.entries.size(); i++) {
			if (i == position)
				return this.entries.get(i);
		}

		return null;
	}

	public BoardTimer getCooldown(final String id) {
		for (final BoardTimer cooldown : getTimers()) {
			if (cooldown.getId().equals(id))
				return cooldown;
		}

		return null;
	}

	public Set<BoardTimer> getTimers() {
		this.timers.removeIf(cooldown -> System.currentTimeMillis() >= cooldown.getEnd());
		return this.timers;
	}

	public Objective getTabObjective() {
		return scoreboard.getObjective("tabhealth");
	}

}
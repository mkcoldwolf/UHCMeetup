package mitw.meetup.player;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import mitw.meetup.UHCMeetup;
import mitw.meetup.impl.NocleanTimer;
import mitw.meetup.scenarios.Scenario;
import mitw.meetup.util.UHCMeetupDatabase;
import net.development.mitw.player.PlayerInfo;
import net.development.mitw.utils.FastUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;

public class PlayerProfile extends PlayerInfo {

	@Getter private final UUID player;
	private int kills;
	private boolean isNoClean;
	private boolean isAlive;

	@Getter @Setter private int global_kills;
    @Getter @Setter private int deaths;
    @Getter @Setter private int wins;
    @Getter @Setter private int elo;
    @Getter @Setter private int games_played;

	@Getter
	@Setter
	private Scenario voted;

	private UHCTeam teamIn = null;
	private int teamP = 0;

	public PlayerProfile(final Player p) {
        super(p.getUniqueId(), null);
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

	public void addGlobalKills() {
	    this.global_kills++;
    }

    public void addDeaths() {
	    this.deaths++;
    }

    public void addWins() {
	    this.wins++;
    }

    public void addGamesPlayed() {
	    this.games_played++;
    }

    public void setElo(int elo) {
	    this.elo = elo;
    }

    public void load() {
        UHCMeetupDatabase database = UHCMeetup.getInstance().getDatabase();

        if (this.hasData(database)) {
            database.getTable().executeSelect("uuid = ?")
                    .dataSource(database.getDatabase().getDataSource())
                    .statement(s -> s.setString(1, FastUUID.toString(getPlayer())))
                    .result(result -> {
                        if (result.isBeforeFirst()) {
                            while (result.next()) {
                                if(this.getName() == null) {
                                    setName(result.getString("name"));
                                }
                                setGlobal_kills(result.getInt("global_kills"));
                                setWins(result.getInt("wins"));
                                setDeaths(result.getInt("deaths"));
                                setGames_played(result.getInt("games_played"));
                                setElo(result.getInt("elo"));
                            }
                        }
                        return null;
                    }).run();
        }
    }

	public void save() {
		UHCMeetupDatabase database = UHCMeetup.getInstance().getDatabase();
		if (this.hasData(database)) {
			database.getTable().executeUpdate("UPDATE `uhcmeetup_player` SET " +
					"`name` = ?, " +
					"`global_kills` = ?, " +
					"`wins` = ?, " +
                    "`deaths` = ?, " +
                    "`games_played` = ?, " +
					"`elo` = ? WHERE `uuid` = ?;")
					.dataSource(database.getDatabase().getDataSource())
					.statement(s -> {
						s.setString(1, this.getPlayerData().getName());
						s.setInt(2, this.global_kills);
						s.setInt(3, this.wins);
						s.setInt(4, this.deaths);
						s.setInt(5, this.games_played);
						s.setInt(6, this.elo);
						s.setString(7, FastUUID.toString(getPlayer()));
					}).run();
		} else {
			database.getTable().executeInsert("?, ?, ?, ?, ?, ?, ?")
					.dataSource(database.getDatabase().getDataSource())
					.statement(s -> {
						s.setString(1, FastUUID.toString(getPlayer()));
						s.setString(2, this.getPlayerData().getName());
						s.setInt(3, this.global_kills);
						s.setInt(4, this.wins);
						s.setInt(5, this.deaths);
                        s.setInt(6, this.games_played);
						s.setInt(7, this.elo);
					}).run();
		}
	}

    private boolean hasData(UHCMeetupDatabase database) {
        return database.getTable().executeSelect("uuid = ?")
                .dataSource(database.getDatabase().getDataSource())
                .statement(s -> s.setString(1, FastUUID.toString(getPlayer())))
                .resultNext(r -> true)
                .run(false, false);
    }

}

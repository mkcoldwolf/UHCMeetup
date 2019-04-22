package mitw.meetup.player;

import lombok.Getter;
import mitw.meetup.UHCMeetup;
import mitw.meetup.player.database.UHCMeetupDatabase;
import net.development.mitw.utils.FastUUID;

import java.util.UUID;

public class PlayerCache implements IPlayerCache {

    @Getter
    private String name;

    @Getter
    private UUID uuid;

    @Getter
    private int global_kills;
    @Getter
    private int deaths;
    @Getter
    private int wins;
    @Getter
    private int elo;
    @Getter
    private int games_played;

    public PlayerCache(String name) {
        this.name = name;
    }

    public boolean load() {
        UHCMeetupDatabase database = UHCMeetup.getInstance().getLeaderboardManager().getDatabase();

        if (this.hasData(database)) {
            database.getTable().executeSelect("name = ?")
                    .dataSource(database.getDatabase().getDataSource())
                    .statement(s -> s.setString(1, name))
                    .result(result -> {
                        if (result.isBeforeFirst()) {
                            while (result.next()) {
                                uuid = FastUUID.parseUUID(result.getString("uuid"));
                                global_kills = (result.getInt("global_kills"));
                                wins = (result.getInt("wins"));
                                deaths = (result.getInt("deaths"));
                                games_played = (result.getInt("games_played"));
                                elo = (result.getInt("elo"));
                            }
                        }
                        return null;
                    }).run();
            return true;
        } else {
            return false;
        }
    }

    private boolean hasData(UHCMeetupDatabase database) {
        return database.getTable().executeSelect("name = ?")
                .dataSource(database.getDatabase().getDataSource())
                .statement(s -> s.setString(1, name))
                .resultNext(r -> true)
                .run(false, false);
    }

}

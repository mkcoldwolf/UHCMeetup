package mitw.meetup.player;

import java.text.DecimalFormat;
import java.util.UUID;

public interface IPlayerCache {

    String getName();

    UUID getUuid();

    int getGlobal_kills();

    int getDeaths();

    int getWins();

    int getElo();

    int getGames_played();

    default String getKDR() {
        return new DecimalFormat("0.0").format(getDeaths() == 0 ? (double) getGlobal_kills() : ((double)getGlobal_kills() / getDeaths()));
    }
}

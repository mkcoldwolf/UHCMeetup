package mitw.meetup.board.adapter;

import mitw.meetup.Lang;
import mitw.meetup.UHCMeetup;
import mitw.meetup.board.Board;
import mitw.meetup.board.BoardAdapter;
import mitw.meetup.enums.GameStatus;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.scenarios.Scenario;
import mitw.meetup.task.GameTask;
import mitw.meetup.task.PVPBorderTask;
import mitw.meetup.task.ReleaseTask;
import mitw.meetup.task.SuddenDeathTask;
import mitw.meetup.util.CStringBuffer;
import mitw.meetup.util.Util;
import net.development.mitw.utils.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UHCMeetupAdapter implements BoardAdapter {

    @Override
    public long getInterval() {
        return 2;
    }

    @Override
    public String getTitle(Player player) {
        return StringUtil.cc(Lang.Title);
    }

    @Override
    public List<String> getScoreboard(Player player, Board board) {
        List<String> lines = new ArrayList<>();

        String type = this.getType(player);

        for (String line : UHCMeetup.getInstance().getLanguage().translateArrays(player, type)) {

            if (line.equals("<noclean>")) {

                if (UHCMeetup.getInstance().getGameManager().getProfile(player.getUniqueId()).isNoClean()) {

                    lines.add(this.placeholder(player, UHCMeetup.getInstance().getLanguage().translate(player, "noClean")));

                } else {

                    continue;

                }

            } else if (line.equals("<suddenDeath>")) {

                if (SuddenDeathTask.isTimerStarted()) {

                    lines.add(UHCMeetup.getInstance().getLanguage().translate(player, "sudden_death") + ": §6" + Util.getTimeHora(SuddenDeathTask.getCountdown()));

                } else {

                    continue;

                }

            } else {

                lines.add(this.placeholder(player, line));

            }

        }

        return lines;
    }

    @Override
    public void onScoreboardCreate(Player player, Scoreboard board) {

    }

    @Override
    public void preLoop() {

    }

    public String getType(Player player) {
        switch (GameStatus.get()) {
            case LOADING:
                return null;
            case WAITING:
                return "LobbyList";
            case TELEPORT:
                return "TPList";
            case PVP:
                if (UHCMeetup.TeamMode) {
                    return "SoloList";
                } else {
                    return "TeamList";
                }
            case FINISH:
                if (UHCMeetup.TeamMode) {
                    return "FinishTeamList";
                } else {
                    return "FinishList";
                }
        }
        return null;
    }

    private DecimalFormat timerFormat = new DecimalFormat("0.0");

    public String placeholder(Player player, String string) {
        final PlayerProfile up = UHCMeetup.getInstance().getGameManager().getProfile(player.getUniqueId());
        CStringBuffer stringBuffer = new CStringBuffer(string);
        switch (GameStatus.get()) {
            case LOADING:
            case WAITING:
                return stringBuffer.replaceAll("<players>", "" + UHCMeetup.getInstance().getGameManager().players.size()).replaceAll("&", "§")
                        .replaceAll("<starting>", "" + Util.getStartingText(player))
                        .replaceAll("<s1>", Scenario.TimeBomb.getVotes() + "/3")
                        .replaceAll("<s2>", Scenario.NoClean.getVotes() + "/3")
                        .replaceAll("<s3>", Scenario.BowLess.getVotes() + "/3")
                        .replaceAll("<s4>", Scenario.RodLess.getVotes() + "/3")
                        .replaceAll("<s5>", Scenario.FireLess.getVotes() + "/3")
                        .replaceAll("<s6>", Scenario.AirDrops.getVotes() + "/3")
                        .replaceAll("<s7>", Scenario.IronRush.getVotes() + "/3")
                        .replaceAll("<server>", UHCMeetup.serverName).toString();

            case FINISH:
            case PVP:

                stringBuffer.replaceAll("<players>", "" + UHCMeetup.getInstance().getGameManager().players.size()).replaceAll("&", "§")
                        .replaceAll("<border>", "" + UHCMeetup.getInstance().getGameManager().getBorder() + "")
                        .replaceAll("<time>", Util.getTimeHora(GameTask.time))
                        .replaceAll("<kills>", up.getKills() + "")
                        .replaceAll("<teamKills>", up.isInTeam() ? up.getTeam().kills + "" : up.getKills() + "")
                        .replaceAll("<max>", UHCMeetup.getInstance().getGameManager().max + "")
                        .replaceAll("<cleanTime>", timerFormat.format(up.getNocleanTimer()));

                if (string.contains("<format>")) {

                    String untiShrink = "§7|§c " + Util.UntiShrinkTime(PVPBorderTask.sec);
                    if (PVPBorderTask.list.isEmpty()) {
                        untiShrink = "";
                    }

                    stringBuffer.replaceAll("<format>", untiShrink);

                }

                return stringBuffer.toString();

            case TELEPORT:

                return stringBuffer.replaceAll("<players>", "" + UHCMeetup.getInstance().getGameManager().players.size())
                        .replaceAll("<starting>", (ReleaseTask.RELEASE_TIME + 1) + "s").toString();

        }
        return stringBuffer.toString();
    }
}

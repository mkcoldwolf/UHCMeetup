package mitw.meetup.commands;

import mitw.meetup.UHCMeetup;
import mitw.meetup.gui.StatsGUI;
import mitw.meetup.manager.TeamManager;
import mitw.meetup.player.PlayerCache;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.scenarios.ScenarioMenu;
import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import net.development.mitw.uuid.UUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MeetupCommands {

    @Command(names = {"vote"})
    public static void vote(Player player) {
        new ScenarioMenu().openMenu(player);
    }

    @Command(names = {"team"})
    public static void team(Player player) {
        if (!UHCMeetup.TeamMode) {
            player.sendMessage(UHCMeetup.getInstance().getLanguage().translate(player, "teammode_not_enable"));
            return;
        }
        TeamManager.getInstance().openGUI(player);
    }

    @Command(names = {"stats"}, async = true)
    public static void stats(Player player, @Parameter(name = "target") String name) {
        if (Bukkit.getPlayer(name) != null) {
            PlayerProfile playerProfile = UHCMeetup.getInstance().getGameManager().getProfile(UUIDCache.getUuid(name));
            if (playerProfile != null) {
                new StatsGUI(playerProfile).openMenu(player);
            }
        } else {
            PlayerCache playerCache = new PlayerCache(name);
            if (playerCache.load()) {
                new StatsGUI(playerCache).openMenu(player);
            } else {
                player.sendMessage(UHCMeetup.getInstance().getLanguage().translate(player, "targetNotExists"));
            }
        }
    }

}

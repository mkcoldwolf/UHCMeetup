package mitw.meetup.commands;

import mitw.meetup.UHCMeetup;
import mitw.meetup.manager.TeamManager;
import mitw.meetup.scenarios.ScenarioMenu;
import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import org.bukkit.entity.Player;

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

    @Command(names = {"stats"})
    public static void stats(Player player, @Parameter(name = "target") Player target) {
        player.sendMessage("§eComing soon!");
    }

}

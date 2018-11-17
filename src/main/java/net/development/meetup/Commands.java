package net.development.meetup;

import net.development.meetup.enums.Status;
import net.development.meetup.options.TeamGUI;
import net.development.meetup.scenarios.SMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vote")) {
            if (!Status.isState(Status.WAITING)) return true;
            new SMenu((Player) sender).o((Player) sender);
        } else if (cmd.getName().equalsIgnoreCase("team")) {
            if (!Status.isState(Status.WAITING)) return true;
            TeamGUI.getInstance().openGUI((Player) sender);
        } else if (cmd.getName().equalsIgnoreCase("lang")) {
            new LanguageGUI().o((Player) sender);
        }
        return false;
    }

}

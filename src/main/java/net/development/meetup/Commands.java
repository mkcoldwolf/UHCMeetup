package net.development.meetup;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.meetup.enums.Status;
import net.development.meetup.options.TeamGUI;
import net.development.meetup.scenarios.SMenu;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg2, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("vote")) {
			if (!Status.isState(Status.WAITING)) return true;
			new SMenu((Player) sender).o((Player) sender);
		} else if (cmd.getName().equalsIgnoreCase("team")) {
			if (!Status.isState(Status.WAITING)) return true;
			TeamGUI.getInstance().openGUI((Player) sender);
		}
		return false;
	}

}

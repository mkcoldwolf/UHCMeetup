package net.development.meetup.board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.board.create.CreateLobbyBoard;
import net.development.meetup.board.create.CreateSoloBoard;
import net.development.meetup.board.create.CreateTeamBoard;
import net.development.meetup.board.create.CreateTeleportBoard;
import net.development.meetup.enums.Status;

public class BoardSetup {
	
	public static void setup() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			ScoreHelper.removeScore(p);
		}switch (Status.getState()) {
		case LOADING:
			break;
		case WAITING:
			new CreateLobbyBoard().runTaskTimerAsynchronously(Main.get(), 0L, 2L);
			break;
		case TELEPORT:
			new CreateTeleportBoard().runTaskTimerAsynchronously(Main.get(), 0L, 2L);
			break;
		case PVP:
			if(Main.TeamMode)new CreateTeamBoard().runTaskTimerAsynchronously(Main.get(), 0L, 2L);
			else new CreateSoloBoard().runTaskTimerAsynchronously(Main.get(), 0L, 2L);
			break;
		case FINISH:
			break;
		}
	}

}

package mitw.meetup.listener;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mitw.meetup.Lang;
import mitw.meetup.UHCMeetup;
import mitw.meetup.board.Board;
import mitw.meetup.manager.ArenaManager;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.scenarios.ScenarioMenu;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import mitw.meetup.enums.GameStatus;
import net.md_5.bungee.api.ChatColor;

@RequiredArgsConstructor
public class JoinListener implements Listener {

	private final UHCMeetup plugin;

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();

		e.setJoinMessage(null);

		plugin.getSidebarManager().getPlayerBoards().put(p.getUniqueId(), new Board(plugin, p, plugin.getSidebarManager().getAdapter()));

		final PlayerProfile uHCPlayerProfile = new PlayerProfile(p);
		plugin.getGameManager().profiles.put(p.getUniqueId(), uHCPlayerProfile);
		uHCPlayerProfile.load();

		switch (GameStatus.get()) {
		case LOADING:
			break;
		case WAITING:
			plugin.getGameManager().setCleanPlayerLobby(p, GameMode.SURVIVAL);
			plugin.getGameManager().sendToLobby(p);
			plugin.getPlayerManager().setIngame(p);
			plugin.getGameManager().checkStart();
			for (final Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(Lang.PREFIX + plugin.getLanguage().translate(pl, "CASTJOIN")
						.replaceAll("<current>", String.valueOf(plugin.getGameManager().players.size())).replaceAll("<player>", p.getDisplayName())
						.replaceAll("<max>", plugin.getConfig().getString("Max-Player")));
			}
			for (int i = 0; i < 30; i++) {
				p.sendMessage(" ");
			}
			final List<String> join = plugin.getLanguage().translateArrays(p, "JOIN");
			for (int run = 0; run < join.size(); run++) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', join.get(run)));
			}
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				new ScenarioMenu().openMenu(p);
				if (!plugin.getGameManager().isBroadcasted()) {
					plugin.getGameManager().openBroadcast();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bungee broadcast meetup-" + UHCMeetup.server + " " + UHCMeetup.TeamMode);
				}
			}, 5);
			break;
		case TELEPORT:
			break;
		case PVP:
			plugin.getPlayerManager().setPlayerSpec(p, false);
			p.teleport(ArenaManager.center);
			break;
		case FINISH:
			plugin.getPlayerManager().setPlayerSpec(p, false);
			p.teleport(ArenaManager.center);
			break;
		}
	}

	@EventHandler
	public void onPlayerLogin(final PlayerLoginEvent e) {
		switch (GameStatus.get()) {
		case LOADING:
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', Lang.LOADING));
			break;
		case TELEPORT:
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', Lang.LOADING));
			break;
		default:
			break;
		}
	}

}

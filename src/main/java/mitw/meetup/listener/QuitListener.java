package mitw.meetup.listener;

import lombok.RequiredArgsConstructor;
import mitw.meetup.Lang;
import mitw.meetup.UHCMeetup;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.scenarios.ScenarioMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import mitw.meetup.enums.GameStatus;
import mitw.meetup.manager.TeamManager;

@RequiredArgsConstructor
public class QuitListener implements Listener {

	private final UHCMeetup plugin;

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		plugin.getSidebarManager().getPlayerBoards().remove(p.getUniqueId());
		e.setQuitMessage(null);
		if (GameStatus.is(GameStatus.PVP) && plugin.getGameManager().players.contains(p.getUniqueId())) {
			e.getPlayer().setHealth(0);
		}
		if (plugin.getGameManager().players.contains(p.getUniqueId())) {
			plugin.getGameManager().players.remove(e.getPlayer().getUniqueId());
			for (final Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(Lang.PREFIX + plugin.getInstance().getLanguage().translate(pl, "CASTQUIT")
						.replaceAll("&", "§")
						.replaceAll("<current>", String.valueOf(plugin.getGameManager().players.size()))
						.replaceAll("<player>", p.getDisplayName())
						.replaceAll("<max>", plugin.getInstance().getConfig().getString("Max-Player")));
			}
			if (GameStatus.is(GameStatus.PVP)) {

				plugin.getGameManager().broadcastNoclean();

			}
		}
		plugin.getGameManager().spectators.remove(e.getPlayer().getUniqueId());

        final PlayerProfile player = plugin.getGameManager().getProfile(e.getPlayer().getUniqueId());
        player.save();
        plugin.getGameManager().profiles.remove(e.getPlayer().getUniqueId());

		if (player.getVoted() != null) {
			player.getVoted().setVotes(player.getVoted().getVotes() - 1);
			player.setVoted(null);
			ScenarioMenu.update();
		}
		if (player.getTeam() != null) {
			player.getTeam().members.remove(e.getPlayer().getUniqueId());
			player.setTeam(null, -1);
			TeamManager.getInstance().updateGUIs();
		}

	}

}

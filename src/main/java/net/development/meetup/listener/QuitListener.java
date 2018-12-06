package net.development.meetup.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.options.TeamGUI;
import net.development.meetup.player.UHCPlayer;
import net.development.meetup.scenarios.SMenu;

public class QuitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		ScoreHelper.removeScore(p);
		e.setQuitMessage(null);
		if (Status.isState(Status.PVP) && Main.getGM().players.contains(p.getUniqueId())) {
			e.getPlayer().setHealth(0);
		}
		if (Main.getGM().players.contains(p.getUniqueId())) {
			Main.getGM().players.remove(e.getPlayer().getUniqueId());
			for (final Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(Lang.PREFIX + Main.get().getLang().translate(pl, "CASTQUIT")
						.replaceAll("&", "§")
						.replaceAll("<current>", String.valueOf(Main.getGM().players.size()))
						.replaceAll("<player>", p.getDisplayName())
						.replaceAll("<max>", Main.get().getConfig().getString("Max-Player")));
			}
			if (Status.isState(Status.PVP)) {

				Main.getGM().broadcastNoclean();

			}
		}
		Main.getGM().spectators.remove(e.getPlayer().getUniqueId());
		final UHCPlayer player = Main.getGM().getData.remove(e.getPlayer().getUniqueId());

		if (player.getTeam() != null) {
			player.getTeam().members.remove(e.getPlayer().getUniqueId());
			TeamGUI.getInstance().updateGUI(player.getTeam().id);
			player.setTeam(null, -1);
		}

		SMenu.removePoint(e.getPlayer());

	}

}

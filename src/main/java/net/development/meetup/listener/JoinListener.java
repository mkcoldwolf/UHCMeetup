package net.development.meetup.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.UHCPlayer;
import net.development.meetup.enums.Status;
import net.development.meetup.manager.ArenaManager;
import net.development.meetup.manager.PlayerManager;
import net.development.meetup.scenarios.SMenu;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Main.putPlayerName(p.getUniqueId(), p.getName());
		UHCPlayer uHCPlayer = new UHCPlayer(p);
		Main.getGM().getData.put(p.getUniqueId(), uHCPlayer);
		switch (Status.getState()) {
		case LOADING:
			break;
		case WAITING:
			if (!Main.getGM().debugModePlayers.containsKey(p.getUniqueId())
					&& Lang.dataConfig.getList("debugmodes").contains(p.getUniqueId().toString()))
				Main.getGM().debugModePlayers.put(p.getUniqueId(), true);
			Main.getGM().setCleanPlayerLobby(p, GameMode.SURVIVAL);
			Main.getGM().sendToLobby(p);
			PlayerManager.setPlayerSolo(p);
			Main.getGM().checkStart();
			for (Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(Lang.PREFIX + Main.get().getLang().translate(pl, "CASTJOIN").replaceAll("&", "§")
						.replaceAll("<current>", String.valueOf(Main.getGM().players.size())).replaceAll("<player>", p.getDisplayName())
						.replaceAll("<max>", Main.get().getConfig().getString("Max-Player")));
			}
			e.setJoinMessage(null);
			for (int i = 0; i < 30; i++) {
				p.sendMessage(" ");
			}
			List<String> join = Main.get().getLang().translateArrays(p, "JOIN");
			for (int run = 0; run < join.size(); run++) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', join.get(run)));
			}
			Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
				@Override
				public void run() {
					new SMenu(p).o(p);
					if (!Main.getGM().isBroadcasted()) {
						Main.getGM().openBroadcast();
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bungee broadcast meetup");
					}
				}
			}, 5);
			break;
		case TELEPORT:
			break;
		case PVP:
			PlayerManager.setPlayerSpec(p);
			p.teleport(ArenaManager.center);
			break;
		case FINISH:
			PlayerManager.setPlayerSpec(p);
			p.teleport(ArenaManager.center);
			break;
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		switch (Status.getState()) {
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

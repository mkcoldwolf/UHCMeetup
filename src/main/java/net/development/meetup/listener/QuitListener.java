package net.development.meetup.listener;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.scenarios.SMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        ScoreHelper.removeScore(p);
        e.setQuitMessage(null);
        if (Status.isState(Status.PVP) && Main.getGM().players.contains(p.getUniqueId())) {
            e.getPlayer().setHealth(0);
        }
        if (Main.getGM().players.contains(p.getUniqueId())) {
            Main.getGM().players.remove(e.getPlayer().getUniqueId());
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.sendMessage(Lang.PREFIX + Main.get().getLang().translate(pl, "CASTQUIT")
                        .replaceAll("&", "§")
                        .replaceAll("<current>", String.valueOf(Main.getGM().players.size()))
                        .replaceAll("<player>", p.getDisplayName())
                        .replaceAll("<max>", Main.get().getConfig().getString("Max-Player")));
            }
            if (Main.getGM().players.size() <= 5 && Status.isState(Status.PVP)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String a : Main.get().getLang().translateArrays(player, "ppl5BroadCast")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', a));
                    }
                }
            }
        }
        Main.getGM().spectators.remove(e.getPlayer().getUniqueId());
        Main.getGM().getData.remove(e.getPlayer().getUniqueId());
        SMenu.removePoint(e.getPlayer());

    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        ScoreHelper.removeScore(p);
        if (Status.isState(Status.PVP) && Main.getGM().players.contains(p.getUniqueId())) {
            e.getPlayer().setHealth(0);
        }
        if (Main.getGM().players.contains(p.getUniqueId())) {
            Main.getGM().players.remove(e.getPlayer().getUniqueId());
            if (Main.getGM().players.size() <= 5 && Status.isState(Status.PVP)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String a : Main.get().getLang().translateArrays(player, "ppl5BroadCast")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', a));
                    }
                }
            }
        }
        Main.getGM().spectators.remove(e.getPlayer().getUniqueId());
        Main.getGM().getData.remove(e.getPlayer().getUniqueId());
        SMenu.removePoint(e.getPlayer());
    }

}

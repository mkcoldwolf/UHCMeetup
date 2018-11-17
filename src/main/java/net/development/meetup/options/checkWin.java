package net.development.meetup.options;

import net.development.meetup.Main;
import net.development.meetup.UHCTeam;
import net.development.meetup.task.FireworkTask;
import net.development.meetup.util.PlaySound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class checkWin {

    public static void checkWins() {
        if (Main.TeamMode) {
            if (TeamGUI.getInstance().getLastTeam() != null) {
                UHCTeam win = TeamGUI.getInstance().getLastTeam();
                if (win.p1 != null) {
                    new FireworkTask(win.p1).runTaskTimer(Main.get(), 10L, 20L);
                }
                if (win.p2 != null) {
                    new FireworkTask(win.p2).runTaskTimer(Main.get(), 10L, 20L);
                }
                PlaySound.PlaySoundAll(Sound.WITHER_DEATH);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&l隊伍" + (win.id + 1) + " &b贏了這場遊戲!!"));
                Bukkit.broadcastMessage("§f隊伍成員: §6" + (win.p1 != null ? win.p1.getName() : "") + (win.p2 != null ? win.p1 != null ? ", " + win.p2.getName() : win.p2.getName() : ""));
                Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            Main.getGM().sendToServer(p);
                        }
                        Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.shutdown();
                            }
                        }, 20);
                    }
                }, 300);
            }
            return;
        }
        if (!Main.getGM().players.isEmpty() && Main.getGM().players.size() == 1) {
            Player winner = Bukkit.getPlayer(Main.getGM().players.get(0));
            new FireworkTask(winner).runTaskTimer(Main.get(), 10L, 20L);
            PlaySound.PlaySoundAll(Sound.WITHER_DEATH);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&l" + winner.getName() + " &b贏了這場遊戲!!"));
            Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Main.getGM().sendToServer(p);
                    }
                    Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }, 20);
                }
            }, 300);
        } else if (Main.getGM().players.isEmpty()) {
            Bukkit.shutdown();
        }
    }

}

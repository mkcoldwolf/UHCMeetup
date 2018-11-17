package net.development.meetup.manager;

import net.development.meetup.Main;
import net.development.meetup.UHCTeam;
import net.development.meetup.board.BoardSetup;
import net.development.meetup.border.Config;
import net.development.meetup.border.CoordXZ;
import net.development.meetup.border.WorldFillTask;
import net.development.meetup.enums.Status;
import net.development.meetup.options.generateScatter;
import net.development.meetup.util.Border;
import net.development.meetup.util.Sit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class ArenaManager {

    public static Location center = new Location(Bukkit.getWorld("world"), 0, 120, 0);

    public void sendPlayers(Player player) {
        Main.getGM().setCleanPlayerLobby(player, GameMode.SURVIVAL);
        Location loc = null;
        if (Main.TeamMode) {
            UHCTeam team = Main.getGM().getData.get(player.getUniqueId()).getTeam();
            if (team != null) loc = team.scatter != null ? team.scatter : generateScatter.generateNewScatter();
            else loc = generateScatter.generateNewScatter();
        } else {
            loc = generateScatter.generateNewScatter();
        }
        player.teleport(loc);
        Sit.spawn(player);
        return;
    }

    public void preparegameManager() {

        canWorld(true, true);

    }

    public void canWorld(Boolean b, Boolean b1) {
        if ((b1.booleanValue()) && (b.booleanValue())) {
            new BukkitRunnable() {
                public void run() {
                    createWorld("UHCArena");
                }

            }.runTaskLater(Main.get(), 20L);
            return;
        }
    }

    private void createWorld(String worldName) {
        World uhc = Bukkit.getWorld("world");
        uhc.setPVP(false);
        uhc.setTime(0L);
        uhc.setDifficulty(Difficulty.EASY);
        uhc.setGameRuleValue("doDaylightCycle", "false");
        uhc.setGameRuleValue("naturalRegeneration", "false");
        uhc.setGameRuleValue("doMobSpawning", "false");
        uhc.setSpawnLocation(0, 100, 0);
        uhc.setMonsterSpawnLimit(0);
        uhc.setAnimalSpawnLimit(0);
        Bukkit.getScheduler().runTaskLater(Main.get(), new Runnable() {
            @Override
            public void run() {
                Main.getGM().setBorder(125);
                Border.setWalls("world", 125, true);
                int fillFrequency = 1000;
                int fillPadding = CoordXZ.chunkToBlock(13);
                int ticks = 1, repeats = 1;
                if (fillFrequency > 20)
                    repeats = fillFrequency / 20;
                else
                    ticks = 20 / fillFrequency;
                Config.fillTask = new WorldFillTask(Bukkit.getServer(), null, "world", fillPadding, repeats, ticks);
                if (Config.fillTask.valid()) {
                    int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(),
                            Config.fillTask, ticks, ticks);
                    Config.fillTask.setTaskID(task);
                } else
                    Bukkit.getConsoleSender().sendMessage("The world map generation task failed to start.");
            }
        }, 20L);
    }

    public void unloadWorld(World world) {
        if (world != null) {
            for (Player p : world.getPlayers()) {
                p.teleport(((World) Bukkit.getWorlds().get(0)).getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(world, false);
        }
    }

    public void deleteWorld(String world) {
        File filePath = new File(Bukkit.getWorldContainer(), world);
        deleteFiles(filePath);
    }

    public boolean deleteFiles(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            File[] arrayOfFile1;
            int j = (arrayOfFile1 = files).length;
            for (int i = 0; i < j; i++) {
                File file = arrayOfFile1[i];
                if (file.isDirectory()) {
                    deleteFiles(file);
                } else {
                    file.delete();
                }
            }
        }
        return path.delete();
    }

    public void deleteFolder(File file, boolean bl) {
        File[] arrfile = file.listFiles();
        boolean bl2 = true;
        if (arrfile != null) {
            File[] arrfile2 = arrfile;
            int n = arrfile2.length;
            int n2 = 0;
            while (n2 < n) {
                File file2 = arrfile2[n2];
                if (file2.isDirectory()) {
                    deleteFolder(file2, bl);
                } else if ((!file2.getName().contains("session.lock")) || (bl)) {
                    file2.delete();
                } else {
                    bl2 = false;
                }
                n2++;
            }
        }
        if (bl2) {
            file.delete();
        }
    }

    public static void finishLoad(World uhc) {
        Status.setState(Status.WAITING);
        BoardSetup.setup();
        Main.getGM().spawn = new Location(uhc, 0, uhc.getHighestBlockYAt(0, 0) + 1, 0);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.GREEN + "MitwMeetup load world finished");
        Bukkit.broadcastMessage(" ");
        for (Chunk c : uhc.getLoadedChunks()) {
            for (org.bukkit.entity.Entity entity : c.getEntities()) {
                entity.remove();
            }
        }
    }

}

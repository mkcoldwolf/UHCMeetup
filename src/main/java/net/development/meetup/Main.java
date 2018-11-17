package net.development.meetup;

import net.development.meetup.border.WBListener;
import net.development.meetup.border.barrier.VisualRunnable;
import net.development.meetup.enums.Status;
import net.development.meetup.listener.*;
import net.development.meetup.manager.ArenaManager;
import net.development.meetup.manager.GameManager;
import net.development.meetup.manager.KitManager;
import net.development.meetup.util.MitwLanguage;
import net.development.meetup.util.MitwLanguage.LangType;
import net.development.meetup.util.MitwLanguage.SQLConnection;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author LeeGod
 * @version 1.0.0
 * @supporter LU__LU
 */
public class Main extends JavaPlugin {

    public static final Map<UUID, String> playerName = new HashMap<>();
    private static Main ins;
    private static GameManager gm;
    private static ArenaManager am;
    private static MitwLanguage lang;
    private static KitManager km;
    public static List<Material> canbreak = new ArrayList<>();
    public static boolean TeamMode;
    private PluginManager pm = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        ins = this;
        TeamMode = getConfig().getBoolean("Team");
        this.RegisterListener();
        this.RegisterManager();
        this.loadConfig();
        Status.setState(Status.LOADING);
        addBlocks();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "RedisBungee");
        KitManager.setListItem();
        am.preparegameManager();
        Commands cmd = new Commands();
        this.getCommand("vote").setExecutor(cmd);
        if (TeamMode)
            this.getCommand("team").setExecutor(cmd);
        this.getCommand("lang").setExecutor(cmd);
        VisualRunnable.init(this);
        SQLConnection conn = new SQLConnection("127.0.0.1", 3306, "root", "mitwsdriverpass", "language");
        if (!conn.connect()) {
            Bukkit.broadcastMessage(":(");
        }
        lang = new MitwLanguage(LangType.CLASS, this, conn, new Lang());
    }

    @Override
    public void onDisable() {
        ArrayList<String> temp = new ArrayList<>();
        for (UUID u : getGM().debugModePlayers.keySet()) {
            if (getGM().debugModePlayers.get(u) == true)
                temp.add(u.toString());
        }
        Lang.dataConfig.set("debugmodes", temp);
        try {
            Lang.dataConfig.save(Lang.data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void putPlayerName(UUID uuid, String name) {
        playerName.put(uuid, name);
    }

    public static String getPlayerName(UUID uuid) {
        return playerName.get(uuid);
    }

    public boolean exists(String s) {
        File localFile = new File(s);
        return localFile.exists();
    }

    public void loadConfig() {
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
    }

    public void RegisterManager() {
        gm = new GameManager();
        am = new ArenaManager();
        km = new KitManager();
    }

    public void RegisterListener() {
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new GameListener(), this);
        pm.registerEvents(new WBListener(), this);
        pm.registerEvents(new QuitListener(), this);
        pm.registerEvents(new DeathListener(), this);
        if (TeamMode)
            pm.registerEvents(new GUIListener(), this);
    }

    public List<UUID> getOnlinePlayers() {
        List<UUID> list = new ArrayList<>();
        list.addAll(gm.players);
        list.addAll(gm.spectators);
        return list;
    }

    public static Main get() {
        return ins;
    }

    public static GameManager getGM() {
        return gm;
    }

    public static ArenaManager getAM() {
        return am;
    }

    public static KitManager getKM() {
        return km;
    }

    public MitwLanguage getLang() {
        return lang;
    }

    public static String Log(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.getConsoleSender().sendMessage(message);
        return message;
    }

    public void addBlocks() {
        canbreak.add(Material.LONG_GRASS);
        canbreak.add(Material.YELLOW_FLOWER);
        canbreak.add(Material.WOOD);
        canbreak.add(Material.LEAVES);
        canbreak.add(Material.LEAVES_2);
        canbreak.add(Material.DEAD_BUSH);
        canbreak.add(Material.WEB);
        canbreak.add(Material.COBBLESTONE);
        canbreak.add(Material.DOUBLE_PLANT);
        canbreak.add(Material.LOG);
        canbreak.add(Material.LOG_2);
        canbreak.add(Material.OBSIDIAN);
    }

}

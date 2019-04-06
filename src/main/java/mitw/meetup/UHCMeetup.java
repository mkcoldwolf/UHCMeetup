package mitw.meetup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import mitw.meetup.board.BoardManager;
import mitw.meetup.board.adapter.UHCMeetupAdapter;
import mitw.meetup.impl.NocleanTimer;
import mitw.meetup.manager.*;
import mitw.meetup.player.Rank;
import net.development.mitw.commands.CommandHandler;
import net.development.mitw.utils.FastRandom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mitw.meetup.border.worldborder.WBListener;
import mitw.meetup.border.barrier.VisualRunnable;
import mitw.meetup.enums.GameStatus;
import mitw.meetup.listener.DeathListener;
import mitw.meetup.listener.GameListener;
import mitw.meetup.listener.JoinListener;
import mitw.meetup.listener.QuitListener;
import net.development.mitw.Mitw;
import net.development.mitw.language.LanguageAPI;

@Getter
public class UHCMeetup extends JavaPlugin {

	@Getter
	private static UHCMeetup instance;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private PlayerManager playerManager;
	private LanguageAPI language;
	private KitManager kitManager;
	public static List<Material> canbreak = new ArrayList<>();
	public static boolean TeamMode;
	public static String serverName;
	public static int server;

	@Getter
	private BoardManager sidebarManager;

	@Getter
	private LeaderboardManager leaderboardManager;

	@Getter
	private static FastRandom random = new FastRandom();

	@Override
	public void onEnable() {

		instance = this;

		TeamMode = getConfig().getBoolean("Team");
		serverName = getConfig().getString("ServerName");
		server = getConfig().getInt("Server");

		this.registerListeners();
		this.loadConfig();

		this.gameManager = new GameManager();
		this.arenaManager = new ArenaManager();
		this.kitManager = new KitManager();
		this.playerManager = new PlayerManager(this);
		this.setBoardManager(new BoardManager(this, new UHCMeetupAdapter()));

		GameStatus.set(GameStatus.LOADING);

		addBlocks();

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		KitManager.setListItem();
		arenaManager.preparegameManager();

		VisualRunnable.init(this);
		language = new LanguageAPI(LanguageAPI.LangType.CLASS, this, Mitw.getInstance().getLanguageData(), new Lang());
		CommandHandler.loadCommandsFromPackage(this, "mitw.meetup.commands");

		Mitw.getInstance().getTimerManager().registerTimer(new NocleanTimer());

		this.leaderboardManager = new LeaderboardManager();

	}

	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			getGameManager().getProfile(player.getUniqueId()).save();
		}
	}

	private void loadConfig() {
		this.getConfig().options().copyDefaults();
		this.saveDefaultConfig();
		Lang.setupFile();
		this.loadRanks();
	}

	private void loadRanks() {
		for (String key : Lang.rankConfig.getKeys(false)) {
			new Rank(Lang.rankConfig.getConfigurationSection(key));
		}
	}

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new GameListener(this), this);
		pm.registerEvents(new WBListener(), this);
		pm.registerEvents(new QuitListener(this), this);
		pm.registerEvents(new DeathListener(this), this);
	}

	public List<UUID> getOnlinePlayers() {
		final List<UUID> list = new ArrayList<>();
		list.addAll(gameManager.players);
		list.addAll(gameManager.spectators);
		return list;
	}

	private void addBlocks() {
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
		canbreak.add(Material.ANVIL);
	}

	private void setBoardManager(final BoardManager manager) {
		this.sidebarManager = manager;
		this.sidebarManager.runTaskTimerAsynchronously(this, manager.getAdapter().getInterval(), manager.getAdapter().getInterval());
	}

}

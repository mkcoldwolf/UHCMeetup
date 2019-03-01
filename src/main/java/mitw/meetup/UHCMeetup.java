package mitw.meetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import mitw.meetup.board.BoardManager;
import mitw.meetup.board.adapter.UHCMeetupAdapter;
import mitw.meetup.impl.NocleanTimer;
import mitw.meetup.manager.PlayerManager;
import net.development.mitw.commands.CommandHandler;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mitw.meetup.border.worldborder.WBListener;
import mitw.meetup.border.barrier.VisualRunnable;
import mitw.meetup.enums.GameStatus;
import mitw.meetup.listener.DeathListener;
import mitw.meetup.listener.GameListener;
import mitw.meetup.listener.JoinListener;
import mitw.meetup.listener.QuitListener;
import mitw.meetup.manager.ArenaManager;
import mitw.meetup.manager.GameManager;
import mitw.meetup.manager.KitManager;
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

	}

	@Override
	public void onDisable() {
		final ArrayList<String> temp = new ArrayList<>();
		for (final UUID u : gameManager.debugModePlayers.keySet()) {
			if (gameManager.debugModePlayers.get(u)) {
				temp.add(u.toString());
			}
		}
		Lang.dataConfig.set("debugmodes", temp);
		try {
			Lang.dataConfig.save(Lang.data);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() {
		this.getConfig().options().copyDefaults();
		this.saveDefaultConfig();
		Lang.setupFile();
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

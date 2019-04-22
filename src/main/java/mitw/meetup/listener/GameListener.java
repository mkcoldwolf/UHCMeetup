package mitw.meetup.listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import mitw.meetup.Lang;
import mitw.meetup.UHCMeetup;
import mitw.meetup.gui.SimpleInventorySnapshot;
import mitw.meetup.gui.StatsGUI;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ilummc.tlib.util.Strings;

import mitw.meetup.enums.GameStatus;
import net.development.mitw.utils.BukkitUtil;
import net.md_5.bungee.api.ChatColor;

public class GameListener implements Listener {

	private static List<UUID> using = new ArrayList<>();
	
	private UHCMeetup plugin;
	
	public GameListener(UHCMeetup plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void noBreakBlock(final BlockBreakEvent e) {
		if (!GameStatus.is(GameStatus.PVP)) {
			e.setCancelled(true);
			return;
		}
		if (plugin.getGameManager().spectators.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
		if (!(UHCMeetup.canbreak.contains(e.getBlock().getType()))) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(plugin.getLanguage().translate(e.getPlayer(), "NOBREAK"));
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent e) {
		if (plugin.getGameManager().spectators.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
		if (!GameStatus.is(GameStatus.PVP)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawn(final CreatureSpawnEvent e) {
		if (!e.getSpawnReason().equals(SpawnReason.NATURAL))
			return;
		e.getEntity().remove();
		e.setCancelled(true);
	}

	@EventHandler
	public void onClick(final PlayerInteractEvent e) {

		if (!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)
				&& !e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		if (GameStatus.is(GameStatus.TELEPORT))
			return;

		if (plugin.getGameManager().spectators.contains(e.getPlayer().getUniqueId()) || GameStatus.is(GameStatus.WAITING)) {
			e.setCancelled(true);
		} else
			return;

		if (e.getItem() == null || e.getItem().getType().equals(Material.AIR))
			return;
		final Player p = e.getPlayer();

		if (e.getItem().getType() == Material.PAINTING) {

			p.chat("/vote");

		} else if (e.getItem().getType() == Material.SLIME_BALL) {

			if (!plugin.getGameManager().spectators.contains(p.getUniqueId())) {
				p.getInventory().remove(e.getItem());
				return;
			}
			if (using.contains(p.getUniqueId())) {
				p.sendMessage(plugin.getLanguage().translate(p, "wait"));
				return;
			}
			using.add(p.getUniqueId());
			final Random rand = new Random();
			final List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
			plugin.getGameManager().spectators.forEach(u -> players.remove(Bukkit.getPlayer(u)));
			final Player target = players.get(rand.nextInt(players.size()));
			p.teleport(target);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> using.remove(p.getUniqueId()), 60l);

		} else if (e.getItem().getType() == Material.BED) {
			plugin.getGameManager().sendToServer(p);
			if (using.contains(p.getUniqueId())) {
				p.sendMessage(plugin.getLanguage().translate(p, "wait"));
				return;
			}
			using.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> using.remove(p.getUniqueId()), 60l);
		} else if (e.getItem().getType() == Material.GOLD_SWORD) {
			p.chat("/team");
			if (using.contains(p.getUniqueId())) {
				p.sendMessage(plugin.getLanguage().translate(p, "wait"));
				return;
			}
			using.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> using.remove(p.getUniqueId()), 60l);
		} else if (e.getItem().getType() == Material.DIAMOND_SWORD) {
			plugin.getGameManager().sendToPractice(p);
			if (using.contains(p.getUniqueId())) {
				p.sendMessage(plugin.getLanguage().translate(p, "wait"));
				return;
			}
			using.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> using.remove(p.getUniqueId()), 60l);
		} else if (e.getItem().getType() == Material.INK_SACK) {

			if (using.contains(p.getUniqueId())) {
				p.sendMessage(plugin.getLanguage().translate(p, "wait"));
				return;
			}
			using.add(p.getUniqueId());
			plugin.getPlayerManager().setPlayerDBMode(p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> using.remove(p.getUniqueId()), 60l);
		} else if (e.getItem().getType() == Material.SIGN) {
            if (using.contains(p.getUniqueId())) {
                p.sendMessage(plugin.getLanguage().translate(p, "wait"));
                return;
            }
            using.add(p.getUniqueId());
            PlayerProfile playerProfile = plugin.getGameManager().getProfile(p.getUniqueId());
            new StatsGUI(playerProfile).openMenu(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> using.remove(p.getUniqueId()), 60l);
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if (plugin.getGameManager().spectators.contains(e.getWhoClicked().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void eatHead(final PlayerItemConsumeEvent e) {
		final Player player = e.getPlayer();
		if (plugin.getGameManager().spectators.contains(player.getUniqueId())) {
			e.setCancelled(true);
		}
		if (e.getItem().getType().equals(Material.GOLDEN_APPLE) && e.getItem().getItemMeta().hasDisplayName()
				&& e.getItem().getItemMeta().getDisplayName().contains(Lang.headName)) {
			final PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1);
			final PotionEffect abs = new PotionEffect(PotionEffectType.ABSORPTION, 20 * 120, 0);
			regen.apply(player);
			abs.apply(player);
		}
	}

	@EventHandler
	public void noStartnoBow(final EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player && !(GameStatus.is(GameStatus.PVP))) {
			e.setCancelled(true);
			e.getBow().setDurability((short) 0);
		} else if (e.getEntity() instanceof Player && Scenario.BowLess.isActive()) {
			e.setCancelled(true);
			((Player) e.getEntity()).sendMessage(plugin.getLanguage().translate((Player) e.getEntity(), "bowLess"));
		}
	}

	@EventHandler
	public void noStartnoDrop(final PlayerDropItemEvent e) {
		if (plugin.getGameManager().spectators.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
		if (!GameStatus.is(GameStatus.PVP)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickUp(final PlayerPickupItemEvent e) {
		if (plugin.getGameManager().spectators.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		if (plugin.getGameManager().spectators.contains(((Player) e.getEntity()).getUniqueId())) {
			e.setCancelled(true);
		}
		if (!GameStatus.is(GameStatus.PVP)) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onSkyBase(final BlockPlaceEvent e) {
		if (plugin.getGameManager().spectators.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
		if (e.getBlock().getLocation().getBlockY() > 80) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§cSkyBase是禁止的喔");
		}
	}

	@EventHandler
	public void noCraftWorkBench(final CraftItemEvent e) {
		if (plugin.getGameManager().spectators.contains(e.getViewers().get(0).getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onClickToEntity(final PlayerInteractEntityEvent e) {
		final Player p = e.getPlayer();
		if (plugin.getGameManager().spectators.contains(p.getUniqueId())) {
			e.setCancelled(true);
			final Entity en = e.getRightClicked();
			if (en != null && en instanceof Player && !plugin.getGameManager().spectators.contains(((Player) en).getUniqueId())) {
				final Player target = (Player) en;
				new SimpleInventorySnapshot(target).openMenu(p);
			}
		}
	}

	private final DecimalFormat format = new DecimalFormat("0.0");

	@EventHandler
	public void onDamageByEntity(final EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			if (e.getDamager() instanceof Player
					&& plugin.getGameManager().spectators.contains(((Player) e.getDamager()).getUniqueId())) {
				e.setCancelled(true);
			}
			return;
		}
		final Player p = (Player) e.getEntity();
		final PlayerProfile profile = plugin.getGameManager().getProfile(p.getUniqueId());
		if (plugin.getGameManager().spectators.contains(p.getUniqueId())) {
			e.setCancelled(true);
			return;
		}
		if (profile.isNoClean()) {
			e.setCancelled(true);
			return;
		}

		final Player k = BukkitUtil.getDamager(e);
		if (k == null)
			return;

		if (plugin.getGameManager().spectators.contains(k.getUniqueId())) {
			e.setCancelled(true);
			return;
		}

		if (UHCMeetup.TeamMode) {

			final PlayerProfile kp = plugin.getGameManager().getProfile(k.getUniqueId());

			if (kp.getTeam() != null && profile.getTeam().equals(kp.getTeam())
					&& (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow)) {

				e.setCancelled(true);
				return;

			}

		}

		if (plugin.getGameManager().getProfile(k.getUniqueId()).isNoClean()) {
			plugin.getGameManager().getProfile(k.getUniqueId()).setNoClean(false);
			k.sendMessage(plugin.getLanguage().translate(k, "noCleanStop"));
		}

		if (!(e.getDamager() instanceof Arrow))
			return;

		double damage = e.getFinalDamage();
		double absorptionHealth = ((CraftPlayer)p).getHandle().getAbsorptionHearts();
		final double absorptionDamage = Math.ceil(absorptionHealth - damage) / 2.0;

		if (absorptionDamage > 0.0D) {
			absorptionHealth = absorptionDamage;
			damage = 0.0;
		} else {
			damage -= absorptionHealth;
			absorptionHealth = 0.0;
		}

		final double health = Math.ceil(((Player) e.getEntity()).getHealth() - damage) / 2.0D;

		k.sendMessage(Strings.replaceWithOrder(plugin.getLanguage().translate(k, "arrowDamage"), p.getName(), format.format(health) + "\u2764", format.format(absorptionHealth) + "\u2764"));
	}

	@EventHandler
	public void onSpawnEgg(final PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getPlayer().getItemInHand().equals(new ItemStack(Material.MONSTER_EGG, 1, (byte) 100))) {
				e.setCancelled(true);
				if (!GameStatus.is(GameStatus.PVP))
					return;
				e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
				final Horse horse = (Horse) Bukkit.getWorld("world").spawnEntity(e.getPlayer().getLocation(),
						EntityType.HORSE);
				horse.setTamed(true);
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
				horse.setAdult();
				horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
				horse.setJumpStrength(0.5);
				horse.setMaxHealth(30.0);
				horse.setHealth(30.0);
				horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));
				horse.setPassenger(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPing(final ServerListPingEvent e) {
		switch (GameStatus.get()) {
		case LOADING:
			e.setMotd(ChatColor.RED + "讀取地圖中...");
			break;
		case WAITING:
			e.setMotd(ChatColor.GREEN + "等待玩家 快進來吧!");
			break;
		case TELEPORT:
			e.setMotd(ChatColor.BLUE + "傳送玩家中..");
			break;
		case PVP:
			e.setMotd(ChatColor.BLUE + "遊戲中...");
			break;
		case FINISH:
			e.setMotd(ChatColor.RED + "結束中..");
			break;
		}
	}

	@EventHandler
	public void onRod(final ProjectileLaunchEvent e) {
		if (!GameStatus.is(GameStatus.PVP)) {
			e.setCancelled(true);
			return;
		}
		if (Scenario.RodLess.isActive()) {
			if (e.getEntity() instanceof FishHook && e.getEntity().getShooter() instanceof Player) {
				final Player p = (Player) e.getEntity().getShooter();
				p.sendMessage("§cRodLess 是開啟的! 不能使用釣竿!");
				p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
				p.getInventory().remove(p.getItemInHand());
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFire(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player
				&& (Scenario.FireLess.isActive() || plugin.getGameManager().getProfile(e.getEntity().getUniqueId()).isNoClean())
				&& (e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.LAVA)
						|| e.getCause().equals(DamageCause.FIRE_TICK))) {
			e.setCancelled(true);
		}
	}
}

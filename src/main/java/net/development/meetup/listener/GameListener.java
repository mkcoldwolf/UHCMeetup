package net.development.meetup.listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.enums.Status;
import net.development.meetup.manager.KitManager;
import net.development.meetup.manager.PlayerManager;
import net.development.meetup.scenarios.ScenariosEnable;
import net.development.meetup.util.SpecInv;
import net.md_5.bungee.api.ChatColor;

public class GameListener implements Listener {
	
	private static List<UUID> using = new ArrayList<>();

	@EventHandler
	public void noBreakBlock(BlockBreakEvent e) {
		if (!Status.isState(Status.PVP)) {
			e.setCancelled(true);
			return;
		}
		if (Main.getGM().spectators.contains(e.getPlayer().getUniqueId()))
			e.setCancelled(true);
		if (!(Main.canbreak.contains(e.getBlock().getType()))) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Main.get().getLang().translate(e.getPlayer(), "NOBREAK"));
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (Main.getGM().spectators.contains(e.getPlayer().getUniqueId()))
			e.setCancelled(true);
		if (!Status.isState(Status.PVP)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawn(CreatureSpawnEvent e) {
		if (!e.getSpawnReason().equals(SpawnReason.NATURAL))
			return;
		e.getEntity().remove();
		e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.LEFT_CLICK_AIR) &&
				!e.getAction().equals(Action.LEFT_CLICK_BLOCK) &&
				!e.getAction().equals(Action.RIGHT_CLICK_AIR) &&
				!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (Status.isState(Status.TELEPORT)) {
			return;
		}
		if (Main.getGM().spectators.contains(e.getPlayer().getUniqueId()) || !Status.isState(Status.PVP))
			e.setCancelled(true);
		if (e.getItem() == null || e.getItem().getType().equals(Material.AIR))
			return;
		Player p = e.getPlayer();
		if (e.getItem().equals(KitManager.vote)) {
			p.performCommand("vote");
		} else if(e.getItem().equals(KitManager.lang)) {
			p.performCommand("lang");
		} else if (e.getItem().equals(KitManager.spec1)) {
			if (!Main.getGM().spectators.contains(p.getUniqueId())) {
				p.getInventory().remove(e.getItem());
				return;
			}
			if(using.contains(p.getUniqueId())) {
				p.sendMessage("§c冷卻中...");
				return;
			}
			using.add(p.getUniqueId());
			Random rand = new Random();
			List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
			Main.getGM().spectators.forEach(u -> players.remove(Bukkit.getPlayer(u)));
			Player target = players.get(rand.nextInt(players.size()));
			p.teleport(target);
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					using.remove(p.getUniqueId());
				}
			}, 60l);
		} else if (e.getItem().equals(KitManager.spec2)) {
			Main.getGM().sendToServer(p);
			if(using.contains(p.getUniqueId())) {
				p.sendMessage("§c冷卻中...");
				return;
			}
			using.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					using.remove(p.getUniqueId());
				}
			}, 60l);
		} else if(e.getItem().equals(KitManager.team)) {
			p.performCommand("team");
			if(using.contains(p.getUniqueId())) {
				p.sendMessage("§c冷卻中...");
				return;
			}
			using.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					using.remove(p.getUniqueId());
				}
			}, 60l);
		}else if(e.getItem().equals(KitManager.retunToArenaPvP)) {
			Main.getGM().sendToPractice(p);
			if(using.contains(p.getUniqueId())) {
				p.sendMessage("§c冷卻中...");
				return;
			}
			using.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					using.remove(p.getUniqueId());
				}
			}, 60l);
		}else if(e.getItem().equals(KitManager.DbOn) || e.getItem().equals(KitManager.DbOff)) {
			
			if(using.contains(p.getUniqueId())) {
				p.sendMessage("§c冷卻中...");
				return;
			}
			using.add(p.getUniqueId());
			PlayerManager.setPlayerDBMode(p);
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					using.remove(p.getUniqueId());
				}
			}, 60l);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (Main.getGM().spectators.contains(e.getWhoClicked().getUniqueId()))
			e.setCancelled(true);
	}

	@EventHandler
	public void eatHead(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		if (Main.getGM().spectators.contains(player.getUniqueId()))
			e.setCancelled(true);
		if (e.getItem().getType().equals(Material.GOLDEN_APPLE) && e.getItem().getItemMeta().hasDisplayName()
				&& e.getItem().getItemMeta().getDisplayName().contains(Lang.headName)) {
			PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1);
			PotionEffect abs = new PotionEffect(PotionEffectType.ABSORPTION, 20 * 120, 0);
			regen.apply(player);
			abs.apply(player);
		}
	}

	@EventHandler
	public void noStartnoBow(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player && !(Status.isState(Status.PVP))) {
			e.setCancelled(true);
			e.getBow().setDurability((short) 0);
		} else if (e.getEntity() instanceof Player && ScenariosEnable.BowLessE) {
			e.setCancelled(true);
			((Player) e.getEntity()).sendMessage(Main.get().getLang().translate((Player)e.getEntity(), "bowLess"));
		}
	}

	@EventHandler
	public void noStartnoDrop(PlayerDropItemEvent e) {
		if (Main.getGM().spectators.contains(e.getPlayer().getUniqueId()))
			e.setCancelled(true);
		if (!Status.isState(Status.PVP)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {
		if (Main.getGM().spectators.contains(e.getPlayer().getUniqueId()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (Main.getGM().spectators.contains(((Player) e.getEntity()).getUniqueId()))
			e.setCancelled(true);
		if (!Status.isState(Status.PVP)) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onSkyBase(BlockPlaceEvent e) {
		if (Main.getGM().spectators.contains(e.getPlayer().getUniqueId()))
			e.setCancelled(true);
		if (e.getBlock().getLocation().getBlockY() > 80) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§cSkyBase是禁止的喔");
		}
	}

	@EventHandler
	public void noCraftWorkBench(CraftItemEvent e) {
		if (Main.getGM().spectators.contains(e.getViewers().get(0).getUniqueId()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onClickToEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (Main.getGM().spectators.contains(p.getUniqueId())) {
			e.setCancelled(true);
			Entity en = e.getRightClicked();
			if (en != null && en instanceof Player && !Main.getGM().spectators.contains(((Player) en).getUniqueId())) {
				Player target = (Player) en;
				new SpecInv(target).o(p);
			}
		}
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			if (e.getDamager() instanceof Player
					&& Main.getGM().spectators.contains(((Player) e.getDamager()).getUniqueId())) {
				e.setCancelled(true);
			}
			return;
		}
		Player p = (Player) e.getEntity();
		if (Main.getGM().spectators.contains(p.getUniqueId())) {
			e.setCancelled(true);
			return;
		}
		if (Main.getGM().getData.get(e.getEntity().getUniqueId()).isNoClean()) {
			e.setCancelled(true);
			return;
		}
		if (e.getDamager() instanceof Player) {
			Player k = (Player) e.getDamager();
			if (Main.getGM().spectators.contains(k.getUniqueId())) {
				e.setCancelled(true);
				return;
			}
			if (Main.getGM().getData.get(k.getUniqueId()).isNoClean()) {
				Main.getGM().getData.get(k.getUniqueId()).setNoClean(false);
				k.sendMessage(Main.get().getLang().translate(k, "noCleanStop"));
			}
			return;
		}
		if (!(e.getDamager() instanceof Arrow))
			return;
		Arrow a = (Arrow) e.getDamager();
		if (a.getShooter() instanceof Player) {
			new BukkitRunnable() {
				@Override
				public void run() {
					((Player) a.getShooter()).sendMessage(Main.get().getLang().translate((Player) a.getShooter(), "bow").replace("<player>", p.getName()).replace("<health>", new DecimalFormat("0.00").format(p.getHealth() / 2) + "\u2764"));
				}
			}.runTaskLaterAsynchronously(Main.get(), 2L);
		}
	}

	@EventHandler
	public void onSpawnEgg(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getPlayer().getItemInHand().equals(new ItemStack(Material.MONSTER_EGG, 1, (byte) 100))) {
				e.setCancelled(true);
				if (!Status.isState(Status.PVP)) {
					return;
				}
				e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
				Horse horse = (Horse) Bukkit.getWorld("world").spawnEntity(e.getPlayer().getLocation(),
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
	public void onPing(ServerListPingEvent e) {
		switch (Status.getState()) {
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
	public void onRod(ProjectileLaunchEvent e) {
		if (!Status.isState(Status.PVP)) {
			e.setCancelled(true);
			return;
		}
		if (ScenariosEnable.RodLessE) {
			if (e.getEntity() instanceof FishHook && e.getEntity().getShooter() instanceof Player) {
				Player p = (Player) e.getEntity().getShooter();
				p.sendMessage("§cRodLess 是開啟的! 不能使用釣竿!");
				p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
				p.getInventory().remove(p.getItemInHand());
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFire(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && (ScenariosEnable.FireLessE || Main.getGM().getData.get(e.getEntity().getUniqueId()).isNoClean())
				&& (e.getCause().equals(DamageCause.FIRE)
				|| e.getCause().equals(DamageCause.LAVA)
				|| e.getCause().equals(DamageCause.FIRE_TICK))) {
			e.setCancelled(true);
		}
	}
}

package net.development.meetup.options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.development.meetup.player.UHCTeam;
import net.development.meetup.util.ItemBuilder;
import net.development.mitw.uuid.UUIDCache;

public class TeamGUI {

	private static TeamGUI ins;
	public List<UHCTeam> teams = new ArrayList<>();
	public Inventory gui = Bukkit.createInventory(null, 3 * 9, "§e選擇隊伍!");

	public TeamGUI() {
		for (int i = 0; i <= 27; i++) {
			gui.addItem(ItemBuilder.createItem1(Material.WOOL, 1, 0, "§e隊伍 " + (i + 1), "§7人數: §60/2"));
			teams.add(new UHCTeam(null, null, i));
		}
	}

	public static TeamGUI getInstance() {
		if (ins == null) {
			ins = new TeamGUI();
		}
		return ins;
	}

	public void openGUI(final Player p) {
		p.openInventory(gui);
	}

	public void updateGUI(final int team) {
		final UHCTeam uteam = teams.get(team);
		int size = 0;
		if (uteam.p1 != null) {
			size++;
		}
		if (uteam.p2 != null) {
			size++;
		}
		if (size == 2) {
			gui.setItem(team, ItemBuilder.createItem1(Material.WOOL, 1, 14, "§e隊伍 " + (team + 1), "§7人數: §6" + size + "/2", "§7- §e" + UUIDCache.getName(uteam.p1), "§7- §e" + UUIDCache.getName(uteam.p2)));
		} else if (size == 1) {
			gui.setItem(team, ItemBuilder.createItem1(Material.WOOL, 1, 5, "§e隊伍 " + (team + 1), "§7人數: §6" + size + "/2", "§7- §e" + (uteam.p1 != null ? UUIDCache.getName(uteam.p1) : uteam.p2 != null ? UUIDCache.getName(uteam.p2) : "")));
		} else {
			gui.setItem(team, ItemBuilder.createItem1(Material.WOOL, 1, 0, "§e隊伍 " + (team + 1), "§7人數: §6" + size + "/2"));
		}
	}

	public int getTeamAlive() {
		int alive = 0;
		for (final UHCTeam team : teams) {
			if (team.isAlive()) {
				alive++;
			}
		}
		return alive;
	}

	public UHCTeam getLastTeam() {
		if (getTeamAlive() <= 1) {
			for (final UHCTeam team : teams) {
				if (team.isAlive()) return team;
			}
		}
		return null;
	}

}

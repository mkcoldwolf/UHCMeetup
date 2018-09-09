package net.development.meetup.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.development.meetup.Lang;
import net.development.meetup.scenarios.ScenariosEnable;
import net.development.meetup.util.ItemBuilder;

public class KitManager {
	public static ItemStack vote = ItemBuilder.createItem1(Material.PAINTING, 1, 0, Lang.vote, "&7點我進行投票!");
	public static ItemStack lang = ItemBuilder.createItem1(Material.FEATHER, 1, 0, "&9語言&7(Language)", "&7點我選擇語言!");
	public static ItemStack spec1 = ItemBuilder.createItem1(Material.SLIME_BALL, 1, 0, Lang.spec1, "&7點我隨機傳送到一個玩家身邊!");
	public static ItemStack spec2 = ItemBuilder.createItem1(Material.BED, 1, 0, Lang.spec2,
			"&7點我回到大廳!&7(Return to Hub)");
	public static ItemStack retunToArenaPvP = ItemBuilder.createItem1(Material.DIAMOND_SWORD, 1, 0,
			Lang.reTurnToPractice, "&6回到對戰分流&7(Return to Practice)");
	public static ItemStack team = ItemBuilder.createItem1(Material.GOLD_SWORD, 1, 0, "&6隊伍&f選擇", "&7點我選擇隊伍!");
	public static ArrayList<Material> diamondArmors = new ArrayList<>();
	public static ArrayList<Material> sword = new ArrayList<>();
	private HashMap<UUID, Integer> swordlevel = new HashMap<>();

	public void setItem(Player player) {
		Inventory i = player.getInventory();
		i.clear();
		int ran = (int) ((Math.random() * 10));
		i.addItem(swordEnchant(player, new ItemStack(sword.get(ran)), Enchantment.DAMAGE_ALL, 3));
		i.addItem(new ItemStack(Material.FISHING_ROD));
		i.addItem(randomEnchant(new ItemStack(Material.BOW), Enchantment.ARROW_DAMAGE, 3));
		i.addItem(new ItemStack(Material.LAVA_BUCKET));
		i.setItem(30, new ItemStack(Material.LAVA_BUCKET));
		i.addItem(new ItemStack(Material.WATER_BUCKET));
		i.setItem(31, new ItemStack(Material.WATER_BUCKET));
		i.addItem(new ItemStack(Material.COBBLESTONE, 64));
		i.setItem(32, new ItemStack(Material.COBBLESTONE, 64));
		i.addItem(new ItemStack(Material.GOLDEN_APPLE, (new Random().nextInt(7) + 3)));
		i.addItem(goldenHead(new Random().nextInt(3) + 2));
		i.addItem(new ItemStack(Material.DIAMOND_PICKAXE));
		i.addItem(new ItemStack(Material.COOKED_BEEF, 64));
		i.addItem(new ItemStack(Material.ARROW, 32));
		i.addItem(new ItemStack(Material.ANVIL, 3));
		if (new Random().nextInt(100) < 50)
			i.addItem(new ItemStack(Material.WEB, 3));

		if (new Random().nextInt(100) < 20)
			i.addItem(new ItemStack(Material.POTION, 1, (byte) 8226));
		else if (new Random().nextInt(100) < 20)
			i.addItem(new ItemStack(Material.POTION, 1, (byte) 8227));

		if (new Random().nextInt(100) < 30)
			i.addItem(new ItemStack(Material.ENCHANTMENT_TABLE));

		if (!ScenariosEnable.IronKingE)
			setDefaultArmor(player);
		else
			setIronArmors(player);
	}

	private void setIronArmors(Player player) {
		Inventory i = player.getInventory();
		if (i.contains(Material.DIAMOND_SWORD)) {
			if (swordlevel.get(player.getUniqueId()) >= 2)
				LevelEnchant(player, 2);
			else
				LevelEnchant(player, 3);
		} else { // iron sword
			if (swordlevel.get(player.getUniqueId()) >= 2)
				LevelEnchant(player, 3);
			else
				LevelEnchant(player, 4);
		}

	}

	private void LevelEnchant(Player player, int MaxLevel) {
		PlayerInventory i = player.getInventory();
		i.setHelmet(randomEnchant(new ItemStack(Material.IRON_HELMET), Enchantment.PROTECTION_ENVIRONMENTAL, MaxLevel));
		i.setChestplate(
				randomEnchant(new ItemStack(Material.IRON_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, MaxLevel));
		i.setLeggings(
				randomEnchant(new ItemStack(Material.IRON_LEGGINGS), Enchantment.PROTECTION_ENVIRONMENTAL, MaxLevel));
		i.setBoots(randomEnchant(new ItemStack(Material.IRON_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, MaxLevel));
	}

	public void setDefaultArmor(Player player) {
		PlayerInventory i = player.getInventory();
		setIronArmors(player);
		// add DiamondArmor
		if (i.contains(Material.DIAMOND_SWORD)) {
			if (swordlevel.get(player.getUniqueId()) >= 2)
				addDiamondArmor(player, 1);
			else
				addDiamondArmor(player, 2);
		} else {
			if (swordlevel.get(player.getUniqueId()) >= 3)
				addDiamondArmor(player, 1);
			else
				addDiamondArmor(player, 2);
		}

	}

	public void addDiamondArmor(Player player, int MaxLevel) {
		PlayerInventory i = player.getInventory();
		int random1, random2;
		do {
			random1 = new Random().nextInt(diamondArmors.size());
			random2 = new Random().nextInt(diamondArmors.size());
		} while (random1 == random2);
		ItemStack armor1 = randomEnchant(new ItemStack(diamondArmors.get(random1)),
				Enchantment.PROTECTION_ENVIRONMENTAL, MaxLevel);
		ItemStack armor2 = randomEnchant(new ItemStack(diamondArmors.get(random2)),
				Enchantment.PROTECTION_ENVIRONMENTAL, MaxLevel);
		switch (armor1.getType()) {
		case DIAMOND_HELMET:
			i.setHelmet(armor1);
			break;
		case DIAMOND_CHESTPLATE:
			i.setChestplate(armor1);
			break;
		case DIAMOND_LEGGINGS:
			i.setLeggings(armor1);
			break;
		case DIAMOND_BOOTS:
			i.setBoots(armor1);
			break;
		default:
			break;
		}
		switch (armor2.getType()) {
		case DIAMOND_HELMET:
			i.setHelmet(armor2);
			break;
		case DIAMOND_CHESTPLATE:
			i.setChestplate(armor2);
			break;
		case DIAMOND_LEGGINGS:
			i.setLeggings(armor2);
			break;
		case DIAMOND_BOOTS:
			i.setBoots(armor2);
			break;
		default:
			break;
		}
	}

	public ItemStack randomEnchant(ItemStack item, Enchantment enchant, int maxLevel) {
		int level = (new Random().nextInt(maxLevel)) + 1;
		item.addUnsafeEnchantment(enchant, level);
		return item;
	}

	public ItemStack swordEnchant(Player p, ItemStack item, Enchantment enchant, int maxLevel) {
		int level = (new Random().nextInt(maxLevel)) + 1;
		item.addUnsafeEnchantment(enchant, level);
		if (level != 3 && new Random().nextInt(100) < 10) {
			item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
			swordlevel.put(p.getUniqueId(), level + 1);
		} else {
			swordlevel.put(p.getUniqueId(), level);
		}
		return item;
	}

	public static void setListItem() {
		diamondArmors.add(Material.DIAMOND_HELMET);
		diamondArmors.add(Material.DIAMOND_CHESTPLATE);
		diamondArmors.add(Material.DIAMOND_LEGGINGS);
		diamondArmors.add(Material.DIAMOND_BOOTS);

		for (int a = 0; a < 4; a++) {
			sword.add(Material.DIAMOND_SWORD);
		}
		for (int a = 0; a < 6; a++) {
			sword.add(Material.IRON_SWORD);
		}
	}

	public static ItemStack goldenHead(int amount) {
		ItemStack head = new ItemStack(Material.GOLDEN_APPLE, amount);
		ItemMeta meta = head.getItemMeta();
		meta.setDisplayName(Lang.headName);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§d回復效果會有 §610 §d秒鐘");
		lore.add("§6大量回血必備的物品!");
		meta.setLore(lore);
		head.setItemMeta(meta);
		return head;
	}

}
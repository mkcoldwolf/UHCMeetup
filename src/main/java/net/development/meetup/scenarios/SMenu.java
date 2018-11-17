package net.development.meetup.scenarios;

import net.development.meetup.util.ItemBuilder;
import net.development.meetup.util.Menu;
import net.development.meetup.util.PlaySound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SMenu extends Menu {

    @SuppressWarnings("deprecation")
    public SMenu(Player p) {
        super("&6&l模式&f&l選單", 3);
        if (ScenariosEnable.Timebomb.contains(p.getUniqueId())) {
            s(10, ItemBuilder.createItem1Ench(Material.CHEST.getId(), 1, 0, "&6&lTimeBomb",
                    "&f投票數量:&6 " + ScenariosEnable.Timebomb.size()));
        } else {
            s(10, ItemBuilder.createItem1(Material.CHEST, 1, 0, "&6&lTimeBomb",
                    "&f投票數量:&6 " + ScenariosEnable.Timebomb.size()));
        }
        if (ScenariosEnable.NoClean.contains(p.getUniqueId())) {
            s(11, ItemBuilder.createItem1Ench(Material.IRON_SWORD.getId(), 1, 0, "&d&lNoClean",
                    "&f投票數量:&6 " + ScenariosEnable.NoClean.size()));
        } else {
            s(11, ItemBuilder.createItem1(Material.IRON_SWORD, 1, 0, "&d&lNoClean",
                    "&f投票數量:&6 " + ScenariosEnable.NoClean.size()));
        }
        if (ScenariosEnable.BowLess.contains(p.getUniqueId())) {
            s(12, ItemBuilder.createItem1Ench(Material.BOW.getId(), 1, 0, "&e&lBowLess",
                    "&f投票數量:&6 " + ScenariosEnable.BowLess.size()));
        } else {
            s(12, ItemBuilder.createItem1(Material.BOW, 1, 0, "&e&lBowLess",
                    "&f投票數量:&6 " + ScenariosEnable.BowLess.size()));
        }
        if (ScenariosEnable.RodLess.contains(p.getUniqueId())) {
            s(13, ItemBuilder.createItem1Ench(Material.FISHING_ROD.getId(), 1, 0, "&9&lRodLess",
                    "&f投票數量:&6 " + ScenariosEnable.RodLess.size()));
        } else {
            s(13, ItemBuilder.createItem1(Material.FISHING_ROD, 1, 0, "&9&lRodLess",
                    "&f投票數量:&6 " + ScenariosEnable.RodLess.size()));
        }
        if (ScenariosEnable.FireLess.contains(p.getUniqueId())) {
            s(14, ItemBuilder.createItem1Ench(Material.FLINT_AND_STEEL.getId(), 1, 0, "&6&lFireLess",
                    "&f投票數量:&6 " + ScenariosEnable.FireLess.size()));
        } else {
            s(14, ItemBuilder.createItem1(Material.FLINT_AND_STEEL, 1, 0, "&6&lFireLess",
                    "&f投票數量:&6 " + ScenariosEnable.FireLess.size()));
        }
        if (ScenariosEnable.AirDrops.contains(p.getUniqueId())) {
            s(15, ItemBuilder.createItem1Ench(Material.ENDER_CHEST.getId(), 1, 0, "&e&lAirDrops",
                    "&f投票數量:&6 " + ScenariosEnable.AirDrops.size()));
        } else {
            s(15, ItemBuilder.createItem1(Material.ENDER_CHEST, 1, 0, "&e&lAirDrops",
                    "&f投票數量:&6 " + ScenariosEnable.AirDrops.size()));
        }
        if (ScenariosEnable.IronKing.contains(p.getUniqueId())) {
            s(16, ItemBuilder.createItem1Ench(Material.IRON_INGOT.getId(), 1, 0, "&f&lIron&c&lKing",
                    "&f投票數量:&6 " + ScenariosEnable.IronKing.size()));
        } else {
            s(16, ItemBuilder.createItem1(Material.IRON_INGOT, 1, 0, "&f&lIron&c&lKing",
                    "&f投票數量:&6 " + ScenariosEnable.IronKing.size()));
        }
    }

    @Override
    public void onClick(Player player, ItemStack item, ItemStack[] items) {
        String name = item.getItemMeta().getDisplayName();
        if (name.equals("§6§lTimeBomb")) {
            removePoint(player);
            ScenariosEnable.Timebomb.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        } else if (name.equals("§d§lNoClean")) {
            removePoint(player);
            ScenariosEnable.NoClean.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        } else if (name.equals("§e§lBowLess")) {
            removePoint(player);
            ScenariosEnable.BowLess.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        } else if (name.equals("§9§lRodLess")) {
            removePoint(player);
            ScenariosEnable.RodLess.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        } else if (name.equals("§6§lFireLess")) {
            removePoint(player);
            ScenariosEnable.FireLess.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        } else if (name.equals("§e§lAirDrops")) {
            removePoint(player);
            ScenariosEnable.AirDrops.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        } else if (name.equals("§f§lIron§c§lKing")) {
            removePoint(player);
            ScenariosEnable.IronKing.add(player.getUniqueId());
            PlaySound.PlayerSound(player, Sound.CLICK);
        }
        updateInv(player);

    }

    @SuppressWarnings("deprecation")
    private void updateInv(Player p) {
        if (ScenariosEnable.Timebomb.contains(p.getUniqueId())) {
            s(10, ItemBuilder.createItem1Ench(Material.CHEST.getId(), 1, 0, "&6&lTimeBomb",
                    "&f投票數量:&6 " + ScenariosEnable.Timebomb.size()));
        } else {
            s(10, ItemBuilder.createItem1(Material.CHEST, 1, 0, "&6&lTimeBomb",
                    "&f投票數量:&6 " + ScenariosEnable.Timebomb.size()));
        }
        if (ScenariosEnable.NoClean.contains(p.getUniqueId())) {
            s(11, ItemBuilder.createItem1Ench(Material.IRON_SWORD.getId(), 1, 0, "&d&lNoClean",
                    "&f投票數量:&6 " + ScenariosEnable.NoClean.size()));
        } else {
            s(11, ItemBuilder.createItem1(Material.IRON_SWORD, 1, 0, "&d&lNoClean",
                    "&f投票數量:&6 " + ScenariosEnable.NoClean.size()));
        }
        if (ScenariosEnable.BowLess.contains(p.getUniqueId())) {
            s(12, ItemBuilder.createItem1Ench(Material.BOW.getId(), 1, 0, "&e&lBowLess",
                    "&f投票數量:&6 " + ScenariosEnable.BowLess.size()));
        } else {
            s(12, ItemBuilder.createItem1(Material.BOW, 1, 0, "&e&lBowLess",
                    "&f投票數量:&6 " + ScenariosEnable.BowLess.size()));
        }
        if (ScenariosEnable.RodLess.contains(p.getUniqueId())) {
            s(13, ItemBuilder.createItem1Ench(Material.FISHING_ROD.getId(), 1, 0, "&9&lRodLess",
                    "&f投票數量:&6 " + ScenariosEnable.RodLess.size()));
        } else {
            s(13, ItemBuilder.createItem1(Material.FISHING_ROD, 1, 0, "&9&lRodLess",
                    "&f投票數量:&6 " + ScenariosEnable.RodLess.size()));
        }
        if (ScenariosEnable.FireLess.contains(p.getUniqueId())) {
            s(14, ItemBuilder.createItem1Ench(Material.FLINT_AND_STEEL.getId(), 1, 0, "&6&lFireLess",
                    "&f投票數量:&6 " + ScenariosEnable.FireLess.size()));
        } else {
            s(14, ItemBuilder.createItem1(Material.FLINT_AND_STEEL, 1, 0, "&6&lFireLess",
                    "&f投票數量:&6 " + ScenariosEnable.FireLess.size()));
        }
        if (ScenariosEnable.AirDrops.contains(p.getUniqueId())) {
            s(15, ItemBuilder.createItem1Ench(Material.ENDER_CHEST.getId(), 1, 0, "&e&lAirDrops",
                    "&f投票數量:&6 " + ScenariosEnable.AirDrops.size()));
        } else {
            s(15, ItemBuilder.createItem1(Material.ENDER_CHEST, 1, 0, "&e&lAirDrops",
                    "&f投票數量:&6 " + ScenariosEnable.AirDrops.size()));
        }
        if (ScenariosEnable.IronKing.contains(p.getUniqueId())) {
            s(16, ItemBuilder.createItem1Ench(Material.IRON_INGOT.getId(), 1, 0, "&f&lIron&c&lKing",
                    "&f投票數量:&6 " + ScenariosEnable.IronKing.size()));
        } else {
            s(16, ItemBuilder.createItem1(Material.IRON_INGOT, 1, 0, "&f&lIron&c&lKing",
                    "&f投票數量:&6 " + ScenariosEnable.IronKing.size()));
        }

    }

    public static void removePoint(Player p) {
        ScenariosEnable.Timebomb.remove(p.getUniqueId());
        ScenariosEnable.NoClean.remove(p.getUniqueId());
        ScenariosEnable.BowLess.remove(p.getUniqueId());
        ScenariosEnable.RodLess.remove(p.getUniqueId());
        ScenariosEnable.FireLess.remove(p.getUniqueId());
        ScenariosEnable.AirDrops.remove(p.getUniqueId());
        ScenariosEnable.IronKing.remove(p.getUniqueId());
    }

}

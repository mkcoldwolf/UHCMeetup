package mitw.meetup.gui;

import lombok.RequiredArgsConstructor;
import mitw.meetup.UHCMeetup;
import mitw.meetup.player.IPlayerCache;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.util.StringUtil;
import net.development.mitw.menu.Button;
import net.development.mitw.menu.Menu;
import net.development.mitw.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StatsGUI extends Menu {

    private IPlayerCache cache;

    public StatsGUI(IPlayerCache playerCache) {
        this.cache = playerCache;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + cache.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new NullActionButton(new ItemBuilder(Material.GOLD_INGOT).name(UHCMeetup.getInstance().getLanguage().translate(player, "wins") + cache.getWins()).build()));
        buttons.put(1, new NullActionButton(new ItemBuilder(Material.IRON_SWORD).name(UHCMeetup.getInstance().getLanguage().translate(player, "kills") + cache.getGlobal_kills()).build()));
        buttons.put(2, new NullActionButton(new ItemBuilder(Material.SKULL_ITEM).durability(2).name(UHCMeetup.getInstance().getLanguage().translate(player, "deaths") + cache.getDeaths()).build()));
        buttons.put(3, new NullActionButton(new ItemBuilder(Material.BOOK).name("§fKDR: §6" + cache.getKDR()).build()));
        buttons.put(4, new NullActionButton(new ItemBuilder(Material.GOLD_AXE).name(UHCMeetup.getInstance().getLanguage().translate(player, "elo") + cache.getElo()).build()));
        buttons.put(5, new NullActionButton(new ItemBuilder(Material.DIAMOND).name(UHCMeetup.getInstance().getLanguage().translate(player, "elo_ranking") + UHCMeetup.getInstance().getLeaderboardManager().getRatingPosition(player.getUniqueId().toString())).build()));

        buttons.put(8, new LeaderboardButton());

        return buttons;
    }

    @RequiredArgsConstructor
    private class NullActionButton extends Button {
        private final ItemStack itemStack;

        @Override
        public ItemStack getButtonItem(Player player) {
            return itemStack;
        }
    }

    private class LeaderboardButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ENCHANTED_BOOK).name(UHCMeetup.getInstance().getLanguage().translate(player, "leaderboard")).build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new LeaderboardGUI().openMenu(player);
        }
    }
}

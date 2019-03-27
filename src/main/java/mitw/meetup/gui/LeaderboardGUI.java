package mitw.meetup.gui;

import lombok.RequiredArgsConstructor;
import mitw.meetup.UHCMeetup;
import mitw.meetup.player.Rank;
import net.development.mitw.menu.Button;
import net.development.mitw.menu.Menu;
import net.development.mitw.utils.FastUUID;
import net.development.mitw.utils.ItemBuilder;
import net.development.mitw.uuid.UUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardGUI extends Menu {

    @Override
    public String getTitle(Player player) {
        return UHCMeetup.getInstance().getLanguage().translate(player, "leaderboard_title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new RatingButton());
        buttons.put(1, new WinsButton());
        buttons.put(2, new KillsButton());
        buttons.put(3, new kdrButton());

        return buttons;
    }

    private class RatingButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder rating = new ItemBuilder(Material.DIAMOND_BLOCK).name(UHCMeetup.getInstance().getLanguage().translate(player, "leaderboard_rating"));
            int i = 1;
            Map<String, Integer> top = UHCMeetup.getInstance().getLeaderboardManager().getRatingTop();
            for (final String name : top.keySet()) {
                if (i > 10) {
                    break;
                }
                rating.lore("§e" + i + ") " + name + " §7- " + Rank.getRank(top.get(name)) + top.get(name));
                i++;
            }
            return rating.build();
        }
    }

    private class WinsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder rating = new ItemBuilder(Material.GOLD_BLOCK).name(UHCMeetup.getInstance().getLanguage().translate(player, "leaderboard_wins"));
            int i = 1;
            Map<String, Integer> top = UHCMeetup.getInstance().getLeaderboardManager().getWinsTop();
            for (final String name : top.keySet()) {
                if (i > 10) {
                    break;
                }
                rating.lore("§e" + i + ") " + name + " §7- §6" + top.get(name));
                i++;
            }
            return rating.build();
        }
    }

    private class KillsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder rating = new ItemBuilder(Material.IRON_BLOCK).name(UHCMeetup.getInstance().getLanguage().translate(player, "leaderboard_kills"));
            int i = 1;
            Map<String, Integer> top = UHCMeetup.getInstance().getLeaderboardManager().getKillsTop();
            for (final String name : top.keySet()) {
                if (i > 10) {
                    break;
                }
                rating.lore("§e" + i + ") " + name + " §7- §6" + top.get(name));
                i++;
            }
            return rating.build();
        }
    }

    private class kdrButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder rating = new ItemBuilder(Material.REDSTONE_BLOCK).name("§cKDR");
            int i = 1;
            Map<String, Double> top = UHCMeetup.getInstance().getLeaderboardManager().getKdrTop();
            for (final String name : top.keySet()) {
                if (i > 10) {
                    break;
                }
                rating.lore("§e" + i + ") " + name+ " §7- §6" + top.get(name));
                i++;
            }
            return rating.build();
        }
    }
}

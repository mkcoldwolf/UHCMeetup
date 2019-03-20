package mitw.meetup.gui;

import lombok.RequiredArgsConstructor;
import mitw.meetup.UHCMeetup;
import mitw.meetup.util.StringUtil;
import net.development.mitw.menu.Button;
import net.development.mitw.menu.Menu;
import net.development.mitw.utils.ItemBuilder;
import net.development.mitw.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SimpleInventorySnapshot extends Menu {

    private final String name;
    private final ItemStack[] contents;
    private final ItemStack[] armorContents;
    private final double health;
    private final int hunger;

    public SimpleInventorySnapshot(Player player) {
        this.name = player.getName();
        this.contents = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();
        this.health = player.getHealth() + ((CraftPlayer)player).getHandle().getAbsorptionHearts();
        this.hunger = player.getFoodLevel();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (ItemStack itemStack : contents) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                i++;
                continue;
            }
            buttons.put(i++, new NullActionButton(itemStack));
        }

        i = 36;
        for (ItemStack itemStack : armorContents) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                i++;
                continue;
            }
            buttons.put(i++, new NullActionButton(itemStack));
        }

        buttons.put(44, new NullActionButton(new ItemBuilder(Material.GOLDEN_APPLE)
                .name(UHCMeetup.getInstance().getLanguage().translate(player, "health") + StringUtil.FORMAT.format(health)).build()));
        buttons.put(43, new NullActionButton(new ItemBuilder(Material.COOKED_BEEF)
                .name(UHCMeetup.getInstance().getLanguage().translate(player, "hunger") + StringUtil.FORMAT.format(hunger)).build()));
        buttons.put(42, new StatsButton());

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + name;
    }

    @RequiredArgsConstructor
    private class NullActionButton extends Button {
        private final ItemStack itemStack;

        @Override
        public ItemStack getButtonItem(Player player) {
            return itemStack;
        }
    }

    private class StatsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .name(UHCMeetup.getInstance().getLanguage().translate(player, "stats")).build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
            player.chat("/stats " + name);
        }
    }
}

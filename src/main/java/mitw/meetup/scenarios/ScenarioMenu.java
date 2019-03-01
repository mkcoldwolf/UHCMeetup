package mitw.meetup.scenarios;

import lombok.RequiredArgsConstructor;
import mitw.meetup.UHCMeetup;
import mitw.meetup.player.PlayerProfile;
import net.development.mitw.menu.Button;
import net.development.mitw.menu.Menu;
import net.development.mitw.utils.ItemBuilder;
import net.development.mitw.utils.RV;
import net.development.mitw.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return UHCMeetup.getInstance().getLanguage().translate(player, "scenario_menu_title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (Scenario scenario : Scenario.values()) {
            buttons.put(i++, new ScenarioButton(scenario));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class ScenarioButton extends Button {

        private final Scenario scenario;

        @Override
        public ItemStack getButtonItem(Player player) {

            ItemBuilder itemBuilder = new ItemBuilder(scenario.getDisplayMaterial());

            itemBuilder.name(scenario.getDisplayName(player));

            List<String> loresWithoutPH = UHCMeetup.getInstance().getLanguage().translateArrays(player, scenario.getVotes() < 3 ? "vote_nonFull" : "vote_full");
            List<String> lores = new ArrayList<>();

            for (String lore : loresWithoutPH) {

                if (lore.equals("<description>")) {

                    continue;

                }

                lores.add(StringUtil.replace(lore, RV.o("<votes>", scenario.getVotes() + "")));

            }

            itemBuilder.lore(lores);

            return itemBuilder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            PlayerProfile profile = UHCMeetup.getInstance().getGameManager().getProfile(player.getUniqueId());

            if (profile.getVoted() != null) {
                profile.getVoted().setVotes(profile.getVoted().getVotes() - 1);
            }

            profile.setVoted(scenario);
            scenario.setVotes(scenario.getVotes() + 1);
            UHCMeetup.getInstance().getLanguage().send(player, "vote_to", RV.o("%0", scenario.getDisplayName(player)));
            update();
        }
    }

    public static void update() {

        Menu.currentlyOpenedMenus.forEach((uuid, menu) -> {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                return;
            }

            if (menu instanceof ScenarioMenu) {
                if (player.getOpenInventory() != null) {

                    Inventory inventory = player.getOpenInventory().getTopInventory();

                    inventory.setContents(new ItemStack[inventory.getSize()]);

                    for (final Map.Entry<Integer, Button> buttonEntry : menu.getButtons().entrySet()) {
                        inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));
                    }

                }
            }
        });

    }
}

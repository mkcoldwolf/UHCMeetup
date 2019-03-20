package mitw.meetup.manager;

import java.util.*;

import lombok.RequiredArgsConstructor;
import mitw.meetup.UHCMeetup;
import mitw.meetup.enums.GameStatus;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.player.UHCTeam;
import net.development.mitw.menu.Button;
import net.development.mitw.menu.Menu;
import net.development.mitw.utils.ItemBuilder;
import net.development.mitw.utils.RV;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import net.development.mitw.uuid.UUIDCache;
import org.bukkit.inventory.ItemStack;

public class TeamManager {

    private static TeamManager ins;
    public List<UHCTeam> teams = new ArrayList<>();

    public TeamManager() {
        for (int i = 0; i < 27; i++) {
            teams.add(new UHCTeam(i));
        }
    }

    public static TeamManager getInstance() {
        if (ins == null) {
            ins = new TeamManager();
        }
        return ins;
    }

    public void openGUI(final Player p) {
        new TeamMenu().openMenu(p);
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

    private class TeamMenu extends Menu {

        @Override
        public String getTitle(Player player) {
            return UHCMeetup.getInstance().getLanguage().translate(player, "team_gui_title");
        }

        @Override
        public Map<Integer, Button> getButtons(Player player) {

            Map<Integer, Button> buttons = new HashMap<>();

            for (int i = 0; i < teams.size(); i++) {

                buttons.put(i, new TeamButton(teams.get(i)));

            }

            return buttons;
        }
    }

    @RequiredArgsConstructor
    private class TeamButton extends Button {

        private final UHCTeam team;

        @Override
        public ItemStack getButtonItem(Player player) {
            final int size = team.members.size();
            final ItemBuilder builder = new ItemBuilder(Material.WOOL);

            if (size == 2) {
                builder.durability(14);
            } else if (size == 1) {
                builder.durability(5);
            } else {
                builder.durability(0);
            }

            builder.name("§e" + UHCMeetup.getInstance().getLanguage().translate(player, "team") + " " + (team.id + 1))
                    .lore("§7" + UHCMeetup.getInstance().getLanguage().translate(player, "teammate_count") + ": §6" + size + "/2");
            for (final UUID uuid : team.members) {
                builder.lore("§7- §e" + UUIDCache.getName(uuid));
            }

            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

            if (!GameStatus.is(GameStatus.WAITING)) {
                player.closeInventory();
                return;
            }

            final PlayerProfile up = UHCMeetup.getInstance().getGameManager().getProfile(player.getUniqueId());

            if (team.members.contains(player.getUniqueId())) {
                player.sendMessage(UHCMeetup.getInstance().getLanguage().translate(player, "already_in_team"));
                return;
            }

            if (up.isInTeam()) {
                final UHCTeam teamBefore = up.getTeam();
                if (teamBefore != null) {
                    teamBefore.members.remove(player.getUniqueId());
                }
            }

            if (team.members.size() < 2) {

                team.members.add(player.getUniqueId());

            } else {
                player.sendMessage(UHCMeetup.getInstance().getLanguage().translate(player, "team_full"));
                return;
            }

            TeamManager.getInstance().updateGUIs();
            up.setTeam(team, team.id);

            UHCMeetup.getInstance().getLanguage().send(player, "join_team", RV.o("%0", (team.id + 1) + ""));
        }
    }

    public void updateGUIs() {

        Menu.currentlyOpenedMenus.forEach((uuid, menu) -> {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                return;
            }

            if (menu instanceof TeamMenu) {
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

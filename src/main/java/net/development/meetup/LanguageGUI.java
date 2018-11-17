package net.development.meetup;

import net.development.meetup.util.ItemBuilder;
import net.development.meetup.util.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LanguageGUI extends Menu {

    public LanguageGUI() {
        super("&6&lLanguages &7| &f語言", 1);
        s(0, ItemBuilder.createItem1(Material.BOOK, 1, 0, "&6&l中文", "&7click me to select this language!", "&7點我選擇該語言!"));
        s(1, ItemBuilder.createItem1(Material.BOOK, 1, 0, "&b&lEnglish", "&7click me to select this language!", "&7點我選擇該語言!"));
    }

    @Override
    public void onClick(Player var1, ItemStack var2, ItemStack[] items) {
        if (var2.getItemMeta().getDisplayName().equals("§6§l中文")) {
            Main.get().getLang().setLang(var1, "zh_tw", true);
            var1.sendMessage(Main.get().getLang().translate(var1, "choose"));
        } else {
            Main.get().getLang().setLang(var1, "en_us", true);
            var1.sendMessage(Main.get().getLang().translate(var1, "choose"));
        }
    }

}

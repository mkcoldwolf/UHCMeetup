package mitw.meetup.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {


    @SuppressWarnings("deprecation")
    public static ItemStack createNormal(ItemStack itemStack, int amount, int data) {
        ItemStack item = new ItemStack(amount, (short) data);
        return item;
    }

    public static ItemStack createArmor(ItemStack items) {
        ItemStack item = items;
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        return item;
    }


    @SuppressWarnings("deprecation")
    public static ItemStack createItem(int id, int amount, int data, String name) {
        ItemStack item = new ItemStack(id, amount, (byte) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', (String) name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createCabeza(String owner, String name, String... loreOptions) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', (String) name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = loreOptions;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta((ItemMeta) meta);
        return item;
    }

    public static ItemStack createCabeza(String owner, String name, Integer amount, String... loreOptions) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', (String) name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = loreOptions;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta((ItemMeta) meta);
        return item;
    }


    @SuppressWarnings("deprecation")
    public static ItemStack createItem1(int id, int amount, int data, String name, String... lore) {
        ItemStack item = new ItemStack(id, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', (String) name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = lore;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem1(Material mat, int amount, int data, String name, String... lore) {
        ItemStack item = new ItemStack(mat, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = lore;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes('&', b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta(meta);
        return item;
    }


    @SuppressWarnings("deprecation")
    public static ItemStack createItem1(int id, int amount, int data, String name, List<String> lore) {
        ItemStack item = new ItemStack(id, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', (String) name));
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem1(Material mat, int amount, int data, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', name));
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }


    @SuppressWarnings("deprecation")
    public static ItemStack createItem2(int id, int amount, int data, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(id, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }


    @SuppressWarnings("deprecation")
    public static ItemStack createItem3(int id, int amount, int data, String name, String[] strings) {
        ItemStack item = new ItemStack(id, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = strings;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char) '&', (String) b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createKit(ItemStack[] armor) {

        return null;
    }
}
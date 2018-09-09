package net.development.meetup.util;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

    public EnchantGlow(int id) {
        super(id);
    }

    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    public EnchantmentTarget getItemTarget() {
        return null;
    }

    public int getMaxLevel() {
        return 10;
    }

    public String getName() {
        return "Glow";
    }

    public int getStartLevel() {
        return 1;
    }

    public static Enchantment getGlow() {
        if (glow != null) {
            return glow;
        }
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        glow = new EnchantGlow(255);
        Enchantment.registerEnchantment((Enchantment)glow);
        return glow;
    }

    public static void addGlow(ItemStack item) {
        Enchantment glow = EnchantGlow.getGlow();
        item.addEnchantment(glow, 1);
    }
}


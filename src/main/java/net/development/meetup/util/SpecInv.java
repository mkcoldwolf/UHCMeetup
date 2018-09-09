package net.development.meetup.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpecInv extends Menu {
	
    public SpecInv(Player target) {
    	
        super(target.getName().toString(), 6);
                
        if (target.getInventory().getHelmet() != null) {
            this.s(0, ItemBuilder.createArmor(target.getInventory().getHelmet()));
        }
        if (target.getInventory().getChestplate() != null) {
            this.s(1, ItemBuilder.createArmor(target.getInventory().getChestplate()));
        }
        if (target.getInventory().getLeggings() != null) {
            this.s(2, ItemBuilder.createArmor(target.getInventory().getLeggings()));
        }
        if (target.getInventory().getBoots() != null) {
            this.s(3, ItemBuilder.createArmor(target.getInventory().getBoots()));
        }
        
//        s(8, ItemBuilder.createItem1(Material., amount, data, name, lore))
        
        if (target.getInventory().getItem(0) != null) {
            this.s(45, ItemBuilder.createArmor(target.getInventory().getItem(0)));
        }
        if (target.getInventory().getItem(1) != null) {
            this.s(46, ItemBuilder.createArmor(target.getInventory().getItem(1)));
        }
        if (target.getInventory().getItem(2) != null) {
            this.s(47, ItemBuilder.createArmor(target.getInventory().getItem(2)));
        }
        if (target.getInventory().getItem(3) != null) {
            this.s(48, ItemBuilder.createArmor(target.getInventory().getItem(3)));
        }
        if (target.getInventory().getItem(4) != null) {
            this.s(49, ItemBuilder.createArmor(target.getInventory().getItem(4)));
        }
        if (target.getInventory().getItem(5) != null) {
            this.s(50, ItemBuilder.createArmor(target.getInventory().getItem(5)));
        }
        if (target.getInventory().getItem(6) != null) {
            this.s(51, ItemBuilder.createArmor(target.getInventory().getItem(6)));
        }
        if (target.getInventory().getItem(7) != null) {
            this.s(52, ItemBuilder.createArmor(target.getInventory().getItem(7)));
        }
        if (target.getInventory().getItem(8) != null) {
            this.s(53, ItemBuilder.createArmor(target.getInventory().getItem(8)));
        }
        if (target.getInventory().getItem(9) != null) {
            this.s(36, ItemBuilder.createArmor(target.getInventory().getItem(9)));
        }
        if (target.getInventory().getItem(10) != null) {
            this.s(37, ItemBuilder.createArmor(target.getInventory().getItem(10)));
        }
        if (target.getInventory().getItem(11) != null) {
            this.s(38, ItemBuilder.createArmor(target.getInventory().getItem(11)));
        }
        if (target.getInventory().getItem(12) != null) {
            this.s(39, ItemBuilder.createArmor(target.getInventory().getItem(12)));
        }
        if (target.getInventory().getItem(13) != null) {
            this.s(40, ItemBuilder.createArmor(target.getInventory().getItem(13)));
        }
        if (target.getInventory().getItem(14) != null) {
            this.s(41, ItemBuilder.createArmor(target.getInventory().getItem(14)));
        }
        if (target.getInventory().getItem(15) != null) {
            this.s(42, ItemBuilder.createArmor(target.getInventory().getItem(15)));
        }
        if (target.getInventory().getItem(16) != null) {
            this.s(43, ItemBuilder.createArmor(target.getInventory().getItem(16)));
        }
        if (target.getInventory().getItem(17) != null) {
            this.s(44, ItemBuilder.createArmor(target.getInventory().getItem(17)));
        }
        if (target.getInventory().getItem(18) != null) {
            this.s(27, ItemBuilder.createArmor(target.getInventory().getItem(18)));
        }
        if (target.getInventory().getItem(19) != null) {
            this.s(28, ItemBuilder.createArmor(target.getInventory().getItem(19)));
        }
        if (target.getInventory().getItem(20) != null) {
            this.s(29, ItemBuilder.createArmor(target.getInventory().getItem(20)));
        }
        if (target.getInventory().getItem(21) != null) {
            this.s(30, ItemBuilder.createArmor(target.getInventory().getItem(21)));
        }
        if (target.getInventory().getItem(22) != null) {
            this.s(31, ItemBuilder.createArmor(target.getInventory().getItem(22)));
        }
        if (target.getInventory().getItem(23) != null) {
            this.s(32, ItemBuilder.createArmor(target.getInventory().getItem(23)));
        }
        if (target.getInventory().getItem(24) != null) {
            this.s(33, ItemBuilder.createArmor(target.getInventory().getItem(24)));
        }
        if (target.getInventory().getItem(25) != null) {
            this.s(34, ItemBuilder.createArmor(target.getInventory().getItem(25)));
        }
        if (target.getInventory().getItem(26) != null) {
            this.s(35, ItemBuilder.createArmor(target.getInventory().getItem(26)));
        }
        if (target.getInventory().getItem(27) != null) {
            this.s(18, ItemBuilder.createArmor(target.getInventory().getItem(27)));
        }
        if (target.getInventory().getItem(28) != null) {
            this.s(19, ItemBuilder.createArmor(target.getInventory().getItem(28)));
        }
        if (target.getInventory().getItem(29) != null) {
            this.s(20, ItemBuilder.createArmor(target.getInventory().getItem(29)));
        }
        if (target.getInventory().getItem(30) != null) {
            this.s(21, ItemBuilder.createArmor(target.getInventory().getItem(30)));
        }
        if (target.getInventory().getItem(31) != null) {
            this.s(22, ItemBuilder.createArmor(target.getInventory().getItem(31)));
        }
        if (target.getInventory().getItem(32) != null) {
            this.s(23, ItemBuilder.createArmor(target.getInventory().getItem(32)));
        }
        if (target.getInventory().getItem(33) != null) {
            this.s(24, ItemBuilder.createArmor(target.getInventory().getItem(33)));
        }
        if (target.getInventory().getItem(34) != null) {
            this.s(25, ItemBuilder.createArmor(target.getInventory().getItem(34)));
        }
        if (target.getInventory().getItem(35) != null) {
            this.s(26, ItemBuilder.createArmor(target.getInventory().getItem(35)));
        }
    }

    @Override
    public void onClick(Player p, ItemStack stack, ItemStack[] items) {
    }
}


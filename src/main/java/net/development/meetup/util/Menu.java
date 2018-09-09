package net.development.meetup.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.development.meetup.Main;


public abstract class Menu implements Listener {
	
	String s = null;
    public Inventory _inv;

    public Menu(String name, int rows) {
        this._inv = Bukkit.createInventory((InventoryHolder)null, (int)(9 * rows), (String)ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        Main.get().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)Main.get());
        this.s = name;
    }

    public void a(ItemStack stack) {
        this._inv.addItem(new ItemStack[]{stack});
    }

    public void s(int i, ItemStack stack) {
        this._inv.setItem(i, stack);
    }
    
    public void s(ItemStack[] stack) {
        this._inv.addItem(stack);
    }
    
    public Inventory i() {
        return this._inv;
    }

    public String n() {
        return this._inv.getName();
    }

    public void o(Player p) {
        p.openInventory(this._inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
    		
    	if (e.getInventory().equals((Object)this.i()) && e.getCurrentItem() != null && i().contains(e.getCurrentItem()) && e.getWhoClicked() instanceof Player) {
            onClick((Player)e.getWhoClicked(), e.getCurrentItem(), e.getInventory().getContents());
            e.setCancelled(true);
        }
  
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals((Object)this.i()) && event.getPlayer() instanceof Player) {
            this.onClose((Player)event.getPlayer(), event.getInventory().getContents());
        }
    }

    public void onClose(Player player, ItemStack[] contents) {
    }

    public abstract void onClick(Player var1, ItemStack var2, ItemStack[] items);
}
package mitw.meetup.scenarios;

import mitw.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class AirDrops {

    private static AirDrops ins;
    private int x;
    private int y;
    private int z;
    private final Random random = new Random();

    public static AirDrops getInstance() {
        if (ins == null) ins = new AirDrops();
        return ins;
    }

    public void AnnounceDrops(String time, boolean bol) {
        if (bol) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "airDrop").replace("<x>", x + "").replace("<y>", y + "").replace("<z>", z + ""));
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(UHCMeetup.getInstance().getLanguage().translate(p, "airDropCountDown").replace("<time>", time).replace("<x>", x + "").replace("<y>", y + "").replace("<z>", z + ""));
            }
        }
    }

    public void dropChest() {
        Location loc = new Location(Bukkit.getWorld("world"), x, y + 1, z);
        loc.getBlock().setType(Material.CHEST);
        putItems(loc.clone());
        loc.add(0, -1, 0).getBlock().setType(Material.OBSIDIAN);
        loc.add(1, 0, 0).getBlock().setType(Material.OBSIDIAN);
        loc.add(0, 0, 1).getBlock().setType(Material.OBSIDIAN);
        loc.add(-1, 0, 0).getBlock().setType(Material.OBSIDIAN);
        loc.add(-1, 0, 0).getBlock().setType(Material.OBSIDIAN);
        loc.add(0, 0, -1).getBlock().setType(Material.OBSIDIAN);
        loc.add(0, 0, -1).getBlock().setType(Material.OBSIDIAN);
        loc.add(1, 0, 0).getBlock().setType(Material.OBSIDIAN);
        loc.add(1, 0, 0).getBlock().setType(Material.OBSIDIAN);
    }

    public void generateNewLocation() {
        int border = UHCMeetup.getInstance().getGameManager().getBorder();
        int i = border - 20;
        int j = -border + 15;
        int k = border - 17;
        int m = -border + 13;
        int x = randInt(j, i);
        int z = randInt(m, k);
        Block hBlock = Bukkit.getWorld("world").getHighestBlockAt(x, z);
        while (!hBlock.getType().isSolid())
            hBlock = hBlock.getRelative(0, -1, 0);
        int y = hBlock.getY() + 1;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    private ItemStack d1 = new ItemStack(Material.DIAMOND_HELMET);
    private ItemStack d2 = new ItemStack(Material.DIAMOND_CHESTPLATE);
    private ItemStack d3 = new ItemStack(Material.DIAMOND_LEGGINGS);
    private ItemStack d4 = new ItemStack(Material.DIAMOND_BOOTS);
    private ItemStack s1 = new ItemStack(Material.DIAMOND_SWORD);
    private ItemStack s2 = new ItemStack(Material.IRON_SWORD);
    private ItemStack b1 = new ItemStack(Material.BOW);
    private ItemStack b2 = new ItemStack(Material.BOW);

    public AirDrops() {
        d1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        d2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        d3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        d4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        s1.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        s2.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        s2.addEnchantment(Enchantment.FIRE_ASPECT, 1);
        b1.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
        b2.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
    }

    public void putItems(Location loc) {
        Block b = loc.getBlock();
        Chest c = (Chest) b.getState();
        if (random.nextInt(100) + 1 <= 60) {
            c.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, random.nextInt(10) + 6));
        }
        if (random.nextInt(100) + 1 <= 30) {
            c.getInventory().addItem(d1);
        } else if (random.nextInt(100) + 1 <= 20) {
            c.getInventory().addItem(d2);
        } else if (random.nextInt(100) + 1 <= 30) {
            c.getInventory().addItem(d3);
        } else if (random.nextInt(100) + 1 <= 40) {
            c.getInventory().addItem(d4);
        }
        if (random.nextInt(100) + 1 <= 15) {
            c.getInventory().addItem(s1);
        }
        if (random.nextInt(100) + 1 <= 10) {
            c.getInventory().addItem(s2);
        }
        if (random.nextInt(100) + 1 <= 20) {
            c.getInventory().addItem(b1);
        }
        if (random.nextInt(100) + 1 <= 25) {
            c.getInventory().addItem(b2);
        }
        if (random.nextInt(100) + 1 <= 70) {
            c.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, random.nextInt(16) + 16));
        }
        if (random.nextInt(100) + 1 <= 50) {
            c.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, random.nextInt(16) + 5));
        }
    }

}

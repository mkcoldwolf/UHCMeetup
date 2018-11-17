package net.development.meetup;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UHCTeam {

    public Player p1;
    public Player p2;
    public int id;
    public Location scatter;
    public int kills;

    public UHCTeam(Player p1, Player p2, int id) {
        this.p1 = p1;
        this.p2 = p2;
        this.id = id;
    }

    public boolean isAlive() {
        if (p1 != null && Main.getGM().players.contains(p1.getUniqueId())) return true;
        if (p2 != null && Main.getGM().players.contains(p2.getUniqueId())) return true;
        return false;
    }

}

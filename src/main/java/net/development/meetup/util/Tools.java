package net.development.meetup.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.task.LobbyTask;


public class Tools {
	
	public static int getCenter(Player player) {
        Location location = new Location(Bukkit.getWorld("world"), 0.0, player.getLocation().getY(), 0.0);
        int n = (int)Math.floor(player.getLocation().distance(location));
        return n;
    }

    public static String getTime(int o) {
        int i = ++o / 60;
        int j = o - i * 60;
        String str = null;
        str = i <= 0 ? (j < 10 ? "" + j : "" + j) : (i < 10 && i > 0 ? (j < 10 ? String.valueOf(i) + ":0" + j : String.valueOf(i) + ":" + j) : (j < 10 ? String.valueOf(i) + ":0" + j : String.valueOf(i) + ":" + j));
        return str;
    }

    public static String getTimeHora(int o) {    	
    	String timer;
    	int totalSecs = o;        
        int hours = totalSecs / 3600;        
        int minutes = totalSecs % 3600 / 60;        
        int seconds = totalSecs % 60;
        if (totalSecs >= 3600) {
        	timer = String.format("%02d:%02d:%02d", hours, minutes, seconds);        
        } else {
        	timer = String.format("%02d:%02d", minutes, seconds);        
        }        
        return timer;        
    }
    
    public static String UntiShrinkTime(int o) {
    	int totalSecs = o;        
        int hours = totalSecs / 3600;        
        int minutes = totalSecs / 60;        
        int seconds = totalSecs;
        String str = null;
        
        if (o >= 3600) {
        	str = "" + hours + "h";
        }
        if (o < 3600 && o >= 60 ) {
        	minutes = minutes + 1;
        	str = "" + (minutes) + "m";
        }
        if (o < 60) {
        	str = "" + seconds + "s";
        }        
        return str;
    }
    
    public static String getShrinkTime(Player p, int o) {
    	int i = ++o / 60;
		int j = o - i * 60;
		String seg = Main.get().getLang().translate(p, "s1");
		String segs = Main.get().getLang().translate(p, "s2");
		String min = Main.get().getLang().translate(p, "m1");
		String mins = Main.get().getLang().translate(p, "m2");
		String str = null;
		str = i <= 0 ? (j < 2 ? seg : segs) : (i < 2 && i > 0 ? (j < 10 ? min : min) : (j < 10 ? mins : mins));
		return str;
    }
    
    public static String getStartingText(Player p) {
    	if(LobbyTask.start) {
    		return (LobbyTask.i)+Main.get().getLang().translate(p, "s2");
    	}
    	return "15秒";
    }

}

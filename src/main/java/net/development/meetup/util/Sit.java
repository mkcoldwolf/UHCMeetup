package net.development.meetup.util;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class Sit {

	public static HashMap<Player, Integer> horses = new HashMap<>();

	public static void spawn(Player p) {
		Location l = p.getLocation();
		EntityPig horse = new EntityPig(((CraftWorld) l.getWorld()).getHandle());
	
		horse.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
		horse.setInvisible(true);

		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(horse);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

		horses.put(p, horse.getId());

		PacketPlayOutAttachEntity sit = new PacketPlayOutAttachEntity(0,
				((CraftPlayer) p).getHandle(), horse);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(sit);
	}

	public static void removeHorses(Player p) {

		if (horses.get(p) != null) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(horses.get(p));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

}

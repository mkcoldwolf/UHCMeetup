package mitw.meetup.util;

import mitw.meetup.UHCMeetup;
import mitw.meetup.task.LobbyTask;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

public class Util {

	public static int getCenter(final Player player) {
		final Location location = new Location(Bukkit.getWorld("world"), 0.0, player.getLocation().getY(), 0.0);
		final int n = (int) Math.floor(player.getLocation().distance(location));
		return n;
	}

	public static String getTime(int o) {
		final int i = ++o / 60;
		final int j = o - i * 60;
		String str = null;
		str = i <= 0 ? (j < 10 ? "" + j : "" + j)
				: (i < 10 && i > 0 ? (j < 10 ? String.valueOf(i) + ":0" + j : String.valueOf(i) + ":" + j)
						: (j < 10 ? String.valueOf(i) + ":0" + j : String.valueOf(i) + ":" + j));
		return str;
	}

	public static String getTimeHora(final int o) {
		String timer;
		final int totalSecs = o;
		final int hours = totalSecs / 3600;
		final int minutes = totalSecs % 3600 / 60;
		final int seconds = totalSecs % 60;
		if (totalSecs >= 3600) {
			timer = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			timer = String.format("%02d:%02d", minutes, seconds);
		}
		return timer;
	}

	public static String UntiShrinkTime(final int o) {
		final int totalSecs = o;
		final int hours = totalSecs / 3600;
		int minutes = totalSecs / 60;
		final int seconds = totalSecs;
		String str = null;

		if (o >= 3600) {
			str = "" + hours + "h";
		}
		if (o < 3600 && o >= 60) {
			minutes = minutes + 1;
			str = "" + (minutes) + "m";
		}
		if (o < 60) {
			str = "" + seconds + "s";
		}
		return str;
	}

	public static String getShrinkTime(final Player p, int o) {
		final int i = ++o / 60;
		final int j = o - i * 60;
		final String seg = UHCMeetup.getInstance().getLanguage().translate(p, "s1");
		final String segs = UHCMeetup.getInstance().getLanguage().translate(p, "s2");
		final String min = UHCMeetup.getInstance().getLanguage().translate(p, "m1");
		final String mins = UHCMeetup.getInstance().getLanguage().translate(p, "m2");
		String str = null;
		str = i <= 0 ? (j < 2 ? seg : segs) : (i < 2 && i > 0 ? (j < 10 ? min : min) : (j < 10 ? mins : mins));
		return str;
	}

	public static String colored(final String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static String getStartingText(final Player p) {
		if (LobbyTask.start)
			return LobbyTask.i + UHCMeetup.getInstance().getLanguage().translate(p, "s2");
		return "30" + UHCMeetup.getInstance().getLanguage().translate(p, "s2");
	}

	public static void setBlockFast(final Block block, final Material material, final boolean applyPhysics) {
		setBlockFast(block.getLocation(), material, applyPhysics);
	}

	public static void setBlockFast(final Location location, final Material material, final boolean applyPhysics) {
		setBlockFast(location, material.getId(), 0, applyPhysics);
	}

	public static void setBlockFast(final Location location, final int material, final boolean applyPhysics) {
		setBlockFast(location, material, 0, applyPhysics);
	}

	public static void setBlockFast(final Location location, final int blockId, final int data, final boolean applyPhysics) {
		setBlockFast(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockId, (byte) 0, applyPhysics);
	}

	public static void setBlockFast(final World world, final int x, final int y, final int z, final int blockId,
							 final byte data, final boolean applyPhysics) {
		try {
			final net.minecraft.server.v1_8_R3.World w = ((CraftWorld) world).getHandle();
			final net.minecraft.server.v1_8_R3.Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
			final BlockPosition bp = new BlockPosition(x, y, z);
			final int combined = blockId + (data << 12);
			final IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
			w.setTypeAndData(bp, ibd, applyPhysics ? 3 : 2);
			chunk.a(bp, ibd);
		} catch (final Throwable throwable) {
			throwable.printStackTrace();
		}
	}

}

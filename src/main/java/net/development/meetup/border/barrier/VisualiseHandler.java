package net.development.meetup.border.barrier;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Predicate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.development.meetup.Main;
import net.development.meetup.util.cuboid.Cuboid;

public class VisualiseHandler {

	private static VisualiseHandler instance;

	public static VisualiseHandler getInstance() {
		if (instance == null)
			instance = new VisualiseHandler();
		return instance;
	}

	private final Table<UUID, Location, VisualBlock> storedVisualises = HashBasedTable.create();

	public LinkedHashMap<Location, VisualBlockData> generate(Player player, Cuboid cuboid, VisualType visualType,
			boolean canOverwrite) {
		Collection<Location> locations = new HashSet<>(
				(int) (cuboid.getSizeX() * cuboid.getSizeY() * cuboid.getSizeZ()));
		Iterator<Block> iterator = cuboid.iterator();
		while (iterator.hasNext()) {
			locations.add(iterator.next().getLocation());
		}
		return generate(player, locations, visualType, canOverwrite);
	}

	@SuppressWarnings("deprecation")
	public LinkedHashMap<Location, VisualBlockData> generate(Player player, Iterable<Location> locations,
			VisualType visualType, boolean canOverwrite) {
		synchronized (this.storedVisualises) {
			LinkedHashMap<Location, VisualBlockData> results = new LinkedHashMap<>();

			List<VisualBlockData> filled = visualType.blockFiller().bulkGenerate(player, locations);
			int count;
			if (filled != null) {
				count = 0;
				for (Location location : locations) {
					if ((canOverwrite) || (!this.storedVisualises.contains(player.getUniqueId(), location))) {
						Material previousType = location.getBlock().getType();
						if ((!previousType.isSolid()) && (previousType == Material.AIR)) {
							VisualBlockData visualBlockData = (VisualBlockData) filled.get(count++);
							results.put(location, visualBlockData);
							player.sendBlockChange(location, visualBlockData.getBlockType(), visualBlockData.getData());
							this.storedVisualises.put(player.getUniqueId(), location,
									new VisualBlock(visualType, visualBlockData, location));
						}
					}
				}
			}
			return results;
		}
	}

	public boolean clearVisualBlock(Player player, Location location) {
		return clearVisualBlock(player, location, true);
	}

	@SuppressWarnings("deprecation")
	public boolean clearVisualBlock(Player player, Location location, boolean sendRemovalPacket) {
		synchronized (this.storedVisualises) {
			VisualBlock visualBlock = (VisualBlock) this.storedVisualises.remove(player.getUniqueId(), location);
			if ((sendRemovalPacket) && (visualBlock != null)) {
				Block block = location.getBlock();
				VisualBlockData visualBlockData = visualBlock.getBlockData();
				if ((visualBlockData.getBlockType() != block.getType())
						|| (visualBlockData.getData() != block.getData())) {
					player.sendBlockChange(location, block.getType(), block.getData());
				}
				return true;
			}
		}
		return false;
	}

	public Map<Location, VisualBlock> clearVisualBlocks(Player player, @Nullable VisualType visualType,
			@Nullable Predicate<VisualBlock> predicate) {
		return clearVisualBlocks(player, visualType, predicate, true);
	}

	@Deprecated
	public Map<Location, VisualBlock> clearVisualBlocks(Player player, @Nullable VisualType visualType,
			@Nullable Predicate<VisualBlock> predicate, boolean sendRemovalPackets) {
		synchronized (this.storedVisualises) {
			if (!this.storedVisualises.containsRow(player.getUniqueId())) {
				return Collections.emptyMap();
			}
			Map<Location, VisualBlock> results = new HashMap<>(this.storedVisualises.row(player.getUniqueId()));
			Map<Location, VisualBlock> removed = new HashMap<>();
			for (Map.Entry<Location, VisualBlock> entry : results.entrySet()) {
				VisualBlock visualBlock = (VisualBlock) entry.getValue();
				if (((predicate == null) || (predicate.apply(visualBlock)))
						&& ((visualType == null) || (visualBlock.getVisualType() == visualType))) {
					Location location = (Location) entry.getKey();
					if (removed.put(location, visualBlock) == null) {
						clearVisualBlock(player, location, sendRemovalPackets);
					}
				}
			}
			return removed;
		}
	}

	public void handlePositionChanged(Player player, final World toWorld, final int toX, final int toY, final int toZ) {
		VisualType visualType = VisualType.UHC_BORDER;

		clearVisualBlocks(player, visualType, new Predicate<VisualBlock>() {
			public boolean apply(VisualBlock visualBlock) {
				Location other = visualBlock.getLocation();
				return (other.getWorld().equals(toWorld)) && ((Math.abs(toX - other.getBlockX()) > 5)
						|| (Math.abs(toY - other.getBlockY()) > 4) || (Math.abs(toZ - other.getBlockZ()) > 5));
			}
		});
		int minHeight = toY - 3;
		int maxHeight = toY + 4;

		double x = Main.getGM().getBorder();
		double z = Main.getGM().getBorder();

		Location loc1 = new Location(toWorld, x, 0.0D, -z);
		Location loc2 = new Location(toWorld, -x, 0.0D, z);

		Cuboid cb = new Cuboid(loc1, loc2);

		Collection<Vector> edges = cb.edges();
		for (Vector edge : edges) {
			if ((Math.abs(edge.getBlockX() - toX) <= 5) && (Math.abs(edge.getBlockZ() - toZ) <= 5)) {
				Location location = edge.toLocation(toWorld);
				if (location != null) {
					Location first = location.clone();
					first.setY(minHeight);
					Location second = location.clone();
					second.setY(maxHeight);
					generate(player, new Cuboid(first, second), visualType, false).size();
				}
			}
		}
	}
}
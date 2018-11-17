package net.development.meetup.border.barrier;

import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

abstract class BlockFiller {
    @Deprecated
    VisualBlockData generate(Player player, World world, int x, int y, int z) {
        return generate(player, new Location(world, x, y, z));
    }

    abstract VisualBlockData generate(Player paramPlayer, Location paramLocation);

    List<VisualBlockData> bulkGenerate(Player player, Iterable<Location> locations) {
        List<VisualBlockData> data = new ArrayList<>(Iterables.size(locations));
        for (Location location : locations) {
            data.add(generate(player, location));
        }
        return data;
    }
}

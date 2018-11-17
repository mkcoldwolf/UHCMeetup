package net.development.meetup.border.barrier;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

enum VisualType {
    UHC_BORDER {
        @Override
        BlockFiller blockFiller() {
            final BlockFiller blockFiller = new BlockFiller() {

                @SuppressWarnings("deprecation")
                @Override
                VisualBlockData generate(Player paramPlayer, Location paramLocation) {
                    return new VisualBlockData(Material.STAINED_GLASS, DyeColor.ORANGE.getData());
                }
            };
            return blockFiller;
        }
    };

    private VisualType() {
    }

    abstract BlockFiller blockFiller();
}

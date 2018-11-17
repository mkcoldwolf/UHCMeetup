package net.development.meetup.border.barrier;

import org.bukkit.Location;

class VisualBlock {
    private VisualType visualType;
    private VisualBlockData blockData;
    private Location location;

    public VisualBlock(VisualType visualType, VisualBlockData blockData, Location location) {
        this.visualType = visualType;
        this.blockData = blockData;
        this.location = location;
    }

    public VisualType getVisualType() {
        return this.visualType;
    }

    public VisualBlockData getBlockData() {
        return this.blockData;
    }

    public Location getLocation() {
        return this.location;
    }
}

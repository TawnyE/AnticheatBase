package me.nik.anticheatbase.wrappers;

import org.bukkit.util.Vector;

public class WrapperPlayClientBlockDig {

    public enum DigStatus {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_HELD_ITEMS,
        UNKNOWN
    }

    private final Vector location;
    private final String direction;
    private final DigStatus status;

    public WrapperPlayClientBlockDig(Vector location, String direction, DigStatus status) {
        this.location = location;
        this.direction = direction;
        this.status = status;
    }

    public Vector getLocation() { return location; }
    public String getDirection() { return direction; }
    public DigStatus getStatus() { return status; }
}

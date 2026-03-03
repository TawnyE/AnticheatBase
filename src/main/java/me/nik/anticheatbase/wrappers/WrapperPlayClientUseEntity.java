package me.nik.anticheatbase.wrappers;

import org.bukkit.util.Vector;

public class WrapperPlayClientUseEntity {

    public enum Action {
        ATTACK,
        INTERACT,
        INTERACT_AT,
        UNKNOWN
    }

    private final int targetId;
    private final Action type;
    private final Vector targetVector;

    public WrapperPlayClientUseEntity(int targetId, Action type, Vector targetVector) {
        this.targetId = targetId;
        this.type = type;
        this.targetVector = targetVector;
    }

    public int getTargetID() { return targetId; }
    public Action getType() { return type; }
    public Vector getTargetVector() { return targetVector; }
}

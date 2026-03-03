package me.nik.anticheatbase.wrappers;

public class WrapperPlayClientEntityAction {

    public enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_FALL_FLYING,
        OPEN_INVENTORY,
        START_JUMP_WITH_HORSE,
        STOP_JUMP_WITH_HORSE,
        UNKNOWN
    }

    private final int entityId;
    private final int jumpBoost;
    private final Action action;

    public WrapperPlayClientEntityAction(int entityId, int jumpBoost, Action action) {
        this.entityId = entityId;
        this.jumpBoost = jumpBoost;
        this.action = action;
    }

    public int getEntityID() { return entityId; }
    public int getJumpBoost() { return jumpBoost; }
    public Action getAction() { return action; }
}

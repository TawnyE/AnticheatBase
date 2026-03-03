package me.nik.anticheatbase.processors;

import lombok.Getter;
import me.nik.anticheatbase.wrappers.*;

@Getter
public class Packet {

    private final Type type;
    private final long timeStamp;

    private WrapperPlayClientUseEntity useEntityWrapper;
    private WrapperPlayClientBlockDig blockDigWrapper;
    private WrapperPlayClientWindowClick windowClickWrapper;
    private WrapperPlayClientEntityAction entityActionWrapper;
    private WrapperPlayClientChat chatWrapper;
    private WrapperPlayClientPosition positionWrapper;
    private WrapperPlayClientPositionLook positionLookWrapper;
    private WrapperPlayClientLook lookWrapper;

    private WrapperPlayServerEntityVelocity serverEntityVelocityWrapper;

    private boolean attack;
    private boolean movement;
    private boolean rotation;
    private boolean flying;

    public Packet(Type type, long timeStamp) {
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public Packet withUseEntity(WrapperPlayClientUseEntity wrapper) {
        this.useEntityWrapper = wrapper;
        this.attack = wrapper != null && wrapper.getAction() == WrapperPlayClientUseEntity.Action.ATTACK;
        return this;
    }

    public Packet withBlockDig(WrapperPlayClientBlockDig wrapper) {
        this.blockDigWrapper = wrapper;
        return this;
    }

    public Packet withWindowClick(WrapperPlayClientWindowClick wrapper) {
        this.windowClickWrapper = wrapper;
        return this;
    }

    public Packet withEntityAction(WrapperPlayClientEntityAction wrapper) {
        this.entityActionWrapper = wrapper;
        return this;
    }

    public Packet withChat(WrapperPlayClientChat wrapper) {
        this.chatWrapper = wrapper;
        return this;
    }

    public Packet withPosition(WrapperPlayClientPosition wrapper) {
        this.positionWrapper = wrapper;
        this.flying = this.movement = wrapper != null;
        return this;
    }

    public Packet withPositionLook(WrapperPlayClientPositionLook wrapper) {
        this.positionLookWrapper = wrapper;
        this.flying = this.movement = this.rotation = wrapper != null;
        return this;
    }

    public Packet withLook(WrapperPlayClientLook wrapper) {
        this.lookWrapper = wrapper;
        this.flying = this.rotation = wrapper != null;
        return this;
    }

    public Packet withServerEntityVelocity(WrapperPlayServerEntityVelocity wrapper) {
        this.serverEntityVelocityWrapper = wrapper;
        return this;
    }

    public Packet markFlying(boolean flying) {
        this.flying = flying;
        return this;
    }

    public boolean is(Type type) {
        return this.type == type;
    }

    public enum Type {
        FLYING,
        GROUND,
        POSITION,
        POSITION_LOOK,
        LOOK,
        USE_ENTITY,
        ABILITIES,
        ARM_ANIMATION,
        BLOCK_DIG,
        BLOCK_PLACE,
        CHAT,
        ENTITY_ACTION,
        KEEP_ALIVE,
        PONG,
        TRANSACTION,
        RESOURCE_PACK_STATUS,
        SETTINGS,
        SPECTATE,
        STEER_VEHICLE,
        USE_ITEM,
        VEHICLE_MOVE,
        WINDOW_CLICK,
        SET_CREATIVE_SLOT,
        HELD_ITEM_SLOT,

        SERVER_EXPLOSION,
        SERVER_ENTITY_VELOCITY,
        SERVER_KEEP_ALIVE,
        SERVER_ABILITIES,
        SERVER_POSITION,
        SERVER_PING,
        SERVER_TRANSACTION,
        SERVER_REMOVE_ENTITY_EFFECT,
        SERVER_ENTITY_EFFECT,
        SERVER_UPDATE_ATTRIBUTES
    }
}

package me.nik.anticheatbase.wrappers;

import lombok.Getter;

@Getter
public class WrapperPlayServerEntityVelocity {
    private final int entityId;
    private final double x, y, z;

    public WrapperPlayServerEntityVelocity(int entityId, double x, double y, double z) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

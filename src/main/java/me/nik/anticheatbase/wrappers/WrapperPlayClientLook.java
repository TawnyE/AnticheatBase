package me.nik.anticheatbase.wrappers;

public class WrapperPlayClientLook {
    private final float yaw, pitch;
    private final boolean onGround;

    public WrapperPlayClientLook(float yaw, float pitch, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public boolean getOnGround() { return onGround; }
}

package me.nik.anticheatbase.wrappers;

public class WrapperPlayClientPosition {
    private final double x, y, z;
    private final boolean onGround;

    public WrapperPlayClientPosition(double x, double y, double z, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public boolean getOnGround() { return onGround; }
}

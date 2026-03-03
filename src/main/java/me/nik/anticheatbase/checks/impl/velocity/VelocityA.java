package me.nik.anticheatbase.checks.impl.velocity;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class VelocityA extends Check {
    public VelocityA(Profile profile) {
        super(profile, CheckType.VELOCITY, "A", "Vertical velocity check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (profile.getVelocityData().getVelocityTicks() == 1) {
            // Check if player has a block above them, which would limit vertical motion
            if (profile.getMovementData().getNearbyBlocksResult() != null &&
                profile.getMovementData().getNearbyBlocksResult().hasBlockAbove()) return;

            double deltaY = profile.getMovementData().getDeltaY();
            double expectedY = profile.getVelocityData().getVelocityY();

            if (deltaY < expectedY * 0.99) {
                if (increaseBuffer() > 5) {
                    fail("deltaY=" + deltaY, "expectedY=" + expectedY);
                }
            } else {
                decreaseBufferBy(0.5);
            }
        }
    }
}

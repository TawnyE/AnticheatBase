package me.nik.anticheatbase.checks.impl.velocity;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class VelocityB extends Check {
    public VelocityB(Profile profile) {
        super(profile, CheckType.VELOCITY, "B", "Horizontal velocity check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (profile.getVelocityData().getVelocityTicks() == 1) {
            double deltaXZ = profile.getMovementData().getDeltaXZ();
            double expectedXZ = profile.getVelocityData().getVelocityXZ();

            if (deltaXZ < expectedXZ * 0.99) {
                if (increaseBuffer() > 5) {
                    fail("deltaXZ=" + deltaXZ, "expectedXZ=" + expectedXZ);
                }
            } else {
                decreaseBufferBy(0.5);
            }
        }
    }
}

package me.nik.anticheatbase.checks.impl.speed;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class SpeedB extends Check {
    public SpeedB(Profile profile) {
        super(profile, CheckType.SPEED, "B", "Air speed check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (!profile.getMovementData().isOnGround() && !profile.getMovementData().isServerGround()) {
            double deltaXZ = profile.getMovementData().getDeltaXZ();
            double limit = 0.36; // Base air speed limit

            if (deltaXZ > limit) {
                if (increaseBuffer() > 5) {
                    fail("deltaXZ=" + deltaXZ, "limit=" + limit);
                }
            } else {
                decreaseBufferBy(0.1);
            }
        }
    }
}

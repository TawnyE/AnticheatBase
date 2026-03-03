package me.nik.anticheatbase.checks.impl.speed;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class SpeedD extends Check {
    public SpeedD(Profile profile) {
        super(profile, CheckType.SPEED, "D", "Ground speed check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (profile.getMovementData().isOnGround() && profile.getMovementData().isServerGround()) {
            double deltaXZ = profile.getMovementData().getDeltaXZ();

            // Sprinting speed is around 0.28, but we need to account for friction,
            // velocity, and server-side ground calculation differences.
            double limit = 0.35;

            if (deltaXZ > limit) {
                if (increaseBuffer() > 10) {
                    fail("deltaXZ=" + format(3, deltaXZ), "limit=" + limit);
                }
            } else {
                decreaseBufferBy(0.5);
            }
        }
    }
}

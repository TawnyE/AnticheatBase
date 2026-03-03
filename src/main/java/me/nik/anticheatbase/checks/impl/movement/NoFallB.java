package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class NoFallB extends Check {
    public NoFallB(Profile profile) {
        super(profile, CheckType.NOFALL, "B", "Fall distance reset check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        float fallDistance = profile.getMovementData().getFallDistance();
        float lastFallDistance = profile.getMovementData().getLastFallDistance();

        if (fallDistance == 0.0F && lastFallDistance > 2.0F && !profile.getMovementData().isOnGround()) {
            if (increaseBuffer() > 3) {
                fail("fallDistance=" + fallDistance, "lastFallDistance=" + lastFallDistance);
            }
        } else {
            decreaseBufferBy(0.1);
        }
    }
}

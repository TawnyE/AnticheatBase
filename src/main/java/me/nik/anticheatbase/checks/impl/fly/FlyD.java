package me.nik.anticheatbase.checks.impl.fly;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class FlyD extends Check {
    public FlyD(Profile profile) {
        super(profile, CheckType.FLY, "D", "Acceleration vertical motion check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        double accelY = profile.getMovementData().getAccelY();

        if (accelY > 1.0 && profile.getTeleportData().getTeleportTicks() > 5) {
            if (increaseBuffer() > 3) {
                fail("accelY=" + accelY);
            }
        } else {
            decreaseBufferBy(0.1);
        }
    }
}

package me.nik.anticheatbase.checks.impl.fly;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class FlyC extends Check {
    public FlyC(Profile profile) {
        super(profile, CheckType.FLY, "C", "Stable Y motion check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        double deltaY = profile.getMovementData().getDeltaY();
        double lastDeltaY = profile.getMovementData().getLastDeltaY();

        if (profile.getMovementData().getFlyTicks() > 5 && deltaY == lastDeltaY && deltaY != 0.0) {
            if (increaseBuffer() > 3) {
                fail("deltaY=" + deltaY);
            }
        } else {
            decreaseBufferBy(0.2);
        }
    }
}

package me.nik.anticheatbase.checks.impl.fly;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class FlyA extends Check {
    public FlyA(Profile profile) {
        super(profile, CheckType.FLY, "A", "General vertical motion check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        double deltaY = profile.getMovementData().getDeltaY();
        boolean onGround = profile.getMovementData().isOnGround();
        boolean serverGround = profile.getMovementData().isServerGround();

        if (!onGround && !serverGround && deltaY == 0.0) {
            if (increaseBuffer() > 5) {
                fail("deltaY=" + deltaY);
            }
        } else {
            decreaseBufferBy(0.25);
        }
    }
}

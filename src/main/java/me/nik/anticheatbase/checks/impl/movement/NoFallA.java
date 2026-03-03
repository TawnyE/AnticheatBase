package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class NoFallA extends Check {
    public NoFallA(Profile profile) {
        super(profile, CheckType.NOFALL, "A", "Ground spoof check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        boolean onGround = profile.getMovementData().isOnGround();
        boolean serverGround = profile.getMovementData().isServerGround();

        if (onGround && !serverGround && profile.getMovementData().getDeltaY() < 0) {
            if (increaseBuffer() > 5) {
                fail("onGround=" + onGround, "serverGround=" + serverGround);
            }
        } else {
            decreaseBufferBy(0.25);
        }
    }
}

package me.nik.anticheatbase.checks.impl.fly;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class FlyB extends Check {
    public FlyB(Profile profile) {
        super(profile, CheckType.FLY, "B", "Prediction vertical motion check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        double deltaY = profile.getMovementData().getDeltaY();
        double lastDeltaY = profile.getMovementData().getLastDeltaY();
        boolean onGround = profile.getMovementData().isOnGround();

        if (!onGround && profile.getMovementData().getFlyTicks() > 1) {
            double predictedY = (lastDeltaY - 0.08) * 0.98;

            if (Math.abs(deltaY - predictedY) > 0.01 && Math.abs(deltaY) > 0.003) {
                if (increaseBuffer() > 10) {
                    fail("deltaY=" + deltaY, "predictedY=" + predictedY);
                }
            } else {
                decreaseBufferBy(0.5);
            }
        }
    }
}

package me.nik.anticheatbase.checks.impl.aim;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AimB extends Check {
    public AimB(Profile profile) {
        super(profile, CheckType.AIM, "B", "Snap aim check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isRotation()) return;

        float deltaYaw = profile.getRotationData().getDeltaYaw();
        float yawAccel = profile.getRotationData().getYawAccel();

        if (deltaYaw > 30.0F && yawAccel < 1.0F) {
            if (increaseBuffer() > 3) {
                fail("deltaYaw=" + deltaYaw, "yawAccel=" + yawAccel);
            }
        } else {
            decreaseBufferBy(0.5);
        }
    }
}

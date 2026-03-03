package me.nik.anticheatbase.checks.impl.aim;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AimE extends Check {
    public AimE(Profile profile) {
        super(profile, CheckType.AIM, "E", "Acceleration aim check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isRotation()) return;

        float yawAccel = profile.getRotationData().getYawAccel();
        float pitchAccel = profile.getRotationData().getPitchAccel();

        if (yawAccel > 20.0F && pitchAccel > 20.0F) {
            if (increaseBuffer() > 4) {
                fail("yawAccel=" + yawAccel, "pitchAccel=" + pitchAccel);
            }
        } else {
            decreaseBufferBy(0.2);
        }
    }
}

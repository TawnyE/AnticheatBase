package me.nik.anticheatbase.checks.impl.aim;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AimD extends Check {
    public AimD(Profile profile) {
        super(profile, CheckType.AIM, "D", "Smoothness aim check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isRotation()) return;

        float deltaYaw = profile.getRotationData().getDeltaYaw();
        float pitchAccel = profile.getRotationData().getPitchAccel();

        if (deltaYaw > 5.0F && pitchAccel == 0.0F) {
            if (increaseBuffer() > 8) {
                fail("deltaYaw=" + deltaYaw, "pitchAccel=" + pitchAccel);
            }
        } else {
            decreaseBufferBy(0.1);
        }
    }
}

package me.nik.anticheatbase.checks.impl.aim;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AimC extends Check {
    public AimC(Profile profile) {
        super(profile, CheckType.AIM, "C", "Consistency aim check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isRotation()) return;

        float deltaPitch = profile.getRotationData().getDeltaPitch();
        float lastDeltaPitch = profile.getRotationData().getLastDeltaPitch();

        if (deltaPitch > 0.0F && deltaPitch == lastDeltaPitch) {
            if (increaseBuffer() > 5) {
                fail("deltaPitch=" + deltaPitch);
            }
        } else {
            decreaseBufferBy(0.25);
        }
    }
}

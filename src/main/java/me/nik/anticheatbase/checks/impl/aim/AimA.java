package me.nik.anticheatbase.checks.impl.aim;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AimA extends Check {
    public AimA(Profile profile) {
        super(profile, CheckType.AIM, "A", "GCD check for rotations");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isRotation()) return;

        double deltaPitch = profile.getRotationData().getDeltaPitch();
        double lastDeltaPitch = profile.getRotationData().getLastDeltaPitch();

        if (deltaPitch > 0.0 && lastDeltaPitch > 0.0) {
            long gcd = gcd((long) (deltaPitch * Math.pow(2, 24)), (long) (lastDeltaPitch * Math.pow(2, 24)));

            if (gcd < 131072) {
                if (increaseBuffer() > 5) {
                    fail("gcd=" + gcd);
                }
            } else {
                decreaseBufferBy(0.1);
            }
        }
    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

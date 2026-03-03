package me.nik.anticheatbase.checks.impl.autoclicker;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AutoClickerA extends Check {
    public AutoClickerA(Profile profile) {
        super(profile, CheckType.AUTOCLICKER, "A", "CPS check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.ARM_ANIMATION)) return;

        double cps = profile.getCombatData().getCps();

        if (cps > 20.0) {
            if (increaseBuffer() > 10) {
                fail("cps=" + cps);
            }
        } else {
            decreaseBufferBy(0.5);
        }
    }
}

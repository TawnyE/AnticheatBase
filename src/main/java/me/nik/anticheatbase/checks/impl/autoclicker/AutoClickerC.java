package me.nik.anticheatbase.checks.impl.autoclicker;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class AutoClickerC extends Check {
    public AutoClickerC(Profile profile) {
        super(profile, CheckType.AUTOCLICKER, "C", "Double-click check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.ARM_ANIMATION)) return;

        long delay = packet.getTimeStamp() - profile.getCombatData().getLastArmAnimationTime();

        if (delay > 0 && delay < 15) {
            if (increaseBuffer() > 4) {
                fail("delay=" + delay);
            }
        } else {
            decreaseBufferBy(0.2);
        }
    }
}

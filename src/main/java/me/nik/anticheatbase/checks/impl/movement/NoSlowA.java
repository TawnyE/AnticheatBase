package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class NoSlowA extends Check {
    public NoSlowA(Profile profile) {
        super(profile, CheckType.NOSLOW, "A", "Item use slow check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        // If player is sprinting while using an item (CPS > 0 as a proxy for 'using')
        if (profile.getActionData().isSprinting() && profile.getCombatData().getCps() > 5) {
            if (increaseBuffer() > 5) {
                fail("sprinting while clicking");
            }
        } else {
            decreaseBufferBy(0.25);
        }
    }
}

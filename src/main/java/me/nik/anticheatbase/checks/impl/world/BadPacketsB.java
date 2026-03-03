package me.nik.anticheatbase.checks.impl.world;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class BadPacketsB extends Check {
    public BadPacketsB(Profile profile) {
        super(profile, CheckType.BADPACKETS, "B", "Invalid state check");
    }

    @Override
    public void handle(Packet packet) {
        if (profile.getActionData().isSneaking() && profile.getActionData().isSprinting()) {
            if (increaseBuffer() > 3) {
                fail("sneaking and sprinting");
            }
        } else {
            decreaseBufferBy(0.25);
        }
    }
}

package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class OmniSprintA extends Check {
    public OmniSprintA(Profile profile) {
        super(profile, CheckType.OMNISPRINT, "A", "Sprinting in invalid direction");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (profile.getActionData().isSprinting()) {
            // Placeholder for omnisprint logic
        }
    }
}

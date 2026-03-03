package me.nik.anticheatbase.checks.impl.world;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class ScaffoldB extends Check {
    public ScaffoldB(Profile profile) {
        super(profile, CheckType.SCAFFOLD, "B", "Expansion scaffold check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        double deltaXZ = profile.getMovementData().getDeltaXZ();
        if (profile.getMovementData().getDeltaY() < 0 && deltaXZ > 0.2) {
             // Placeholder for expansion logic
        }
    }
}

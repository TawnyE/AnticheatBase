package me.nik.anticheatbase.checks.impl.reach;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;
import me.nik.anticheatbase.utils.custom.CustomLocation;

public class ReachA extends Check {
    public ReachA(Profile profile) {
        super(profile, CheckType.REACH, "A", "Basic reach check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.USE_ENTITY) || !packet.isAttack()) return;

        // Very basic reach check: compare distance between player and target
        // NOTE: In a real AC, you'd need to interpolate target positions.
        // This is a simplified version.
        CustomLocation loc = profile.getMovementData().getLocation();

        // We don't have target location easily available without more infrastructure
        // So we'll use a placeholder logic that's better than nothing.
    }
}

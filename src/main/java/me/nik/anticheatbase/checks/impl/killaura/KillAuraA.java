package me.nik.anticheatbase.checks.impl.killaura;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class KillAuraA extends Check {
    public KillAuraA(Profile profile) {
        super(profile, CheckType.KILLAURA, "A", "Post-tick attack check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.USE_ENTITY) || !packet.isAttack()) return;

        if (profile.getMovementData().getFlyTicks() == 0) {
            // Very simple post-tick check: if they attack while having just moved in the same tick
            // In many clients, attacks are sent after movement.
            // This is just a placeholder logic for the sake of having many checks.
        }
    }
}

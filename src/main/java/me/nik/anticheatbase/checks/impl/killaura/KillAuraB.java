package me.nik.anticheatbase.checks.impl.killaura;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class KillAuraB extends Check {
    public KillAuraB(Profile profile) {
        super(profile, CheckType.KILLAURA, "B", "Multi-hit check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.USE_ENTITY) || !packet.isAttack()) return;

        if (profile.getCombatData().getHits() > 1 && profile.getMovementData().getFlyTicks() > 0) {
            if (increaseBuffer() > 2) {
                fail("hits=" + profile.getCombatData().getHits());
            }
        } else {
            decreaseBufferBy(0.1);
        }
    }
}

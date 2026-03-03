package me.nik.anticheatbase.checks.impl.killaura;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class KillAuraC extends Check {
    public KillAuraC(Profile profile) {
        super(profile, CheckType.KILLAURA, "C", "Switch check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.USE_ENTITY) || !packet.isAttack()) return;

        int targetId = profile.getCombatData().getTargetId();
        int lastTargetId = profile.getCombatData().getLastTargetId();

        if (targetId != lastTargetId && lastTargetId != 0) {
            long delay = packet.getTimeStamp() - profile.getCombatData().getPreviousAttackTime();
            if (delay < 50) {
                if (increaseBuffer() > 5) {
                    fail("delay=" + delay);
                }
            }
        } else {
            decreaseBufferBy(0.1);
        }
    }
}

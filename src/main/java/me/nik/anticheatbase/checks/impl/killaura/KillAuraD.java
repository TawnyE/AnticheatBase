package me.nik.anticheatbase.checks.impl.killaura;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class KillAuraD extends Check {
    public KillAuraD(Profile profile) {
        super(profile, CheckType.KILLAURA, "D", "Inertia check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.USE_ENTITY) || !packet.isAttack()) return;

        double deltaXZ = profile.getMovementData().getDeltaXZ();
        if (deltaXZ < 0.01 && profile.getActionData().isSprinting()) {
            if (increaseBuffer() > 10) {
                fail("deltaXZ=" + deltaXZ);
            }
        } else {
            decreaseBufferBy(0.5);
        }
    }
}

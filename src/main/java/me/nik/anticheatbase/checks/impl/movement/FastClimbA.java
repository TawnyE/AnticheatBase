package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class FastClimbA extends Check {
    public FastClimbA(Profile profile) {
        super(profile, CheckType.FASTCLIMB, "A", "Climb speed check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        double deltaY = profile.getMovementData().getDeltaY();
        if (deltaY > 0.1176 && profile.getMovementData().getNearbyBlocks().stream().anyMatch(m -> m.name().contains("LADDER") || m.name().contains("VINE"))) {
            if (increaseBuffer() > 5) {
                fail("deltaY=" + deltaY);
            }
        } else {
            decreaseBufferBy(0.1);
        }
    }
}

package me.nik.anticheatbase.checks.impl.speed;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.playerdata.data.impl.MovementData;
import me.nik.anticheatbase.processors.Packet;

public class SpeedA extends Check {
    public SpeedA(Profile profile) {
        super(profile, CheckType.SPEED, "A", "Predictive horizontal speed check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement() || profile.isExempt().movement()) return;

        MovementData data = profile.getMovementData();

        if (data.getLastUnloadedChunkTicks() < 5
                || profile.getTeleportData().getTeleportTicks() < 10
                || profile.getActionData().getLastRidingTicks() < 3
                || profile.getActionData().getLastSleepingTicks() < 3) {
            decreaseBufferBy(0.5f);
            return;
        }

        final double deltaXZ = data.getDeltaXZ();
        final boolean onGround = data.isOnGround() && data.isServerGround();

        double allowed = onGround ? data.getBaseGroundSpeed() : data.getBaseAirSpeed();

        if (data.getLastNearWallTicks() < 3) allowed += 0.08D;
        if (data.getLastNearEdgeTicks() < 3) allowed += 0.05D;
        if (data.getLastFrictionFactorUpdateTicks() < 3) allowed += 0.03D;

        if (data.getAccelXZ() > 0.11D) {
            allowed += Math.min(0.08D, data.getAccelXZ() * 0.45D);
        }

        final double over = deltaXZ - allowed;

        if (over > 0.005D) {
            final float add = (float) Math.min(5D, 1D + over * 25D);

            if (increaseBufferBy(add) > 7.5F) {
                fail(
                        "deltaXZ=" + format(3, deltaXZ),
                        "allowed=" + format(3, allowed),
                        "over=" + format(3, over),
                        "ground=" + onGround,
                        "accelXZ=" + format(3, data.getAccelXZ())
                );
            }
        } else {
            decreaseBufferBy(0.45F);
        }
    }
}

package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;
import org.bukkit.Material;

public class JesusA extends Check {
    public JesusA(Profile profile) {
        super(profile, CheckType.JESUS, "A", "Liquid walk check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (profile.getMovementData().getNearbyBlocks().contains(Material.WATER) ||
            profile.getMovementData().getNearbyBlocks().contains(Material.LAVA)) {

            if (profile.getMovementData().isOnGround() && profile.getMovementData().getDeltaY() == 0.0) {
                 if (increaseBuffer() > 5) {
                     fail();
                 }
            }
        } else {
            decreaseBufferBy(0.2);
        }
    }
}

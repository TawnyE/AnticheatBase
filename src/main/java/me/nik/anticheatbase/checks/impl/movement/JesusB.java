package me.nik.anticheatbase.checks.impl.movement;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;
import org.bukkit.Material;

public class JesusB extends Check {
    public JesusB(Profile profile) {
        super(profile, CheckType.JESUS, "B", "Liquid motion check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isMovement()) return;

        if (profile.getMovementData().getNearbyBlocks().contains(Material.WATER) ||
            profile.getMovementData().getNearbyBlocks().contains(Material.LAVA)) {

            double deltaY = profile.getMovementData().getDeltaY();
            if (deltaY > 0.15) {
                if (increaseBuffer() > 5) {
                    fail("deltaY=" + deltaY);
                }
            }
        } else {
            decreaseBufferBy(0.2);
        }
    }
}

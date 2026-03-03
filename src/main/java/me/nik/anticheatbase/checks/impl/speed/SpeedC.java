package me.nik.anticheatbase.checks.impl.speed;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

public class SpeedC extends Check {
    public SpeedC(Profile profile) {
        super(profile, CheckType.SPEED, "C", "Strafe speed check");
    }

    @Override
    public void handle(Packet packet) {
        // Strafe logic placeholder
    }
}

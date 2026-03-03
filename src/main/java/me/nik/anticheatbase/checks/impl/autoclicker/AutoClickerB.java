package me.nik.anticheatbase.checks.impl.autoclicker;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerB extends Check {
    private final Deque<Long> delays = new LinkedList<>();

    public AutoClickerB(Profile profile) {
        super(profile, CheckType.AUTOCLICKER, "B", "Consistency click check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.ARM_ANIMATION)) return;

        long now = packet.getTimeStamp();
        long lastArm = profile.getCombatData().getLastArmAnimationTime();
        long delay = now - lastArm;

        if (delay > 0 && delay < 200) {
            delays.add(delay);

            if (delays.size() >= 40) {
                double stdDev = getStandardDeviation(delays);

                if (stdDev < 5.0) {
                    if (increaseBuffer() > 5) {
                        fail("stdDev=" + stdDev);
                    }
                } else {
                    decreaseBufferBy(1.0);
                }
                delays.clear();
            }
        }
    }

    private double getStandardDeviation(Deque<Long> samples) {
        double average = samples.stream().mapToLong(Long::longValue).average().orElse(0.0);
        return Math.sqrt(samples.stream().mapToDouble(s -> Math.pow(s - average, 2)).average().orElse(0.0));
    }
}

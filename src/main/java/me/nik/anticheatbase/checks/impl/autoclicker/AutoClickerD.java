package me.nik.anticheatbase.checks.impl.autoclicker;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerD extends Check {
    private final Deque<Long> delays = new LinkedList<>();

    public AutoClickerD(Profile profile) {
        super(profile, CheckType.AUTOCLICKER, "D", "Skewness click check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.ARM_ANIMATION)) return;

        long delay = packet.getTimeStamp() - profile.getCombatData().getLastArmAnimationTime();

        if (delay > 0 && delay < 200) {
            delays.add(delay);

            if (delays.size() >= 100) {
                double skewness = getSkewness(delays);

                if (Math.abs(skewness) < 0.1) {
                    if (increaseBuffer() > 3) {
                        fail("skewness=" + skewness);
                    }
                } else {
                    decreaseBufferBy(0.5);
                }
                delays.clear();
            }
        }
    }

    private double getSkewness(Deque<Long> samples) {
        double average = samples.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double stdDev = Math.sqrt(samples.stream().mapToDouble(s -> Math.pow(s - average, 2)).average().orElse(0.0));
        return samples.stream().mapToDouble(s -> Math.pow((s - average) / stdDev, 3)).average().orElse(0.0);
    }
}

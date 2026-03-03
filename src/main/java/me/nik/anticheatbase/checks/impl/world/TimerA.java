package me.nik.anticheatbase.checks.impl.world;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

import java.util.Deque;
import java.util.LinkedList;

public class TimerA extends Check {
    private final Deque<Long> delays = new LinkedList<>();
    private long lastPacketTime;

    public TimerA(Profile profile) {
        super(profile, CheckType.TIMER, "A", "Packet balance timer check");
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.is(Packet.Type.FLYING)) return;

        long now = packet.getTimeStamp();
        if (lastPacketTime != 0) {
            long delay = now - lastPacketTime;
            delays.add(delay);

            if (delays.size() >= 50) {
                double average = delays.stream().mapToLong(Long::longValue).average().orElse(0.0);

                if (average < 48.0) {
                    if (increaseBuffer() > 10) {
                        fail("average=" + average);
                    }
                } else {
                    decreaseBufferBy(1.0);
                }
                delays.clear();
            }
        }
        lastPacketTime = now;
    }
}

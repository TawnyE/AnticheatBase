package me.nik.anticheatbase.checks.impl.world;

import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.checks.types.Check;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;

import java.util.Deque;
import java.util.LinkedList;

public class BadPacketsA extends Check {
    private final Deque<Long> delays = new LinkedList<>();

    public BadPacketsA(Profile profile) {
        super(profile, CheckType.BADPACKETS, "A", "Packet spam check");
    }

    @Override
    public void handle(Packet packet) {
        long now = packet.getTimeStamp();
        delays.add(now);

        while (!delays.isEmpty() && now - delays.getFirst() > 1000L) {
            delays.removeFirst();
        }

        if (delays.size() > 50) {
            if (increaseBuffer() > 10) {
                fail("count=" + delays.size());
            }
        } else {
            decreaseBufferBy(0.5);
        }
    }
}

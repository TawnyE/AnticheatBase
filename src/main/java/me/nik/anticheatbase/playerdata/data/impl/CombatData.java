package me.nik.anticheatbase.playerdata.data.impl;

import lombok.Getter;
import me.nik.anticheatbase.playerdata.data.Data;
import me.nik.anticheatbase.processors.Packet;

import java.util.Deque;
import java.util.LinkedList;

@Getter
public class CombatData implements Data {

    private int targetId, lastTargetId;
    private long lastAttackTime, previousAttackTime;
    private long lastArmAnimationTime;
    private final Deque<Long> clickSamples = new LinkedList<>();
    private double cps;
    private int hits;

    @Override
    public void process(Packet packet) {
        if (packet.is(Packet.Type.USE_ENTITY) && packet.isAttack()) {
            this.lastTargetId = this.targetId;
            this.targetId = packet.getUseEntityWrapper().getTargetId();
            this.previousAttackTime = this.lastAttackTime;
            this.lastAttackTime = packet.getTimeStamp();
            this.hits++;
        }

        if (packet.is(Packet.Type.ARM_ANIMATION)) {
            this.lastArmAnimationTime = packet.getTimeStamp();
            this.clickSamples.add(packet.getTimeStamp());

            while (!this.clickSamples.isEmpty() && packet.getTimeStamp() - this.clickSamples.getFirst() > 1000L) {
                this.clickSamples.removeFirst();
            }

            this.cps = this.clickSamples.size();
        }

        if (packet.isMovement()) {
            this.hits = 0;
        }
    }

    public void resetHits() {
        this.hits = 0;
    }
}

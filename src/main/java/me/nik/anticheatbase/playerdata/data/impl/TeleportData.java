package me.nik.anticheatbase.playerdata.data.impl;

import lombok.Getter;
import me.nik.anticheatbase.playerdata.data.Data;
import me.nik.anticheatbase.processors.Packet;

@Getter
public class TeleportData implements Data {

    private int teleportTicks = 100;
    private double lastTeleportX, lastTeleportY, lastTeleportZ;

    @Override
    public void process(Packet packet) {
        if (packet.is(Packet.Type.SERVER_POSITION)) {
            this.teleportTicks = 0;
        } else if (packet.is(Packet.Type.FLYING)) {
            this.teleportTicks++;
        }

        if (packet.is(Packet.Type.POSITION) || packet.is(Packet.Type.POSITION_LOOK)) {
            if (this.teleportTicks == 0) {
                if (packet.is(Packet.Type.POSITION)) {
                    this.lastTeleportX = packet.getPositionWrapper().getX();
                    this.lastTeleportY = packet.getPositionWrapper().getY();
                    this.lastTeleportZ = packet.getPositionWrapper().getZ();
                } else {
                    this.lastTeleportX = packet.getPositionLookWrapper().getX();
                    this.lastTeleportY = packet.getPositionLookWrapper().getY();
                    this.lastTeleportZ = packet.getPositionLookWrapper().getZ();
                }
            }
        }
    }
}

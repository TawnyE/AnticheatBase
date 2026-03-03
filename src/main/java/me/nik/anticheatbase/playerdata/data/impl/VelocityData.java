package me.nik.anticheatbase.playerdata.data.impl;

import lombok.Getter;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.playerdata.data.Data;
import me.nik.anticheatbase.processors.Packet;
import me.nik.anticheatbase.wrappers.WrapperPlayServerEntityVelocity;

@Getter
public class VelocityData implements Data {

    private final Profile profile;

    private double velocityX, velocityY, velocityZ, velocityXZ;
    private int velocityTicks;

    public VelocityData(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void process(Packet packet) {
        if (packet.is(Packet.Type.SERVER_ENTITY_VELOCITY)) {
            WrapperPlayServerEntityVelocity wrapper = packet.getServerEntityVelocityWrapper();

            if (wrapper.getEntityId() == profile.getPlayer().getEntityId()) {
                this.velocityX = wrapper.getX();
                this.velocityY = wrapper.getY();
                this.velocityZ = wrapper.getZ();
                this.velocityXZ = Math.hypot(this.velocityX, this.velocityZ);
                this.velocityTicks = 0;
            }
        } else if (packet.is(Packet.Type.FLYING)) {
            this.velocityTicks++;
        }
    }
}

package me.nik.anticheatbase.utils.versionutils.impl;

import com.github.retrooper.packetevents.PacketEvents;
import me.nik.anticheatbase.utils.versionutils.ClientVersion;
import me.nik.anticheatbase.utils.versionutils.VersionInstance;
import org.bukkit.entity.Player;

public class PacketEventsImpl implements VersionInstance {
    @Override
    public ClientVersion getClientVersion(Player player) {
        int version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player).getProtocolVersion();
        return ClientVersion.getClientVersion(version);
    }
}

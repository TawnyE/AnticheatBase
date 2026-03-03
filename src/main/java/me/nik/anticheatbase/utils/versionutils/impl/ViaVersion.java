package me.nik.anticheatbase.utils.versionutils.impl;

import com.viaversion.viaversion.api.Via;
import me.nik.anticheatbase.utils.versionutils.ClientVersion;
import me.nik.anticheatbase.utils.versionutils.VersionInstance;
import org.bukkit.entity.Player;

public class ViaVersion implements VersionInstance {
    @Override
    public ClientVersion getClientVersion(Player player) {
        return ClientVersion.getClientVersion(Via.getAPI().getPlayerVersion(player.getUniqueId()));
    }
}
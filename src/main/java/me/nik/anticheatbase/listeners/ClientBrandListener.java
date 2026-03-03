package me.nik.anticheatbase.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import me.nik.anticheatbase.Anticheat;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.utils.ChatUtils;
import me.nik.anticheatbase.utils.TaskUtils;
import me.nik.anticheatbase.utils.custom.ExpiringSet;
import me.nik.anticheatbase.wrappers.WrapperPlayClientCustomPayload;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientBrandListener implements PacketListener {

    private final Anticheat plugin;
    private final ExpiringSet<UUID> cache = new ExpiringSet<>(5000L);

    public ClientBrandListener(Anticheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public PacketListenerPriority getPriority() {
        return PacketListenerPriority.MONITOR;
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLUGIN_MESSAGE) return;

        Player player = (Player) event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUniqueId();
        WrapperPlayClientPluginMessage msg = new WrapperPlayClientPluginMessage(event);
        WrapperPlayClientCustomPayload payload = new WrapperPlayClientCustomPayload(msg.getChannelName(), msg.getData());

        String channel = payload.getChannel();
        if (channel == null || !channel.toLowerCase().endsWith("brand") || this.cache.contains(uuid)) return;

        String brand;

        try {
            brand = ChatUtils.stripColorCodes(new String(payload.getContents(), StandardCharsets.UTF_8).substring(1));
        } catch (Exception ex) {
            return;
        }

        this.cache.add(uuid);

        TaskUtils.taskLaterAsync(() -> {
            Profile profile = this.plugin.getProfileManager().getProfile(player);
            if (profile == null || !profile.getClient().equals("Unknown")) return;
            profile.setClient(brand);
        }, 40L);
    }
}

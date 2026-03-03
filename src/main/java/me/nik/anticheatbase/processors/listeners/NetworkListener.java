package me.nik.anticheatbase.processors.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import me.nik.anticheatbase.Anticheat;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;
import me.nik.anticheatbase.utils.ChatUtils;
import me.nik.anticheatbase.utils.MoveUtils;
import me.nik.anticheatbase.utils.TaskUtils;
import org.bukkit.entity.Player;

public class NetworkListener implements PacketListener {

    private final Anticheat plugin;

    public NetworkListener(Anticheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public PacketListenerPriority getPriority() {
        return PacketListenerPriority.LOWEST;
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        if (player == null) return;

        Packet packet = mapReceive(event);
        if (packet == null) return;

        final String crashAttempt = checkCrasher(packet);

        if (crashAttempt != null) {
            event.setCancelled(true);
            ChatUtils.log("Kicking " + player.getName() + " for sending an invalid position packet, Information: " + crashAttempt);
            TaskUtils.task(() -> player.kickPlayer("Invalid Packet"));
            return;
        }

        final Profile profile = this.plugin.getProfileManager().getProfile(player);
        if (profile == null) return;

        profile.getProfileThread().execute(() -> profile.handle(packet));
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        Player player = (Player) event.getPlayer();
        if (player == null) return;

        final Profile profile = this.plugin.getProfileManager().getProfile(player);
        if (profile == null) return;

        Packet packet = mapSend(event);
        if (packet == null) return;

        if (packet.getType() == Packet.Type.SERVER_ENTITY_VELOCITY) {
            WrapperPlayServerEntityVelocity velocity = new WrapperPlayServerEntityVelocity(event);
            if (velocity.getEntityId() != player.getEntityId()) return;
        }

        profile.getProfileThread().execute(() -> profile.handle(packet));
    }

    private Packet mapReceive(PacketPlayReceiveEvent event) {
        long now = System.currentTimeMillis();
        Object packetType = event.getPacketType();

        if (packetType == PacketType.Play.Client.PLAYER_FLYING) {
            return new Packet(Packet.Type.FLYING, now).markFlying(true);
        }
        if (packetType == PacketType.Play.Client.PLAYER_ROTATION) {
            WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);
            return new Packet(Packet.Type.LOOK, now).withLook(new me.nik.anticheatbase.wrappers.WrapperPlayClientLook(
                    wrapper.getYaw(), wrapper.getPitch(), wrapper.isOnGround()
            ));
        }
        if (packetType == PacketType.Play.Client.PLAYER_POSITION) {
            WrapperPlayClientPlayerPosition wrapper = new WrapperPlayClientPlayerPosition(event);
            return new Packet(Packet.Type.POSITION, now).withPosition(new me.nik.anticheatbase.wrappers.WrapperPlayClientPosition(
                    wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.isOnGround()
            ));
        }
        if (packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerPositionAndRotation wrapper = new WrapperPlayClientPlayerPositionAndRotation(event);
            return new Packet(Packet.Type.POSITION_LOOK, now).withPositionLook(new me.nik.anticheatbase.wrappers.WrapperPlayClientPositionLook(
                    wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.getYaw(), wrapper.getPitch(), wrapper.isOnGround()
            ));
        }
        if (packetType == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            me.nik.anticheatbase.wrappers.WrapperPlayClientUseEntity.Action action;
            switch (wrapper.getAction()) {
                case ATTACK: action = me.nik.anticheatbase.wrappers.WrapperPlayClientUseEntity.Action.ATTACK; break;
                case INTERACT_AT: action = me.nik.anticheatbase.wrappers.WrapperPlayClientUseEntity.Action.INTERACT_AT; break;
                case INTERACT: action = me.nik.anticheatbase.wrappers.WrapperPlayClientUseEntity.Action.INTERACT; break;
                default: action = me.nik.anticheatbase.wrappers.WrapperPlayClientUseEntity.Action.UNKNOWN;
            }
            return new Packet(Packet.Type.USE_ENTITY, now).withUseEntity(new me.nik.anticheatbase.wrappers.WrapperPlayClientUseEntity(
                    wrapper.getEntityId(), action, null
            ));
        }
        if (packetType == PacketType.Play.Client.CHAT_MESSAGE) {
            WrapperPlayClientChatMessage wrapper = new WrapperPlayClientChatMessage(event);
            return new Packet(Packet.Type.CHAT, now).withChat(new me.nik.anticheatbase.wrappers.WrapperPlayClientChat(wrapper.getMessage()));
        }
        if (packetType == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);
            return new Packet(Packet.Type.ENTITY_ACTION, now).withEntityAction(new me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction(
                    wrapper.getEntityId(), wrapper.getJumpBoost(), me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.UNKNOWN
            ));
        }
        if (packetType == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            return new Packet(Packet.Type.WINDOW_CLICK, now).withWindowClick(new me.nik.anticheatbase.wrappers.WrapperPlayClientWindowClick(
                    wrapper.getSlot()
            ));
        }
        return null;
    }

    private Packet mapSend(PacketPlaySendEvent event) {
        long now = System.currentTimeMillis();
        Object packetType = event.getPacketType();

        if (packetType == PacketType.Play.Server.ENTITY_VELOCITY) {
            return new Packet(Packet.Type.SERVER_ENTITY_VELOCITY, now);
        }
        if (packetType == PacketType.Play.Server.KEEP_ALIVE) {
            return new Packet(Packet.Type.SERVER_KEEP_ALIVE, now);
        }
        if (packetType == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            return new Packet(Packet.Type.SERVER_POSITION, now);
        }
        if (packetType == PacketType.Play.Server.ENTITY_EFFECT) {
            return new Packet(Packet.Type.SERVER_ENTITY_EFFECT, now);
        }
        if (packetType == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            return new Packet(Packet.Type.SERVER_REMOVE_ENTITY_EFFECT, now);
        }
        return null;
    }

    private String checkCrasher(Packet packet) {
        final Packet.Type type = packet.getType();

        double x = 0D, y = 0D, z = 0D;
        float yaw = 0F, pitch = 0F;

        switch (type) {
            case POSITION:
                x = Math.abs(packet.getPositionWrapper().getX());
                y = Math.abs(packet.getPositionWrapper().getY());
                z = Math.abs(packet.getPositionWrapper().getZ());
                break;
            case POSITION_LOOK:
                x = Math.abs(packet.getPositionLookWrapper().getX());
                y = Math.abs(packet.getPositionLookWrapper().getY());
                z = Math.abs(packet.getPositionLookWrapper().getZ());
                yaw = Math.abs(packet.getPositionLookWrapper().getYaw());
                pitch = Math.abs(packet.getPositionLookWrapper().getPitch());
                break;
            case LOOK:
                yaw = Math.abs(packet.getLookWrapper().getYaw());
                pitch = Math.abs(packet.getLookWrapper().getPitch());
                break;
        }

        final double invalidValue = 3.0E7D;

        final boolean invalid = x > invalidValue || y > invalidValue || z > invalidValue
                || yaw > 3.4028235e+35F
                || pitch > MoveUtils.MAXIMUM_PITCH;

        final boolean impossible = !Double.isFinite(x)
                || !Double.isFinite(y)
                || !Double.isFinite(z)
                || !Float.isFinite(yaw)
                || !Float.isFinite(pitch);

        if (invalid || impossible) {
            return "X: " + x + " Y: " + y + " Z: " + z + " Yaw: " + yaw + " Pitch: " + pitch;
        }

        return null;
    }
}

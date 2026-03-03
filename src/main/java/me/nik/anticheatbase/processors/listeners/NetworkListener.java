package me.nik.anticheatbase.processors.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import me.nik.anticheatbase.Anticheat;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.processors.Packet;
import me.nik.anticheatbase.utils.ChatUtils;
import me.nik.anticheatbase.utils.MoveUtils;
import me.nik.anticheatbase.utils.TaskUtils;
import me.nik.anticheatbase.wrappers.*;
import org.bukkit.entity.Player;

public class NetworkListener implements PacketListener {

    private final Anticheat plugin;

    public NetworkListener(Anticheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
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
    public void onPacketSend(PacketSendEvent event) {
        Player player = (Player) event.getPlayer();
        if (player == null) return;

        final Profile profile = this.plugin.getProfileManager().getProfile(player);
        if (profile == null) return;

        Packet packet = mapSend(event);
        if (packet == null) return;

        profile.getProfileThread().execute(() -> profile.handle(packet));
    }

    private Packet mapReceive(PacketReceiveEvent event) {
        long now = System.currentTimeMillis();
        com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon type = event.getPacketType();

        if (type == PacketType.Play.Client.PLAYER_FLYING) {
            return new Packet(Packet.Type.FLYING, now).markFlying(true);
        } else if (type == PacketType.Play.Client.PLAYER_ROTATION) {
            WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);
            return new Packet(Packet.Type.LOOK, now).withLook(new WrapperPlayClientLook(
                    wrapper.getYaw(), wrapper.getPitch(), wrapper.isOnGround()
            ));
        } else if (type == PacketType.Play.Client.PLAYER_POSITION) {
            WrapperPlayClientPlayerPosition wrapper = new WrapperPlayClientPlayerPosition(event);
            return new Packet(Packet.Type.POSITION, now).withPosition(new WrapperPlayClientPosition(
                    wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ(), wrapper.isOnGround()
            ));
        } else if (type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerPositionAndRotation wrapper = new WrapperPlayClientPlayerPositionAndRotation(event);
            return new Packet(Packet.Type.POSITION_LOOK, now).withPositionLook(new WrapperPlayClientPositionLook(
                    wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ(), wrapper.getYaw(), wrapper.getPitch(), wrapper.isOnGround()
            ));
        } else if (type == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientUseEntity.Action action = switch (wrapper.getAction()) {
                case ATTACK -> WrapperPlayClientUseEntity.Action.ATTACK;
                case INTERACT_AT -> WrapperPlayClientUseEntity.Action.INTERACT_AT;
                case INTERACT -> WrapperPlayClientUseEntity.Action.INTERACT;
                default -> WrapperPlayClientUseEntity.Action.UNKNOWN;
            };
            return new Packet(Packet.Type.USE_ENTITY, now).withUseEntity(new WrapperPlayClientUseEntity(
                    wrapper.getEntityId(), action, null
            ));
        } else if (type == PacketType.Play.Client.CHAT_MESSAGE) {
            WrapperPlayClientChatMessage wrapper = new WrapperPlayClientChatMessage(event);
            return new Packet(Packet.Type.CHAT, now).withChat(new WrapperPlayClientChat(wrapper.getMessage()));
        } else if (type == PacketType.Play.Client.ENTITY_ACTION) {
            com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction wrapper = new com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction(event);
            me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action action;
            com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action typeAction = wrapper.getAction();

            // Simplified action mapping to avoid version-specific enum issues
            String actionName = typeAction.name();
            if (actionName.equals("START_SNEAKING")) {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.START_SNEAKING;
            } else if (actionName.equals("STOP_SNEAKING")) {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.STOP_SNEAKING;
            } else if (actionName.equals("START_SPRINTING")) {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.START_SPRINTING;
            } else if (actionName.equals("STOP_SPRINTING")) {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.STOP_SPRINTING;
            } else if (actionName.equals("START_FALL_FLYING")) {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.START_FALL_FLYING;
            } else if (actionName.equals("OPEN_INVENTORY")) {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.OPEN_INVENTORY;
            } else {
                action = me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction.Action.UNKNOWN;
            }

            return new Packet(Packet.Type.ENTITY_ACTION, now).withEntityAction(new me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction(
                    wrapper.getEntityId(), 0, action
            ));
        } else if (type == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            return new Packet(Packet.Type.WINDOW_CLICK, now).withWindowClick(new WrapperPlayClientWindowClick(
                    wrapper.getSlot()
            ));
        } else if (type == PacketType.Play.Client.ANIMATION) {
            return new Packet(Packet.Type.ARM_ANIMATION, now);
        }
        return null;
    }

    private Packet mapSend(PacketSendEvent event) {
        long now = System.currentTimeMillis();
        com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon type = event.getPacketType();

        if (type == PacketType.Play.Server.ENTITY_VELOCITY) {
            com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity wrapper =
                    new com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity(event);
            return new Packet(Packet.Type.SERVER_ENTITY_VELOCITY, now).withServerEntityVelocity(new WrapperPlayServerEntityVelocity(
                    wrapper.getEntityId(), wrapper.getVelocity().getX(), wrapper.getVelocity().getY(), wrapper.getVelocity().getZ()
            ));
        } else if (type == PacketType.Play.Server.KEEP_ALIVE) {
            return new Packet(Packet.Type.SERVER_KEEP_ALIVE, now);
        } else if (type == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            return new Packet(Packet.Type.SERVER_POSITION, now);
        } else if (type == PacketType.Play.Server.ENTITY_EFFECT) {
            return new Packet(Packet.Type.SERVER_ENTITY_EFFECT, now);
        } else if (type == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            return new Packet(Packet.Type.SERVER_REMOVE_ENTITY_EFFECT, now);
        }
        return null;
    }

    private String checkCrasher(Packet packet) {
        double x = 0D, y = 0D, z = 0D;
        float yaw = 0F, pitch = 0F;

        if (packet.getType() == Packet.Type.POSITION) {
            x = Math.abs(packet.getPositionWrapper().getX());
            y = Math.abs(packet.getPositionWrapper().getY());
            z = Math.abs(packet.getPositionWrapper().getZ());
        } else if (packet.getType() == Packet.Type.POSITION_LOOK) {
            x = Math.abs(packet.getPositionLookWrapper().getX());
            y = Math.abs(packet.getPositionLookWrapper().getY());
            z = Math.abs(packet.getPositionLookWrapper().getZ());
            yaw = Math.abs(packet.getPositionLookWrapper().getYaw());
            pitch = Math.abs(packet.getPositionLookWrapper().getPitch());
        } else if (packet.getType() == Packet.Type.LOOK) {
            yaw = Math.abs(packet.getLookWrapper().getYaw());
            pitch = Math.abs(packet.getLookWrapper().getPitch());
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

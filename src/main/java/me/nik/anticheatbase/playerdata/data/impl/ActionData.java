package me.nik.anticheatbase.playerdata.data.impl;

import lombok.Getter;
import me.nik.anticheatbase.Anticheat;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.playerdata.data.Data;
import me.nik.anticheatbase.processors.Packet;
import me.nik.anticheatbase.utils.MiscUtils;
import me.nik.anticheatbase.utils.custom.PlacedBlock;
import me.nik.anticheatbase.utils.custom.desync.Desync;
import me.nik.anticheatbase.wrappers.WrapperPlayClientEntityAction;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class ActionData implements Data {

    private GameMode gameMode;

    private boolean allowFlight, sneaking, sprinting, inventoryOpen;

    private final Desync desync;

    private PlacedBlock placedBlock;

    private ItemStack itemInMainHand = MiscUtils.EMPTY_ITEM, itemInOffHand = MiscUtils.EMPTY_ITEM;

    private int lastAllowFlightTicks, lastSleepingTicks, lastRidingTicks;

    /*
     * 1.9+
     */
    private int lastDuplicateOnePointSeventeenPacketTicks = 100;

    public ActionData(Profile profile) {

        this.desync = new Desync(profile);

        //Initialize

        Player player = profile.getPlayer();

        this.gameMode = player.getGameMode();

        this.allowFlight = Anticheat.getInstance().getNmsManager().getNmsInstance().getAllowFlight(player);

        this.lastAllowFlightTicks = this.allowFlight ? 0 : 100;
    }

    @Override
    public void process(Packet packet) {
        if (packet.is(Packet.Type.ENTITY_ACTION)) {
            WrapperPlayClientEntityAction wrapper = packet.getEntityActionWrapper();
            switch (wrapper.getAction()) {
                case START_SNEAKING -> this.sneaking = true;
                case STOP_SNEAKING -> this.sneaking = false;
                case START_SPRINTING -> this.sprinting = true;
                case STOP_SPRINTING -> this.sprinting = false;
                case OPEN_INVENTORY -> this.inventoryOpen = true;
            }
        } else if (packet.is(Packet.Type.WINDOW_CLICK)) {
            this.inventoryOpen = true;
        } else if (packet.is(Packet.Type.FLYING)) {
            this.lastAllowFlightTicks = this.allowFlight ? 0 : this.lastAllowFlightTicks + 1;
            this.lastSleepingTicks++;
            this.lastRidingTicks++;
            this.lastDuplicateOnePointSeventeenPacketTicks++;
        }
    }
}

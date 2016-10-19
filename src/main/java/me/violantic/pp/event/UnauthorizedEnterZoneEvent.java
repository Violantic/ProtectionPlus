package me.violantic.pp.event;

import me.violantic.pp.zone.Zone;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Ethan on 10/17/2016.
 */
public class UnauthorizedEnterZoneEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private UUID user;
    private Zone zone;

    public UnauthorizedEnterZoneEvent(UUID user, Zone zone) {
        this.user = user;
        this.zone = zone;
    }

    public UUID getUser() {
        return user;
    }

    public Zone getZone() {
        return zone;
    }

    public static HandlerList getHANDLERS() {
        return HANDLERS;
    }

}

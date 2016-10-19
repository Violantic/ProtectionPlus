package me.violantic.pp.event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Ethan on 10/17/2016.
 */
public class ZoneCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private UUID user;
    private Location location;

    public ZoneCreateEvent(UUID user, Location location) {
        this.user = user;
        this.location = location;
    }

    public UUID getUser() {
        return user;
    }

    public Location getLocation() {
        return location;
    }

    public static HandlerList getHANDLERS() {
        return HANDLERS;
    }
}

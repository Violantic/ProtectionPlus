package me.violantic.pp.listener;

import me.violantic.pp.ProtectionPlugin;
import me.violantic.pp.event.UnauthorizedEnterZoneEvent;
import me.violantic.pp.event.ZoneCreateEvent;
import me.violantic.pp.util.JSONUtil;
import me.violantic.pp.zone.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 10/19/2016.
 */
public class PlayerListener implements Listener {

    private ProtectionPlugin instance;
    private List<UUID> entry;

    public PlayerListener(ProtectionPlugin instance) {
        this.instance = instance;
        this.entry = new ArrayList<UUID>();
    }

    public ProtectionPlugin getInstance() {
        return instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getPlayer().isOp()) return;

        for(Zone zone : instance.getZoneHandler().getZones()) {
            double radius = zone.getRadius();
            Location location = new Location(Bukkit.getWorld("world"), zone.getCenter()[0], zone.getCenter()[1], zone.getCenter()[2]);

            // Getting 2D distance. //
            double y2 = event.getTo().getZ();
            double y1 = location.getZ();
            double x2 = event.getTo().getX();
            double x1 = location.getX();

            double distance = Math.sqrt((Math.pow(x2-x1, 2)+(Math.pow(y2-y1, 2))));

            if(distance < radius) {
                if(event.getPlayer().hasPermission("protectionplus.*") || event.getPlayer().hasPermission("protectionplus." + zone.getOwner().toString())) {
                    if(!entry.contains(event.getPlayer().getUniqueId())) {
                        event.getPlayer().sendMessage(instance.getPrefix() + "You have entered " + ((zone.isCustom()) ? zone.getCustom().replace("&", ChatColor.COLOR_CHAR + "") : "a protected area!"));
                        entry.add(event.getPlayer().getUniqueId());
                    }
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new UnauthorizedEnterZoneEvent(event.getPlayer().getUniqueId(), zone));
            } else if (distance > radius) {
                if(event.getPlayer().hasPermission("protectionplus.*") || event.getPlayer().hasPermission("protectionplus." + zone.getOwner().toString())) {
                    if(entry.contains(event.getPlayer().getUniqueId())) {
                        event.getPlayer().sendMessage(instance.getPrefix() + "You have exited " + ((zone.isCustom()) ? zone.getCustom().replace("&", ChatColor.COLOR_CHAR + "") : "a protected area!"));
                        entry.remove(event.getPlayer().getUniqueId());
                    }
                    return;
                }
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!player.getItemInHand().getType().equals(Material.STONE_SPADE)) return;
        if(!player.isOp()) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        try {
            if(JSONUtil.zoneExists(player.getUniqueId())) {
                player.sendMessage(instance.getPrefix() + "You have already created a zone, if you wish to create another you must speak with a developer.");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Block clicked = event.getClickedBlock();

        if(instance.getZoneHandler().getZones().size() == 0) {
            instance.getServer().getPluginManager().callEvent(new ZoneCreateEvent(player.getUniqueId(), clicked.getLocation()));
            return;
        }

        // Make sure block selected isn't in a current zone. //
        for(Zone zone : instance.getZoneHandler().getZones()) {
            double radius = zone.getRadius();
            Location location = new Location(Bukkit.getWorld("world"), zone.getCenter()[0], zone.getCenter()[1], zone.getCenter()[2]);

            // Getting 2D distance. //
            double y2 = clicked.getLocation().getZ();
            double y1 = location.getZ();
            double x2 = clicked.getLocation().getX();
            double x1 = location.getX();

            double distance = Math.sqrt((Math.pow(x2-x1, 2)+(Math.pow(y2-y1, 2))));

            if(distance > radius) {
                instance.getServer().getPluginManager().callEvent(new ZoneCreateEvent(player.getUniqueId(), clicked.getLocation()));
            } else {
                if(zone.getOwner().toString().contains(player.getUniqueId().toString())) return;
                player.sendMessage(instance.getPrefix() + "You are currently inside of another zone, leave it to create a new zone.");
            }

        }

    }
}

package me.violantic.pp.listener;

import me.violantic.pp.ProtectionPlugin;
import me.violantic.pp.event.UnauthorizedEnterZoneEvent;
import me.violantic.pp.event.ZoneCreateEvent;
import me.violantic.pp.util.JSONUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * Created by Ethan on 10/19/2016.
 */
public class ZoneListener implements Listener {

    private ProtectionPlugin instance;

    public ZoneListener(ProtectionPlugin instance) {
        this.instance = instance;
    }

    public ProtectionPlugin getInstance() {
        return instance;
    }

    @EventHandler
    public void onUnathorizedEnter(UnauthorizedEnterZoneEvent event) {
        Player player = Bukkit.getServer().getPlayer(event.getUser());

        Location zone = new Location(Bukkit.getWorld("world"), event.getZone().getCenter()[0], event.getZone().getCenter()[1], event.getZone().getCenter()[2]);

        Vector block = zone.getDirection();
        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();

        player.getLocation().setYaw(180 - ((float) Math.toDegrees(Math.atan2(x, z))) * -1);
        player.getLocation().setPitch(90 - ((float) Math.toDegrees(Math.acos(y))));

        player.setVelocity(player.getVelocity().multiply(-1.3D));

        if(event.getZone().isCustom()) {
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage(instance.getPrefix() + "You cannot enter " + (event.getZone().getCustom()).replace("&", ChatColor.COLOR_CHAR + ""));
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");

            Location clone = new Location(Bukkit.getWorld("world"), event.getZone().getCenter()[0], event.getZone().getCenter()[1], event.getZone().getCenter()[2]);

            for(int i = 0; i < 180; i ++) {
                double x1 = event.getZone().getRadius() * Math.cos(i);
                double z1 = event.getZone().getRadius() * Math.sin(i);

                clone.getWorld().playEffect(clone.clone().add(x1, (player.getLocation().getY() - clone.getY()), z1), Effect.HAPPY_VILLAGER, 1);
            }

        } else {
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage(instance.getPrefix() + "You are not allowed to enter that zone!");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");

        }
    }

    @EventHandler
    public void onCreate(ZoneCreateEvent event) {
        Double[] loc = new Double[]{event.getLocation().getX(), event.getLocation().getY(), event.getLocation().getZ()};
        JSONUtil.input(event.getUser(), loc, 16D);

        JSONUtil.dump();
        instance.getZoneHandler().load();
        System.out.println("PROTECTIONPLUS: Zones were loaded into memory!");

        Bukkit.getServer().getPlayer(event.getUser()).sendMessage(instance.getPrefix() + "You created a new Zone at " + Arrays.asList(loc).toString());
    }
}

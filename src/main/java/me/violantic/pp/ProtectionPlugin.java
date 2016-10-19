package me.violantic.pp;

import me.violantic.pp.listener.PlayerListener;
import me.violantic.pp.listener.ZoneListener;
import me.violantic.pp.zone.ZoneHandler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Ethan on 10/17/2016.
 */
public class ProtectionPlugin extends JavaPlugin {

    private ZoneHandler zoneHandler;

    @Override
    public void onEnable() {
        this.zoneHandler = new ZoneHandler(this);
        registerListeners();
    }

    @Override
    public void onDisable() {
        this.zoneHandler.unload();
    }

    public ZoneHandler getZoneHandler() {
        return zoneHandler;
    }

    public static String getPrefix() {
        return ChatColor.YELLOW + "" + ChatColor.BOLD + "ProtectionPlus " + ChatColor.RESET + ChatColor.GRAY;
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new ZoneListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
}

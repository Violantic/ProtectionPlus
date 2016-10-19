package me.violantic.pp.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Ethan on 10/17/2016.
 */
public class LocationUtil {

    public static Location fromArray(double[] array) {
        double x,y,z;
        x = array[0];
        y = array[1];
        z = array[2];
        return new Location(Bukkit.getWorld("world"), x, y, z);
    }

}

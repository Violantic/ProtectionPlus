package me.violantic.pp.zone;

import me.violantic.pp.util.JSONUtil;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Ethan on 10/17/2016.
 */
public class Zone {

    private UUID owner;
    private Double[] location;
    private Double radius;
    private String custom;

    public Zone(UUID owner) {
        this.owner = owner;
        load();
    }

    public UUID getOwner() {
        return owner;
    }

    public Double[] getCenter() {
        return location;
    }

    public double getRadius() {
        return radius;
    }

    public String getCustom() {
        return custom;
    }

    public Double[] fromJSONArray(JSONArray array) {
        Double[] d = new Double[3];
        for(int i = 0; i < array.size(); i++) {
            Double newD = (Double) array.get(i);
            d[i] = newD;
        }

        return d;
    }

    public boolean isCustom() {
        return (custom != null);
    }

    public void load() {
        location = fromJSONArray((JSONArray) JSONUtil.readZone(owner).get("location"));
        radius = (Double) JSONUtil.readZone(owner).get("radius");
        if(JSONUtil.readZone(owner).containsKey("custom")) {
            custom = (String) JSONUtil.readZone(owner).get("custom");
        } else {
            custom = null;
        }
    }

    @Override
    public String toString() {
        Double[] array = fromJSONArray((JSONArray) JSONUtil.readZone(owner).get("location"));
        return "{" + owner + ", " + Arrays.asList(array).toString() + ", " + radius + ((isCustom()) ? ", " + getCustom() + "}":"}");
    }

}

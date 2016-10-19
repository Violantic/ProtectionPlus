package me.violantic.pp.zone;

import me.violantic.pp.ProtectionPlugin;
import me.violantic.pp.util.JSONUtil;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 10/19/2016.
 */
public class ZoneHandler {

    private ProtectionPlugin instance;
    private Map<UUID, Zone> zoneMap;

    public ZoneHandler(ProtectionPlugin instance) {
        this.instance = instance;
        this.zoneMap = new ConcurrentHashMap<UUID, Zone>();
        load();
    }

    public ZoneHandler() {
        this(null);
    }

    public ProtectionPlugin getInstance() {
        return instance;
    }

    public Map<UUID, Zone> getZoneMap() {
        return zoneMap;
    }

    public Collection<Zone> getZones() {
        return getZoneMap().values();
    }

    public Zone getZone(UUID uuid) {
        return zoneMap.get(uuid);
    }

    public void load() {
        JSONUtil.dump();
        for (JSONObject object : JSONUtil.zoneData) {
            getZoneMap().put(UUID.fromString((String) object.get("uuid")), JSONUtil.getZone(UUID.fromString((String) object.get("uuid"))));
            System.out.println(Arrays.asList(zoneMap.values()).toString());
        }
    }

    public void unload() {
        for(Zone zone : zoneMap.values()) {
            JSONUtil.input(zone.getOwner(), zone.getCenter(), zone.getRadius());
        }
    }
}

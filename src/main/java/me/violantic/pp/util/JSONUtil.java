package me.violantic.pp.util;

import me.violantic.pp.ProtectionPlugin;
import me.violantic.pp.zone.Zone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ethan on 10/17/2016.
 */
public class JSONUtil {

    private static Set<File> files = new HashSet<File>();
    public static List<JSONObject> zoneData = new ArrayList<JSONObject>();

    public static Map<String, Object> readZone(final UUID uuid) {
        JSONParser parser = new JSONParser();

        try {
            Object object = parser.parse(new FileReader(ProtectionPlugin.getPlugin(ProtectionPlugin.class).getDataFolder() + "/zones/" + uuid.toString() + ".json"));

            if(object == null) {
                // Parser could not find the file. //
                System.out.println("Could not find: "  + uuid.toString());
                return null;
            }

            final JSONObject jsonObject = (JSONObject) object;

            final String uuidString = (String) jsonObject.get("uuid");
            final JSONArray locationPoints = (JSONArray) jsonObject.get("location");
            final double radius = (Double) jsonObject.get("radius");

            if(locationPoints == null) {
                System.out.println("The location or radius field was null in " + uuid.toString() + "'s zone file");
            }

            return new HashMap<String, Object>() {
                {
                    put("uuid", UUID.fromString(uuidString));
                    put("location", locationPoints);
                    put("radius", radius);
                    if(jsonObject.containsKey("custom")) {
                        put("custom", jsonObject.get("custom"));
                    }
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void dumpFiles(Set<File> collection) {
        File folder = new File(ProtectionPlugin.getPlugin(ProtectionPlugin.class).getDataFolder() + "/zones/");
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles == null) {
            System.out.println("PROTECTIONPLUS: No zone files exist yet!");
            return;
        }

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                collection.add(listOfFiles[i]);

                JSONParser parser = new JSONParser();

                try {
                    Object object = parser.parse(new FileReader(ProtectionPlugin.getPlugin(ProtectionPlugin.class).getDataFolder() + "/zones/" + listOfFiles[i].getName()));

                    if(object == null) {
                        // Parser could not find the file. //
                        System.out.println("Could not find: "  + listOfFiles[i].getName());
                    }

                    JSONObject jsonObject = (JSONObject) object;

                    System.out.println("Caching " + jsonObject.get("uuid"));

                    final String uuidString = (String) jsonObject.get("uuid");
                    final JSONArray locationPoints = (JSONArray) jsonObject.get("location");
                    final double radius = (Double) jsonObject.get("radius");

                    if(locationPoints == null) {
                        System.out.println("The location or radius field was null in " + listOfFiles[i].getName() + "'s zone file");
                    }

                    zoneData.add(jsonObject);
                    System.out.println("Zones that were loaded: " + zoneData.size());

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    public static void dump() {
        dumpFiles(files);
    }

    public static boolean zoneExists(UUID uuid) throws ParseException, IOException {
        for(File file : files) {
            if(file.getName().contains(uuid.toString())) {
                return true;
            }
        }

        return false;
    }


    public void serializeZone(Zone zone) {
        input(zone.getOwner(), zone.getCenter(), zone.getRadius());
    }

    public static Zone getZone(UUID uuid) {
        return new Zone(uuid);
    }

    /**
    public static void main(String[] args) {
        dumpFiles(files);
        new ZoneHandler();
    }
     **/

    public static void createNew(UUID uuid, Zone zone) {
        input(uuid, zone.getCenter(), zone.getRadius());
    }

    /**
     * For testing purposes
     * @param uuid
     * @param loc
     * @param radius
     */
    public static void input(UUID uuid, Double[] loc, double radius) {
        JSONObject countryObj = new JSONObject();
        countryObj.put("uuid", uuid.toString());

        JSONArray listOfStates = new JSONArray();
        for(int i = 0; i < loc.length; i++) {
            listOfStates.add(loc[i]);
        }
        countryObj.put("location", listOfStates);

        countryObj.put("radius", radius);

        try {

            // Writing to a file
            File file = new File(ProtectionPlugin.getPlugin(ProtectionPlugin.class).getDataFolder() + "/zones");
            File zone = new File(file, uuid.toString() + ".json");
            file.mkdirs();
            if(!zone.exists()) {
                zone.createNewFile();
            } else if(zone.exists()) {
                System.out.println("JSON file already existed for " + uuid + ", attempting to override data");
            }
            FileWriter fileWriter = new FileWriter(zone);
            System.out.println("Writing JSON object to file");
            System.out.println("-----------------------");
            System.out.print(countryObj);

            fileWriter.write(countryObj.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

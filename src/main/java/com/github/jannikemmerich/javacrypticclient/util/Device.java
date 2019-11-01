package com.github.jannikemmerich.javacrypticclient.util;

import com.github.jannikemmerich.javacrypticclient.client.Request;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Device {

    private UUID uuid;
    private String name;
    private UUID owner;
    private boolean poweredOn;

    public String getName() {
        return name;
    }

    public Device(JSONObject object) {
        this.uuid = UUID.fromString((String) object.get("uuid"));
        this.name = (String) object.get("name");
        this.owner = UUID.fromString((String) object.get("owner"));
        this.poweredOn = (boolean) object.get("powered_on");
    }

    public static List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();
        new Request(JSONBuilder.newJSONObject().add("ms", "device").add("endpoint", Arrays.asList("device", "all")).add("data", new JSONObject()).add("tag", UUID.randomUUID().toString()).build())
                .subscribe((result) -> {
                    List<JSONObject> jsonDevices = (List<JSONObject>) result.get("devices");
                    jsonDevices.forEach((json) -> devices.add(new Device(json)));
        });
        return devices;
    }

    public static Device getStarterDevice() {
        AtomicReference<Device> starterDevice = new AtomicReference<>();
        new Request(JSONBuilder.newJSONObject().add("ms", "device").add("endpoint", Arrays.asList("device", "starter_device")).add("data", new JSONObject()).add("tag", UUID.randomUUID().toString()).build())
                .subscribe((result) -> {
                    starterDevice.set(new Device(result));
                });
        return starterDevice.get();
    }
}

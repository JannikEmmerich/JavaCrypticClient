package com.github.jannikemmerich.javacrypticclient.models;

import com.github.jannikemmerich.javacrypticclient.client.Request;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeviceModel {

    private UUID uuid;
    private String hostname;

    public DeviceModel(UUID uuid, String hostname) {
        this.uuid = uuid;
        this.hostname = hostname;
    }

    public static DeviceModel getFirstDevice() {
        JSONObject requestData = Request.create("device", Arrays.asList("device", "all"), new JSONObject()).waitForAnswer();

        List<JSONObject> devices = (List<JSONObject>) requestData.get("devices");

        if(devices == null || devices.size() == 0) {
            JSONObject starterDevice = Request.create("device", Arrays.asList("device", "starter_device"), new JSONObject()).waitForAnswer();
            return new DeviceModel(UUID.fromString((String) starterDevice.get("uuid")), (String) starterDevice.get("name"));
        }

        return new DeviceModel(UUID.fromString((String) devices.get(0).get("uuid")), (String) devices.get(0).get("name"));
    }

    public String getHostname() {
        return hostname;
    }

    public UUID getUuid() {
        return uuid;
    }
}

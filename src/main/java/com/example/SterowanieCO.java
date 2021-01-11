package com.example;

import com.pi4j.io.serial.Serial;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.iot.webthing.Property;
import org.mozilla.iot.webthing.Thing;
import org.mozilla.iot.webthing.Value;

import java.util.Arrays;

class SterowanieCO extends Thing {

    public SterowanieCO(Serial aSerial) {

        super("urn:dev:ops:my-CO-M70A",
                "Sterowanie C.O.",
                new JSONArray(Arrays.asList("TemperatureSensor")),
                "Sterowanie tempreturami centralnego ogrzewania");

        JSONObject t_zkSetTemperature = new JSONObject();
        t_zkSetTemperature.put("@type", "TargetTemperatureProperty");
        t_zkSetTemperature.put("title", "Temperatura płaszcza kominka");
        t_zkSetTemperature.put("type", "integer");
        t_zkSetTemperature.put("description", "Ustawianie temperatury płaszcza kominka");
        t_zkSetTemperature.put("minimum", 30);
        t_zkSetTemperature.put("maximum", 100);
        t_zkSetTemperature.put("unit", "degrees celsius");

        SerialWrite serialWrite = new SerialWrite();
        Value<Integer> brightness = new Value<>(30,
                l -> {
                    serialWrite.serialWrite(aSerial, "{\"T_ZK\":" + l + "}");
                });

        this.addProperty(new Property(this,
                "temperatura",
                brightness,
                t_zkSetTemperature));
    }
}
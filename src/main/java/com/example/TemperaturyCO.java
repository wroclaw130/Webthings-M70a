package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.iot.webthing.Property;
import org.mozilla.iot.webthing.Thing;
import org.mozilla.iot.webthing.Value;
import java.util.Arrays;

class TemperaturyCO extends Thing {

    private Value<Object> T_K;
    private Value<Object> T_CWU;
    private Value<Object> T_CO1;
    private Value<Object> T_CO2;

    SerialListener serialListener = new SerialListener();

    public TemperaturyCO() {

        super("urn:dev:ops:my-temperatures-M70A",
                "temperatury",
                new JSONArray(Arrays.asList("TemperatureSensor")),
                "temperatury");

        JSONObject temperaturaCO2 = new JSONObject();
        temperaturaCO2.put("@type", "TemperatureProperty");
        temperaturaCO2.put("title", "Temperaura CO2");
        temperaturaCO2.put("type", "object");
        temperaturaCO2.put("unit", "degree celsius");
        temperaturaCO2.put("readOnly", true);
        this.T_CO2 = new Value<>(0);
        this.addProperty(new Property(
                this,
                "T_CO2",
                T_CO2,
                temperaturaCO2));

        JSONObject temperaturaCO1 = new JSONObject();
        temperaturaCO1.put("@type", "TemperatureProperty");
        temperaturaCO1.put("title", "Temperaura CO1");
        temperaturaCO1.put("type", "object");
        temperaturaCO1.put("unit", "degree celsius");
        temperaturaCO1.put("readOnly", true);
        this.T_CO1 = new Value<>(0);
        this.addProperty(new Property(
                this,
                "T_CO1",
                T_CO1,
                temperaturaCO1));

        JSONObject temperaturaWodyUzytkowej = new JSONObject();
        temperaturaWodyUzytkowej.put("@type", "TemperatureProperty");
        temperaturaWodyUzytkowej.put("title", "Temperaura centralnej wody użytkowej");
        temperaturaWodyUzytkowej.put("type", "object");
        temperaturaWodyUzytkowej.put("unit", "degree celsius");
        temperaturaWodyUzytkowej.put("readOnly", true);
        this.T_CWU = new Value<>(0);
        this.addProperty(new Property(
                this,
                "T_CWU",
                T_CWU,
                temperaturaWodyUzytkowej));

        JSONObject temperaturaPlaszczaKominka = new JSONObject();
        temperaturaPlaszczaKominka.put("@type", "TemperatureProperty");
        temperaturaPlaszczaKominka.put("title", "Temperaura płaszcza kominka");
        temperaturaPlaszczaKominka.put("type", "object");
        temperaturaPlaszczaKominka.put("unit", "degree celsius");
        temperaturaPlaszczaKominka.put("readOnly", true);
        this.T_K = new Value<>(0);
        this.addProperty(new Property(
                this,
                "T_ZK",
                T_K,
                temperaturaPlaszczaKominka));

        new Thread(() -> {
            while (true) {
                try {

                    JsonToString t_k = new JsonToString(SerialListener.getRawSerialData().toString(), "T_K");
                    Object t_k_valueFromJson = t_k.getValueFromJson();
                    this.T_K.notifyOfExternalUpdate(t_k_valueFromJson);

                    JsonToString t_cwu = new JsonToString(SerialListener.getRawSerialData().toString(), "T_CWU");
                    Object t_cwu_valueFromJson = t_cwu.getValueFromJson();
                    this.T_CWU.notifyOfExternalUpdate(t_cwu_valueFromJson);

                    JsonToString t_co1 = new JsonToString(SerialListener.getRawSerialData().toString(), "T_CO1");
                    Object t_co1_valueFromJson = t_co1.getValueFromJson();
                    this.T_CO1.notifyOfExternalUpdate(t_co1_valueFromJson);

                    JsonToString t_co2 = new JsonToString(SerialListener.getRawSerialData().toString(), "T_CO2");
                    Object t_co2_valueFromJson = t_co2.getValueFromJson();
                    this.T_CO2.notifyOfExternalUpdate(t_co2_valueFromJson);

                    Thread.sleep(1000);

                } catch (Exception aE) {
                    aE.printStackTrace();
                }
            }
        }).start();
    }
}
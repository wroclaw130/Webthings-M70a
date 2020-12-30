package com.example;

import com.pi4j.io.serial.*;
import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.iot.webthing.Property;
import org.mozilla.iot.webthing.Thing;
import org.mozilla.iot.webthing.Value;
import org.mozilla.iot.webthing.WebThingServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class M70a {

    private static Object rawSerialdata;

    public static void main(String[] args) {

        Serial serial = SerialFactory.createInstance();
        serialConfig(serial);

        Thread serialListener = new Thread(() -> {
            while (true) {
                try {

                    serialListener(serial);
                    Thread.sleep(1000);

                } catch (InterruptedException aE) {
                    aE.printStackTrace();
                }
            }
        });
        serialListener.start();

        Thread serialWrite = new Thread(() -> {
            while (true) {
                try {

                    serialWrite(serial);
                    Thread.sleep(1000);

                } catch (InterruptedException aE) {
                    aE.printStackTrace();
                }
            }
        });
        serialWrite.start();


//        Thing CO = new SterowanieCO();
        Thing temperatury = new TemperaturyCO();

        try {
            List<Thing> things = new ArrayList<>();
//            things.add(CO);
            things.add(temperatury);

            WebThingServer server =
                    new WebThingServer(new WebThingServer.MultipleThings(things,
                            "Sterowanie CO i odczyt temperatur"),
                            8888);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop()));

            server.start(false);


        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }


/*    public static class SterowanieCO extends Thing {
        public SterowanieCO() {
            super("urn:dev:ops:my-CO-M70A",
                    "CHU",
                    new JSONArray(Arrays.asList("OnOffSwitch"")),
                    "Test");

            JSONObject onDescription = new JSONObject();
            onDescription.put("@type", "OnOffProperty");
            onDescription.put("title", "On/Off");
            onDescription.put("type", "boolean");
            onDescription.put("description", "test");

            Value<Boolean> on = new Value<>(true,
                     v -> System.out.printf(
                            "On-State is now %s\n",
                            v));

            this.addProperty(new Property(this, "on", on, onDescription));

            JSONObject brightnessDescription = new JSONObject();
            brightnessDescription.put("@type", "Zawór_1");
            brightnessDescription.put("title", "Zawór_1");
            brightnessDescription.put("type", "boolean");
            brightnessDescription.put("description",
                    "Sterowanie zaworem 1");
            brightnessDescription.put("unit", "percent");

            Value<Integer> brightness = new Value<>(50,
                    // Here, you could send a signal
                    // to the GPIO that controls the
                    // brightness
                    l -> System.out.printf(
                            "Brightness is now %s\n",
                            l));

            this.addProperty(new Property(this,
                    "brightness",
                    brightness,
                    brightnessDescription));

            JSONObject fadeMetadata = new JSONObject();
            JSONObject fadeInput = new JSONObject();
            JSONObject fadeProperties = new JSONObject();
            JSONObject fadeBrightness = new JSONObject();
            JSONObject fadeDuration = new JSONObject();
            fadeMetadata.put("title", "Fade");
            fadeMetadata.put("description", "Fade the lamp to a given level");
            fadeInput.put("type", "object");
            fadeInput.put("required",
                    new JSONArray(Arrays.asList("brightness",
                            "duration")));
            fadeBrightness.put("type", "integer");
            fadeBrightness.put("minimum", 0);
            fadeBrightness.put("maximum", 100);
            fadeBrightness.put("unit", "percent");
            fadeDuration.put("type", "integer");
            fadeDuration.put("minimum", 1);
            fadeDuration.put("unit", "milliseconds");
            fadeProperties.put("brightness", fadeBrightness);
            fadeProperties.put("duration", fadeDuration);
            fadeInput.put("properties", fadeProperties);
            fadeMetadata.put("input", fadeInput);
            this.addAvailableAction("fade", fadeMetadata, FadeAction.class);

            JSONObject overheatedMetadata = new JSONObject();
            overheatedMetadata.put("description",
                    "The lamp has exceeded its safe operating temperature");
            overheatedMetadata.put("type", "number");
            overheatedMetadata.put("unit", "degree celsius");
            this.addAvailableEvent("overheated", overheatedMetadata);
        }

        public static class OverheatedEvent extends Event {
            public OverheatedEvent(Thing thing, int data) {
                super(thing, "overheated", data);
            }
        }

        public static class FadeAction extends Action {
            public FadeAction(Thing thing, JSONObject input) {
                super(UUID.randomUUID().toString(), thing, "fade", input);
            }

            @Override
            public void performAction() {
                Thing thing = this.getThing();
                JSONObject input = this.getInput();
                try {
                    Thread.sleep(input.getInt("duration"));
                } catch (InterruptedException e) {
                }

                try {
                    thing.setProperty("brightness", input.getInt("brightness"));
                    thing.addEvent(new OverheatedEvent(thing, 102));
                } catch (PropertyError e) {
                }
            }
        }
    }*/


    private static class TemperaturyCO extends Thing {

        private Value<Object> T_ZK;
        private Value<Object> T_CWU;
        private Value<Object> T_CO1;
        private Value<Object> T_CO2;

        public TemperaturyCO() {
            super("urn:dev:ops:my-temperatures-M70A",
                    "temperatury",
                    new JSONArray(Arrays.asList("TemperatureSensor")),
                    "temperatury");

            JSONObject temperaturaPlaszczaKominka = new JSONObject();
            temperaturaPlaszczaKominka.put("@type", "TemperatureProperty");
            temperaturaPlaszczaKominka.put("title", "Temperaura płaszcza kominka");
            temperaturaPlaszczaKominka.put("type", "object");
            temperaturaPlaszczaKominka.put("unit", "degree celsius");
            temperaturaPlaszczaKominka.put("readOnly", true);
            this.T_ZK = new Value<>(0);
            this.addProperty(new Property(
                    this,
                    "T_ZK",
                    T_ZK,
                    temperaturaPlaszczaKominka));

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

            new Thread(() -> {
                while (true) {
                    try {

                        /*JsonToString t_zk = new JsonToString(rawSerialdata.toString(), "T_ZK");
                        Object valueFromJson = t_zk.getValueFromJson();*/
                        Object t_zkValueFromJson = Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
                        this.T_ZK.notifyOfExternalUpdate(t_zkValueFromJson);
                        Object t_cwukValueFromJson = Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
                        this.T_CWU.notifyOfExternalUpdate(t_cwukValueFromJson);
                        Object t_co1kValueFromJson = Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
                        this.T_CO1.notifyOfExternalUpdate(t_co1kValueFromJson);
                        Object t_co2ValueFromJson = Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
                        this.T_CO2.notifyOfExternalUpdate(t_co2ValueFromJson);

                        Thread.sleep(1000);

                    } catch (Exception aE) {
                        aE.printStackTrace();
                    }
                }
            }).start();
        }

        /*Object SerialInput() {

            final Serial serial = SerialFactory.createInstance();
            serial.addListener(event -> {

                try {

                    System.out.println(event.getAsciiString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return rawSerialData;
        }*/

    }

    /*private static void PortOpening() {

        final Serial serial = SerialFactory.createInstance();
        try {

            SerialConfig config = new SerialConfig();

            config.device(SerialPort.getDefaultPort())
                    .baud(Baud._38400)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            serial.open(config);
            System.out.println("Port opened");

        } catch (IOException | InterruptedException | UnsupportedBoardType aE) {
            aE.printStackTrace();
        }
    }

    private static void sendingCommand() throws InterruptedException {
        final Serial serial = SerialFactory.createInstance();
        final Console console = new Console();
        while (console.isRunning()) {
            try {

                serial.write("{\"request\":\"T_ZK\"}");

            } catch (IllegalStateException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }*/

    private static void serialWrite(Serial aSerial) throws InterruptedException {

        while (true) {
            try {

                aSerial.write("{\"request\":\"ACT\"}");
            } catch (IllegalStateException | IOException ex) {
                ex.printStackTrace();
            }

            Thread.sleep(1000);
        }
    }

    private static void serialListener(Serial aSerial) {

        aSerial.addListener(event -> {

            try {
                setRawSerialdata(event.getAsciiString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void serialConfig(Serial aSerial) {

        try {

            SerialConfig config = new SerialConfig();

            config.device(SerialPort.getDefaultPort())
                    .baud(Baud._38400)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            aSerial.open(config);

        } catch (IOException | UnsupportedBoardType | InterruptedException aE) {
            aE.printStackTrace();
        }
    }

    static void setRawSerialdata(Object aRawSerialdata) {
        rawSerialdata = aRawSerialdata;
    }

}
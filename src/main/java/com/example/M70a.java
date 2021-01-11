package com.example;

import com.pi4j.io.serial.*;
import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import org.mozilla.iot.webthing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class M70a {


    public static void main(String[] args) {

        Serial serial = SerialFactory.createInstance();
        serialConfig(serial);

        SerialListener serialListener = new SerialListener();
        Thread serialListenerThread = new Thread(() -> {
            while (true) {
                serialListener.serialListener(serial);
            }
        });
        serialListenerThread.start();

        SerialWrite serialWrite = new SerialWrite();
        Thread serialWriteThread = new Thread(() -> {
            while (true) {
                try {
                    serialWrite.serialWrite(serial, "{\"request\":\"ACT\"}");
                    Thread.sleep(5000);
                } catch (InterruptedException aE) {
                    aE.printStackTrace();
                }
            }
        });
       serialWriteThread.start();


        Thing sterowanieCO = new SterowanieCO(serial);
        Thing temperaturyCO = new TemperaturyCO();

        try {
            List<Thing> things = new ArrayList<>();
            things.add(sterowanieCO);
            things.add(temperaturyCO);
            WebThingServer server =
                    new WebThingServer(new WebThingServer.MultipleThings(things,
                            "Sterowanie CO i odczyt temperatur"),
                            8888);
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            server.start(false);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

/*    static void serialWrite(Serial serial, String command) throws InterruptedException {
            try {
                serial.write("\""+command+"\"");
            } catch (IllegalStateException | IOException ex) {
                ex.printStackTrace();
            }
    }*/

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
}
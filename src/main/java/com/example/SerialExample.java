package com.example;

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.*;
import java.io.IOException;

public class SerialExample {

    private static Object rawSerialdata;

    public static void main(String[] args) {




    }

    private static void printingData() {

        while(true) {
            try {
                System.out.println(rawSerialdata);
                Thread.sleep(1000);

            } catch (Exception aE) {
                aE.printStackTrace();
            }
        }
    }

    private static void serialListener(Serial aSerial) {

        aSerial.addListener(event -> {

            try {
                setRawSerialdata(event.getAsciiString());
                System.out.println("Inside "+rawSerialdata);
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


            while (true) {
                try {

                    aSerial.write("{\"request\":\"T_ZK\"}");
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }

                Thread.sleep(1000);
            }

        } catch (IOException | UnsupportedBoardType | InterruptedException aE) {
            aE.printStackTrace();
        }
    }

    static void setRawSerialdata(Object aRawSerialdata) {
        rawSerialdata = aRawSerialdata;
    }
}
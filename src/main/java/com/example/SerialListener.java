package com.example;

import com.pi4j.io.serial.Serial;
import java.io.IOException;

public class SerialListener {

    static Object rawSerialData = "{\"T_K\":0,\"T_CWU\":0,\"T_CO1\":0,\"T_CO2\":0,\"P_K\":true,\"P_CO\":true,\"P_CWU\":false,\"P_C\":false,\"P_PC\":false,\"DM\":0,\"GRZ\":false,\"Epoch\":0,\"BCC\":0}";
    static Object getRawSerialData() {
        return rawSerialData;
    }
    static void setRawSerialData(Object aRawSerialData) {
        rawSerialData = aRawSerialData;
    }

    void serialListener(Serial aSerial) {
        aSerial.addListener(event -> {
            try {
                setRawSerialData(event.getAsciiString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

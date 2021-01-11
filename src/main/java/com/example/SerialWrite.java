package com.example;

import com.pi4j.io.serial.Serial;
import java.io.IOException;

public class SerialWrite{

    void serialWrite(Serial serial, String command) {

        try {

            serial.write("\"" + command + "\"");

        } catch (IllegalStateException | IOException ex) {
            ex.printStackTrace();
        }
    }
}

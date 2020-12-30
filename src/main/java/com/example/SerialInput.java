package com.example;

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.*;
import com.pi4j.util.Console;
import java.io.IOException;

public class SerialInput {

    public Object rawDataSerial = "42";

     SerialInput(){

         final Console console = new Console();
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

             while(console.isRunning()) {
                 try {

                     serial.write("{\"request\":\"T_ZK\"}");

                 }
                 catch(IllegalStateException ex){
                     ex.printStackTrace();
                 }

                 Thread.sleep(1000);
             }
         } catch (IOException | InterruptedException | UnsupportedBoardType aE) {
             aE.printStackTrace();
         }

         serial.addListener(new SerialDataEventListener() {
             @Override
             public void dataReceived(SerialDataEvent event) {

                 try {

                     console.println(event.getAsciiString());
                     setRawDataSerial(rawDataSerial);

                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         });

     }

    void setRawDataSerial(Object aRawDataSerial) {
        rawDataSerial = aRawDataSerial;
    }

    public Object getRawDataSerial() {
        return rawDataSerial;
    }
}
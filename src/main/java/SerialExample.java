import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class SerialExample {

    public static int SerialValue() throws Exception {

        final Console console = new Console();

        final Serial serial = SerialFactory.createInstance();


        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                try {

                    String dataInputSerial;

                    dataInputSerial = event.getAsciiString();

                    JSONParser dataInputSerialParse = new JSONParser();

                    JSONObject rawDataSerial = (JSONObject)  dataInputSerialParse.parse(dataInputSerial);

                    System.out.println(rawDataSerial.get("T_K"));

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });

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
                /*try {

                    serial.write("{\"request\":\"T_ZK\"}");

                }
                catch(IllegalStateException ex){
                    ex.printStackTrace();
                }

                Thread.sleep(2000);*/
            }

        }
    }
}
import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteM70a {

    public static JSONObject writeJsonSimpleDemo() throws Exception {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("T_K", 72);
        sampleObject.put("T_CWU", 45);
        sampleObject.put("T_CO1", 42);
        sampleObject.put("T_CO2", 39);
        sampleObject.put("BCC", 0);

        return sampleObject;
        }

    public static void main(String[] args) throws Exception {
        System.out.println(writeJsonSimpleDemo().get("T_CO2"));
    }
}

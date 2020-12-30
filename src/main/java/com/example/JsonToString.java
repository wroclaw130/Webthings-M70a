package com.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonToString {

    private Object rawDataSerial;
    private String valueIdentifier;
    private Object valueFromJson;

    public JsonToString(String aRawDataSerial, String aValueIdentifier) throws ParseException {
        rawDataSerial = aRawDataSerial;
        valueIdentifier = aValueIdentifier;


        JSONParser dataInputSerialParse = new JSONParser();
        JSONObject rawDataInput = (JSONObject) dataInputSerialParse.parse(rawDataSerial.toString());
        valueFromJson = rawDataInput.get(valueIdentifier);
    }

    Object getValueFromJson() {
        return valueFromJson;
    }
}
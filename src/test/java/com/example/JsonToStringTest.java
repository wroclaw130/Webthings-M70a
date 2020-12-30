package com.example;

import com.example.JsonToString;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonToStringTest {

    @Test
    void shouldReturnOneValueObject() throws ParseException {
        JsonToString t_zk = new JsonToString("{\"T_ZK\":73,\"BCC\":0}", "BCC");
        Object x = t_zk.getValueFromJson().toString();
        assertEquals("0", x);
    }
}
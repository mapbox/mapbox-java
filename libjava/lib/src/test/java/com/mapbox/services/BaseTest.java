package com.mapbox.services;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by antonio on 7/15/16.
 */
public class BaseTest {

    protected final static double DELTA = 1E-10;

    public void compareJson(String json1, String json2) {
        JsonParser parser = new JsonParser();
        assertEquals(parser.parse(json1), parser.parse(json2));
    }

    protected String loadJsonFixture(String folder, String filename) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/" + folder + "/" + filename));
        return new String(content, StandardCharsets.UTF_8);
    }

}

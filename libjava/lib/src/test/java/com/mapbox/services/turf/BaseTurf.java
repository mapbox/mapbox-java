package com.mapbox.services.turf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by antonio on 7/8/16.
 */
public class BaseTurf {

    protected final static double DELTA = 1E-10;

    protected String loadJsonFixture(String folder, String filename) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/" + folder + "/" + filename));
        return new String(content, StandardCharsets.UTF_8);
    }

}

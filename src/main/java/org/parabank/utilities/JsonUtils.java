package org.parabank.utilities;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

    /**
     * Read content of json file for the given jsonpath
     *
     * @param filepath complete path and name of the file
     * @param jsonpath path for which value is required
     * @return value in String format
     */
    public static String readJSONFile(String filepath, String jsonpath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        Configuration conf = Configuration.builder()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider())
                .build();
        DocumentContext context = JsonPath.using(conf).parse(content);
        return context.read(jsonpath, String.class);
    }
}

package ru.discloud.gateway.config;

import lombok.Data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class EndpointConfig {
    private final static String PROP_FILE_NAME = "endpoint.properties";
    private InputStream inputStream;
    private String protocol;
    private String location;
    private Integer port;

    public EndpointConfig(String serviceName) throws IOException {
        try {
            Properties prop = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + PROP_FILE_NAME + "' not found in the classpath");
            }

            String propertyBase = "endpoint." + serviceName;
            this.protocol = prop.getProperty(propertyBase + ".protocol") != null ? prop.getProperty(propertyBase + ".protocol") : "http";
            this.location = prop.getProperty(propertyBase + ".location") != null ? prop.getProperty(propertyBase + ".location") : "localhost";
            this.port = prop.getProperty(propertyBase + ".port") != null ? Integer.parseInt(prop.getProperty(propertyBase + ".port")) : 80;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (inputStream != null) inputStream.close();
        }
    }

    public String getBaseUrl() {
        return protocol + "://" + location + ":" + port;
    }
}

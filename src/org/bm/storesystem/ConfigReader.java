package org.bm.storesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    public static final String STORAGE_CONFIG_FILENAME = "storage-config.properties";
    private static final ConfigReader CONFIG_READER = new ConfigReader();

    public static final synchronized ConfigReader get() {
        return CONFIG_READER;
    }
    
    private ConfigReader() {}

    public Properties readConfig() {
        InputStream storageConfigInputStream = this.getClass().getResourceAsStream("/" + STORAGE_CONFIG_FILENAME);

        Properties p = new Properties();
        try {
            p.load(storageConfigInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }
}

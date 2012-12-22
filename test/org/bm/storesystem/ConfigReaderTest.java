package org.bm.storesystem;

import java.util.Properties;

import org.junit.Test;

public class ConfigReaderTest {

    @Test
    public void test() {
        Properties config = ConfigReader.get().readConfig();
        
        config.list(System.out);
    }

}

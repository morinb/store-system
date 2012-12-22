package org.bm.storesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

public class StoreSystemTest {

    private static final String STORAGE_CONFIG_FILENAME = "storage-config.properties";

    @Test
    public void test() {
        InputStream stream = this.getClass().getResourceAsStream("/" + STORAGE_CONFIG_FILENAME);

        Path storeFile = StoreSystem.get().storeFile(stream, STORAGE_CONFIG_FILENAME);
        Assert.assertNotNull(storeFile);
        System.out.println(storeFile.toString());
        
        InputStream inputStream = StoreSystem.get().serveFile(STORAGE_CONFIG_FILENAME);
        Assert.assertNotNull(inputStream);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        
        StringBuilder sb = new StringBuilder();
        String s;
        try {
            while((s = br.readLine()) != null) {
                sb.append(s).append('\n');
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

}

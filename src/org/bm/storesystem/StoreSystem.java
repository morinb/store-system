package org.bm.storesystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.bm.storesystem.utils.StoreSystemUtils;

public final class StoreSystem {
    private Path storagePath;
    private Properties config;
    public static final String DATA = "data";

    private static final StoreSystem STORE_SYSTEM = new StoreSystem();

    public static StoreSystem get() {
        return STORE_SYSTEM;
    }

    private StoreSystem() {
        config = ConfigReader.get().readConfig();
        String storagePath = (String) config.get("storate.path");

        this.storagePath = FileSystems.getDefault().getPath(storagePath);

        // ensure storagePath exists

        ensurePathExists(this.storagePath);

        // Then create directories.
        Path dataPath = FileSystems.getDefault().getPath(storagePath, DATA);
        ensurePathExists(dataPath);
    }

    private void ensurePathExists(Path path) {
        if (!Files.isDirectory(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Path storeFile(InputStream is, String filename) {

        int bufferSize = Integer.parseInt(config.getProperty("read.buffer", "4096"));

        try {
            String firstPart = extractHexHashDigit(filename, bufferSize, Integer.parseInt(config.getProperty("hash.digits", "2")));

            StringBuilder sb = new StringBuilder(filename);
            while (sb.length() < 6) {
                sb.append("_");
            }
            String directory = sb.substring(0, 6).toUpperCase();

            Path whereToStore = FileSystems.getDefault().getPath(this.storagePath.toString(), DATA, directory, firstPart);
            ensurePathExists(whereToStore);

            Path filePlace = FileSystems.getDefault().getPath(this.storagePath.toString(), DATA, directory, firstPart, filename);

            // Delete old version of file.
            Files.deleteIfExists(filePlace);
            Files.createFile(filePlace);
            Files.copy(is, filePlace, StandardCopyOption.REPLACE_EXISTING);

            return filePlace;

        } catch (NumberFormatException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String extractHexHashDigit(String filename, int bufferSize, int nbDigits) throws NoSuchAlgorithmException,
            IOException {
        MessageDigest md = MessageDigest.getInstance(config.getProperty("hash.algorithm", "MD5"));

        byte[] digest = md.digest(filename.getBytes());
        String hex = StoreSystemUtils.toHex(digest);
        if (hex.length() > nbDigits) {
            return hex.substring(0, nbDigits);
        }
        return hex;
    }

    public InputStream serveFile(String filename) {
        int bufferSize = Integer.parseInt(config.getProperty("read.buffer", "4096"));
        try {
            String firstPart = extractHexHashDigit(filename, bufferSize, Integer.parseInt(config.getProperty("hash.digits", "2")));
            StringBuilder sb = new StringBuilder(filename);
            while (sb.length() < 6) {
                sb.append("_");
            }
            String directory = sb.substring(0, 6).toUpperCase();

            Path filePlace = FileSystems.getDefault().getPath(this.storagePath.toString(), DATA, directory, firstPart, filename);
            
            return Files.newInputStream(filePlace, StandardOpenOption.READ);
            
        } catch (NumberFormatException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

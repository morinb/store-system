package org.bm.storesystem.utils;

import java.math.BigInteger;

public class StoreSystemUtils {
    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "x", bi);
    }

}

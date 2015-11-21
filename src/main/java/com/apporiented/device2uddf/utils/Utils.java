package com.apporiented.device2uddf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lars on 20.11.15.
 */
public final class Utils {

    private static Logger log = LoggerFactory.getLogger(Utils.class);

    public static double feetToMeters(int feet) {
        return (((int) ((feet)/128.0*0.3048*10))/10.0);
    }

    public static void logBytes(String name, byte[] result) {
        String s = toString(result);
        log.debug(name + ": " + s);
    }

    public static String toString(byte[] result) {
        StringBuffer sb = new StringBuffer();
        if (result != null) {
            for (byte b : result) {
                String s = Integer.toHexString(b & 0xff);
                if (s.length() == 1) {
                    s = "0" + s;
                }
                sb.append(s + " ");
            }
        }
        return sb.toString();
    }

}

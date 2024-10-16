package com.qdx.mule.connectors.ftps.internal.utils;

import java.io.UnsupportedEncodingException;

public class StringUtils {

    public static boolean isBlank(String val) {
        if (val == null) {
            return false;
        }

        String trimmedVal = val.trim();
        return trimmedVal.length() > 0;
    }

    public static String getIsoEncodedString(String utf) {
        String encoded = utf;
        try {
            encoded = new String(utf.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            // do nothing
        }

        return encoded;
    }
}

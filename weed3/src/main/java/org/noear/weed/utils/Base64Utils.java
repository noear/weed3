package org.noear.weed.utils;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Utils {
    public static String encode(String text){
        return encodeByte(text.getBytes(Charset.forName("UTF-8")));
    }

    public static String decode(String code){
        return new String(decodeByte(code));
    }

    public static String encodeByte(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeByte(String code){
        return Base64.getDecoder().decode(code);
    }
}

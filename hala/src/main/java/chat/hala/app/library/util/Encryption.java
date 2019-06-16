package chat.hala.app.library.util;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by astropete on 2017/12/25.
 */
public class Encryption {

    public static String MD5ed(String s){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    public static String SHA1ed(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }

}

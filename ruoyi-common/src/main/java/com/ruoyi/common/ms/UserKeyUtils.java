package com.ruoyi.common.ms;


import com.ruoyi.common.utils.StringUtils;
import org.apache.shiro.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * userkey 加解密类
 */
public class UserKeyUtils {

    private static final String CRYPT_ALGORITHM = "DESede";

    private static String KEY = "C0m9B8c7C6c5I4T3DeS2M1mC";

    public static String decrypt(String value, String key) {
        try {
            if (StringUtils.isBlank(value)) {
                return value;
            }
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(cipher.DECRYPT_MODE, keySpec);
            byte[] decodedByte = Base64.decode(value.getBytes("UTF-8"));
            byte[] decryptedByte = cipher.doFinal(decodedByte);
            return new String(decryptedByte, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String value, String key) {
        try {
            if (StringUtils.isBlank(value)) {
                return value;
            }
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedByte = cipher.doFinal(value.getBytes("UTF-8"));
            byte[] encodedByte = Base64.encode(encryptedByte);
            return new String(encodedByte, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String userKey = "+p9zGtPI8W+M1dGEiaap+f48ax7mBAzA";
        String decrypt = decrypt(userKey, KEY);
        System.out.println("decrypt: " + decrypt);
//        String str = decrypt;
        String str = "01|640221196503215118";
        String encrypt = encrypt(str, KEY);
        System.out.println("encrypt: " + encrypt);

    }
}

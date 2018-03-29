package org.hopto.atkseegow.utility;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AlgorithmAESUtility {

    public static String Encryption(String key, String value) throws Exception{
        SecretKeySpec secretKeySpec = AlgorithmAESUtility.GetSecretKeySpec(key);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return Base64.encodeToString(cipher.doFinal(value.getBytes()),Base64.NO_WRAP);
    }

    public static String Decryption(String key, String value) throws Exception{
        SecretKeySpec secretKeySpec = AlgorithmAESUtility.GetSecretKeySpec(key);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptionValue = cipher.doFinal(Base64.decode(value, Base64.NO_WRAP));
        return new String(decryptionValue);
    }

    public static SecretKeySpec GetSecretKeySpec(String key){
        byte[] keyEncoded = Base64.decode(key, Base64.DEFAULT);
        SecretKey secretKey = new SecretKeySpec(keyEncoded,"AES");
        byte[] secretKeyEncoded = secretKey.getEncoded();

        if(secretKeyEncoded.length != 16){
            byte[] bytes = new byte[16];
            for(int i = 0; i < secretKeyEncoded.length; i++)
                bytes[i] = secretKeyEncoded[i];
            secretKeyEncoded = bytes;
        }

        return new SecretKeySpec(secretKeyEncoded, "AES");
    }
}

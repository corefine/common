package org.corefine.weapp;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    /**
     * AES解密
     *
     * @param encryptedData 消息密文
     * @param ivStr         iv字符串
     */
    public static String decrypt(String sessionKey, String encryptedData, String ivStr) {
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(Base64.getDecoder().decode(ivStr)));

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(sessionKey), "AES"), params);

            return new String(PKCS7Encoder.decode(cipher.doFinal(Base64.getDecoder().decode(encryptedData))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }
}

package com.example.clup;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionService {
    static String algorithm = "DESede";

    public static void createES(String input) throws Exception {

        Key symKey = KeyGenerator.getInstance(algorithm).generateKey();


        Cipher c = Cipher.getInstance(algorithm);

        byte[] encryptionBytes = encryptF(input,symKey,c);

        System.out.println("Decrypted: " + decryptF(encryptionBytes,symKey,c));
    }

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        System.out.println("KEYYYYYYY");
        System.out.println(secret);
        return secret;
    }
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    //@RequiresApi(api = Build.VERSION_CODES.O)



        public static byte[] encryptF(String input,Key pkey,Cipher c) throws InvalidKeyException, BadPaddingException,

            IllegalBlockSizeException {

        c.init(Cipher.ENCRYPT_MODE, pkey);

        byte[] inputBytes = input.getBytes();

        return c.doFinal(inputBytes);
    }

    public static String decryptF(byte[] encryptionBytes,Key pkey,Cipher c) throws InvalidKeyException,

            BadPaddingException, IllegalBlockSizeException {

        c.init(Cipher.DECRYPT_MODE, pkey);

        byte[] decrypt = c.doFinal(encryptionBytes);

        String decrypted = new String(decrypt);

        return decrypted;
    }

}

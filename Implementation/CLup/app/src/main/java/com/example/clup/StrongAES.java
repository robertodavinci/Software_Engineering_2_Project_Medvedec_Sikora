package com.example.clup;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class StrongAES
{


    public byte[] AESEncrypt(String plaintext, String key){
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] cy = cipher.doFinal(plaintext.getBytes());
            //System.out.println(cy);
            return cy;
        }
        catch(Exception e){
            //return 'D';
            return "false".getBytes();
        }
    }

    public String AESDecrypt(byte[] cyphertext, String key){
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String cy2 = new String(cipher.doFinal(cyphertext));
            //System.out.println(cy2);
            return cy2;
        }
        catch(Exception e){
            System.err.println(e);
            return "CCCC";
        }
    }

    public String run(String text, String key)
    {
        try
        {
            //String text = "Hello World";
            //String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            //System.err.println(new String(encrypted));
            // decrypt the text
            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher2.doFinal(encrypted));
            System.out.println(decrypted);
            return "A";
            //System.err.println(decrypted);
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            return "DDDD";
        }
    }
}
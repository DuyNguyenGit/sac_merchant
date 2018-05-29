package com.stb.api.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
//import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class TripleDESCrypto {
	public static String encrypt(String input, String key) {
		if (input == null) {
			return null;
		}
		try {
			key = key.length() == 16 ? key + key.substring(0, 8) : key;
			KeySpec keySpec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
	        SecretKey secretKey = keyFactory.generateSecret(keySpec);
	        Cipher cipher = Cipher.getInstance("DESede");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	        return Base64Util.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    public static String decrypt(String input, String key) {
        if (input == null) {
			return null;
		}
		try {
			key = key.length() == 16 ? key + key.substring(0, 8) : key;
			KeySpec keySpec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
	        SecretKey secretKey = keyFactory.generateSecret(keySpec);
	        Cipher cipher = Cipher.getInstance("DESede");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey);
	        return new String(cipher.doFinal(Base64Util.getDecoder().decode(input)));
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
}

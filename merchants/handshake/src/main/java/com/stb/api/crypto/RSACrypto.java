package com.stb.api.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACrypto {
    public static String sign(String input, String key) {
    	if (input == null) {
    		return null;
    	}
		try {
	        key = key.replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)", "");
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Util.getDecoder().decode(key.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	        Signature signature = Signature.getInstance("SHA1withRSA");
	        signature.initSign(privateKey);
	        signature.update(input.getBytes());
	        return Base64Util.getEncoder().encodeToString(signature.sign());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public static String encrypt(String input, String key) {
    	if (input == null) {
    		return null;
    	}
    	key = key.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(Base64Util.getDecoder().decode(key.getBytes())));
	        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	        return Base64Util.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}

package com.stb.api.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Crypto {
	public static String calculate(String input) {
		if (input == null) {
			return null;
		}
		try {
			StringBuffer output = new StringBuffer();
	        MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            for(byte b : md.digest()) {
            	output.append(String.format("%02X", b));
            }
            return output.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
		return null;
	}
}

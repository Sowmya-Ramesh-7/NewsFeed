package com.newsfeed.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptString {
	public static String encrypt(String originalString) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("sha256");
		byte[] encryptedByteArray = digest.digest(originalString.getBytes());
		return Base64.getEncoder().encodeToString(encryptedByteArray);
	}
}

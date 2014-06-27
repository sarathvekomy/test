package com.vekomy.vbooks.security;

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.InvalidKeyException;

/**
 * @author Satish
 * 
 */
public class Encrypter {
	
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(Encrypter.class);
	/**
	 * String variable holdes algorithm.
	 */
	private static String algorithm = "DES";
	/**
	 * Key variable holdes key.
	 */
	private static Key key = null;
	/**
	 * Cipher variable holdes cipher.
	 */
	private static Cipher cipher = null;

	private static void setUp() throws Exception {
		key = KeyGenerator.getInstance(algorithm).generateKey();
		cipher = Cipher.getInstance(algorithm);
	}

	public static byte[] encrypt(String input) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException, Exception {
		
		if(_logger.isDebugEnabled()){
			_logger.debug("input string: {}", input);
		}
		
		setUp();
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return cipher.doFinal(inputBytes);
	}

	public static String decrypt(String input) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException, Exception {
		if(_logger.isDebugEnabled()){
			_logger.debug("input string: {}", input);
		}
		
		setUp();
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(input.getBytes());
		String recovered = new String(recoveredBytes);
		
		if(_logger.isDebugEnabled()){
			_logger.debug("decrypt string: {}", recovered);
		}
		return recovered;
	}
}

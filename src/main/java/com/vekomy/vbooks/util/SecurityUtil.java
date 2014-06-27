/**
 * com.vekomy.vbooks.util.SecurityUtil.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 8, 2013
 */
package com.vekomy.vbooks.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.SecurityException;

/**
 * This utility class is responsible to encode or decode {@link String} values.
 * 
 * @author Sudhakar
 * 
 */
public class SecurityUtil {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SecurityUtil.class);
	/**
	 * Cipher variable holds encryptCipher.
	 */
	private static Cipher encryptCipher = null;
	/**
	 * Cipher variable holds decryptCipher.
	 */
	private static Cipher decryptCipher = null;
	
	static {
		try {
			DESKeySpec key = new DESKeySpec(OrganizationUtils.SECRET_PASSWORD.getBytes());
			String algorithm = OrganizationUtils.DES_ALGORITHM;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			encryptCipher = Cipher.getInstance(algorithm);
			decryptCipher = Cipher.getInstance(algorithm);
			encryptCipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(key));
			decryptCipher.init(Cipher.DECRYPT_MODE,	keyFactory.generateSecret(key));
		} catch (InvalidKeyException exception) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Error Message: {}", exception.getMessage());
			}
		} catch (NoSuchAlgorithmException exception) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Error Message: {}", exception.getMessage());
			}
		} catch (NoSuchPaddingException exception) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Error Message: {}", exception.getMessage());
			}
		} catch (InvalidKeySpecException exception) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Error Message: {}", exception.getMessage());
			}
		}
	}

	/**
	 * This method is responsible to encode the input {@link String}.
	 * 
	 * @param input - {@link String}
	 * @return decriptedString - {@link String}
	 * @throws SecurityException - {@link SecurityException}
	 */
	public static String encode(String input) throws SecurityException {
		String encriptedString = "";
		try {
			if(!StringUtils.isEmpty(input)) {
				// Encode the string into bytes using UTF-8
				byte[] unencryptedByteArray = input.getBytes("UTF8");
				// Encoding
				byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);
				// Encode bytes to base64 to get a string
				byte[] encodedBytes = Base64.encodeBase64(encryptedBytes);
				encriptedString = new String(encodedBytes, "UTF8");
			}
		} catch (IllegalBlockSizeException exception) {
			throw new SecurityException();
		} catch (BadPaddingException exception) {
			throw new SecurityException();
		} catch (UnsupportedEncodingException exception) {
			throw new SecurityException();
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Input: {} ---> Encoded value: {} ", input, encriptedString);
		}
		return encriptedString;
	}

	/**
	 * This method is responsible to decode the input {@link String}.
	 * 
	 * @param input - {@link String}
	 * @return decriptedString - {@link String}
	 * @throws SecurityException - {@link SecurityException}
	 */
	public static String decode(String input) throws SecurityException {
		String decriptedString = "";
		try {
			if(!StringUtils.isEmpty(input)) {
				// Encode bytes to base64 to get a string
				byte[] decodedBytes = Base64.decodeBase64(input.getBytes());
				// Decoding
				byte[] unencryptedByteArray = decryptCipher.doFinal(decodedBytes);
				decriptedString = new String(unencryptedByteArray, "UTF8");
			}
		} catch (IllegalBlockSizeException exception) {
			throw new SecurityException();
		} catch (BadPaddingException exception) {
			throw new SecurityException();
		} catch (UnsupportedEncodingException exception) {
			throw new SecurityException();
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Input: {} ---> Decoded value: {} ", input, decriptedString);
		}
		return decriptedString;
	}
}

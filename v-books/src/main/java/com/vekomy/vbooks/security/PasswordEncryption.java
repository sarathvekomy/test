package com.vekomy.vbooks.security;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Satish
 * 
 */
public class PasswordEncryption {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(PasswordEncryption.class);
	/**
	 * BasicTextEncryptor variable holds basicTextEncryptor.
	 */
	private static BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
	
	static {
		basicTextEncryptor.setPassword("password");
	}

	public static String encryptPassword(String password) {
		if(_logger.isDebugEnabled()){
			_logger.debug("password to encrypt is: {}", password);
		}
		String convertedPassword = null;
		try {
			MessageDigest messageDigestEncrypt = MessageDigest.getInstance("MD5");
			messageDigestEncrypt.update(password.getBytes(), 0, password.length());
			convertedPassword = new BigInteger(1, messageDigestEncrypt.digest()).toString(16);
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Error message: {}", e.getMessage());
			}
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("Encrypted password: {}", convertedPassword);
		}
		return convertedPassword;
	}

	public static String encrypt(String token) {
		String emptyString = "";
		if (token == null || token.length() == 0) {
			return emptyString;
		} else {
			return basicTextEncryptor.encrypt(token);
		}
	}

	public static String decrypt(String token) {
		String emptyString = "";
		try {
			if (token == null || token.length() == 0) {
				return emptyString;
			} else {
				return basicTextEncryptor.decrypt(token);
			}
		} catch (EncryptionOperationNotPossibleException e) {
			return token;

		}
	}
}
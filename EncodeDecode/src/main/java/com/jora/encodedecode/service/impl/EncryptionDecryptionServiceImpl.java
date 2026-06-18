package com.jora.encodedecode.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jora.encodedecode.service.EncryptionDecryptionService;

@Service
public class EncryptionDecryptionServiceImpl implements EncryptionDecryptionService {
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String KDF_ALGO = "PBKDF2WithHmacSHA256";
	private static final int KEY_SIZE_BITS = 128; // use 256 if available
	private static final int SALT_LENGTH = 16;
	private static final int IV_LENGTH = 16;
	private static final SecureRandom RNG = new SecureRandom();

	@Value("${encode.passcode:RamIsFire}")
	private String keyCode;

	@Value("${encode.iterations:65536}")
	private int iterations;

	private SecretKey deriveKey(byte[] salt) throws Exception {
		PBEKeySpec spec = new PBEKeySpec(keyCode.toCharArray(), salt, iterations, KEY_SIZE_BITS);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KDF_ALGO);
		byte[] keyBytes = factory.generateSecret(spec).getEncoded();
		return new SecretKeySpec(keyBytes, "AES");
	}

	@Override
	public String encrypt(String plainText) throws Exception {
		byte[] salt = new byte[SALT_LENGTH];
		byte[] iv = new byte[IV_LENGTH];
		RNG.nextBytes(salt);
		RNG.nextBytes(iv);
		SecretKey secretKey = deriveKey(salt);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		byte[] combined = new byte[SALT_LENGTH + IV_LENGTH + cipherText.length];
		System.arraycopy(salt, 0, combined, 0, SALT_LENGTH);
		System.arraycopy(iv, 0, combined, SALT_LENGTH, IV_LENGTH);
		System.arraycopy(cipherText, 0, combined, SALT_LENGTH + IV_LENGTH, cipherText.length);
		return Base64.getEncoder().encodeToString(combined);
	}

	@Override
	public String decrypt(String encryptedText) throws Exception {
		byte[] combined = Base64.getDecoder().decode(encryptedText);
		if (combined.length <= SALT_LENGTH + IV_LENGTH) {
			throw new IllegalArgumentException("Invalid payload: too short");
		}
		byte[] salt = Arrays.copyOfRange(combined, 0, SALT_LENGTH);
		byte[] iv = Arrays.copyOfRange(combined, SALT_LENGTH, SALT_LENGTH + IV_LENGTH);
		byte[] cipherBytes = Arrays.copyOfRange(combined, SALT_LENGTH + IV_LENGTH, combined.length);
		SecretKey secretKey = deriveKey(salt);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] plain = cipher.doFinal(cipherBytes);
		return new String(plain, StandardCharsets.UTF_8);
	}
}

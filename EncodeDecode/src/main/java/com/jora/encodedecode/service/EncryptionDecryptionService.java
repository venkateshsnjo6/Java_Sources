package com.jora.encodedecode.service;

public interface EncryptionDecryptionService {

	String encrypt(String plainText) throws Exception;

	String decrypt(String encodedText) throws Exception;
}

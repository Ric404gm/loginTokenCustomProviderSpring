package com.asp.eiyu.ldap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

/*
 * Oficial documentation
 * https://www.baeldung.com/java-aes-encryption-decryption
 */

@SpringBootTest
class LdapApplicationTests {


	private static final Logger LOGGER = LoggerFactory.getLogger(LdapApplicationTests.class);

	private static final String  keyAsString =  "llaveenstring010";  
    

	@Test
	void contextLoads()  throws Exception{



		String input = "admin";
		SecretKey secretKey = generateKey(128);
		
		LOGGER.info(" Key {} ", secretKey.getEncoded());
		IvParameterSpec ivParameterSpec = generateIv();
		
		LOGGER.info(" IV {} ", ivParameterSpec.getIV());
		



		String algorithm = "AES/CBC/PKCS5Padding";
		String cipherText = encrypt(algorithm, input,secretKey, ivParameterSpec);
		String plainText = decrypt(algorithm, cipherText, secretKey, ivParameterSpec);
		


		LOGGER.info(" Input  {}  cifrado {}  decifrado {} ",input, cipherText,plainText);
		assertEquals(input, plainText, " * Test * ");


	}

	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(n);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}


	public static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}


	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException 
	{
    
		Cipher cipher = Cipher.getInstance(algorithm);

		//cipher.init(Cipher.DECRYPT_MODE, key, iv);
		
		 cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyAsString.getBytes()));
		
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(plainText);
	}

	public static String encrypt(String algorithm, String input, SecretKey key,IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException 
	{
    
		Cipher cipher = Cipher.getInstance(algorithm);
		//cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keyAsString.getBytes()));


		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
	}
}

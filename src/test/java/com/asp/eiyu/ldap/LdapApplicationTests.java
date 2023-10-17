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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.asp.eiyu.ldap.utils.AesUtil;
import com.asp.eiyu.ldap.utils.AesUtil.OperationType;

/*
 * Oficial documentation
 * https://www.baeldung.com/java-aes-encryption-decryption
 * http://www.java2s.com/example/java-api/javax/crypto/spec/ivparameterspec/ivparameterspec-1-6.html
 * https://www.baeldung.com/java-encryption-iv 
 * https://mkyong.com/java/java-aes-encryption-and-decryption/
 * https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
 * 
 */

@SpringBootTest
class LdapApplicationTests {


	private static final Logger LOGGER = LoggerFactory.getLogger(LdapApplicationTests.class);

    //Input  HOLA  cifrado l7UP68c69TPpowhxMvCgYA==  decifrado HOLA
	//private static final String keyAsString = "bGxhdmVlbnN0cmluZzAxMA=="; //llaveenstring010 a base 64 : https://www.base64encode.org/  
	//private static final String keyAsString =  "MDEyMzQ1Njc4OTEyMzQ1Ng==";//0123456789123456
	
	
	private static final String keyAsString =  "ZWl5dWtleWFzcDEwMjAyMw==";//eiyukeyasp102023  en base64 debe de tener al menos 24 caracteres
	private static final String ivAsString = "ZWl5dWtleWFzcA=="; //eiyukeyasp
	private static final String INPUT_TEST = "$2a$12$qR05mZYgmxNamlNKu1JWB..6ygypNqfVL3N5t5l2IdsAqwnoOC.t.";

	@Autowired
	private AesUtil aesUtil;


	@Test
	void contextLoads()  throws Exception{


		 
		String  value  = "admin";
		assertEquals(value , aesUtil.doOperation(
				aesUtil.doOperation(value, OperationType.ENCRYPT),
					 OperationType.DECRYPT)  , " * Test * ");
		
		
		String input = INPUT_TEST;
		SecretKey secretKey = decodeBase64ToAESKey(this.keyAsString);
		
		LOGGER.info(" Key {} ", secretKey.getEncoded());
		IvParameterSpec ivParameterSpec = generateIv();
		
		LOGGER.info(" IV {} ", ivParameterSpec.getIV().getClass());
		
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
		return new  IvParameterSpec(ivAsString.getBytes());
		//return new IvParameterSpec(iv); 
	}


	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException 
	{
    
		Cipher cipher = Cipher.getInstance(algorithm);

		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		
		 //cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyAsString.getBytes()));
		
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(plainText);
	}

	public static String encrypt(String algorithm, String input, SecretKey key,IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException 
	{
    
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		//cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keyAsString.getBytes()));


		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static SecretKey decodeBase64ToAESKey(final String encodedKey) throws IllegalArgumentException 
	{
		try {
			// throws IllegalArgumentException - if src is not in valid Base64
			// scheme
			final byte[] keyData = Base64.getDecoder().decode(encodedKey);
			final int keysize = keyData.length * Byte.SIZE;

			// this should be checked by a SecretKeyFactory, but that doesn't exist for AES
			switch (keysize) {
			case 128:
			case 192:
			case 256:
				break;
			default:
				throw new IllegalArgumentException("Invalid key size for AES: " + keysize);
			}

			if (Cipher.getMaxAllowedKeyLength("AES") < keysize) {
				// this may be an issue if unlimited crypto is not installed
				throw new IllegalArgumentException("Key size of " + keysize
						+ " not supported in this runtime");
			}

		// throws IllegalArgumentException - if key is empty
			final SecretKeySpec aesKey = new SecretKeySpec(keyData, "AES");
			return aesKey;
		} catch (final NoSuchAlgorithmException e) {
			// AES functionality is a requirement for any Java SE runtime
			throw new IllegalStateException(
					"AES should always be present in a Java SE runtime", e);
		}
	}
}

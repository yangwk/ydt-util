package com.github.yangwk.ydtutil.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 对称加密解密 工具类
 * 
 * @author yangwk
 *
 */
public class SecurityUtils {

	/**
	 * DES 算法
	 */
	public static final String ALGORITHM_DES = "DES";
	/**
	 * AES 算法
	 */
	public static final String ALGORITHM_AES = "AES";
	/**
	 * DESede(3DES) 算法
	 */
	public static final String ALGORITHM_DESEDE = "DESede";

	private static SecretKey getSecretKey(byte[] key,String algorithm){
		SecretKey securekey = new SecretKeySpec(key,algorithm);

		return securekey;
	}
	
	/**
	 * 加密
	 * @author yangwk
	 *
	 * @param data
	 * @param key
	 * @param algorithm 算法名称 {@link #ALGORITHM_AES} , {@link #ALGORITHM_DES} , {@link #ALGORITHM_DESEDE}
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		try {
			SecretKey securekey = getSecretKey(key, algorithm);

			Cipher cipher = Cipher.getInstance(algorithm, new BouncyCastleProvider());

			SecureRandom sr = new SecureRandom();

			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

			byte[] result = cipher.doFinal(data);

			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 * @author yangwk
	 *
	 * @param data
	 * @param key
	 * @param algorithm 算法名称 {@link #ALGORITHM_AES} , {@link #ALGORITHM_DES} , {@link #ALGORITHM_DESEDE}
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key, String algorithm) {
		try {
			SecretKey securekey = getSecretKey(key, algorithm);

			Cipher cipher = Cipher.getInstance(algorithm, new BouncyCastleProvider());

			SecureRandom sr = new SecureRandom();

			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

			byte[] result = cipher.doFinal(data);

			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

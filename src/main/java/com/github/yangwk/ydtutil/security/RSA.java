package com.github.yangwk.ydtutil.security;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * RSA 算法工具
 * @author yangwk
 *
 */
public class RSA {
	
	private final String ALGORITHM = "RSA";
	private final String TRANSFORMATION = "RSA/None/PKCS1Padding";
	private final int KEYSIZE = 1024;

	private RSAPrivateKey privateKey;	//私钥

	private RSAPublicKey publicKey;	//公钥
	
	/**
	 * 获取私钥
	 * @return 当前的私钥对象
	 */
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 生成随机密钥对，将密钥对保存到文件
	 * 
	 * @author yangwk
	 * @param privateKeyPath
	 *            私钥存放路径，绝对路径
	 * @param publicKeyPath
	 *            公钥存放路径，绝对路径
	 */
	public void genKeyPair(String privateKeyPath, String publicKeyPath){
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM, new BouncyCastleProvider());
			keyPairGen.initialize(KEYSIZE, new SecureRandom());
			KeyPair keyPair= keyPairGen.generateKeyPair();
			
			KeyPairUtils.saveToFile(keyPair, privateKeyPath, publicKeyPath);
			
			this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
			this.publicKey= (RSAPublicKey) keyPair.getPublic();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String load(InputStream input){
		InputStreamReader reader = null;
		BufferedReader bufferReader = null;
		try {
			reader = new InputStreamReader(input);
			bufferReader = new BufferedReader(reader);
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			
			while((readLine= bufferReader.readLine())!=null){
				if(readLine.isEmpty() || 
						readLine.charAt(0)=='-' ){	//注释符-开头
					continue;
				}else{
					sb.append(readLine);
				}
			}
			
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(bufferReader, reader, input);
		}
	}

	/**
	 * 从文件中输入流中加载公钥
	 */
	public void loadPublicKey(InputStream input){
		loadPublicKey( load(input) );
	}


	/**
	 * 从字符串中加载公钥
	 */
	public void loadPublicKey(String base64PublicKey){
		try {
			byte[] keys = Base64.decodeBase64(base64PublicKey);	//解码base64
			
			KeyFactory keyFactory= KeyFactory.getInstance(ALGORITHM, new BouncyCastleProvider());
			X509EncodedKeySpec keySpec= new X509EncodedKeySpec( keys );
			
			this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从文件中加载私钥
	 */
	public void loadPKCS8PrivateKey(InputStream in){
		loadPKCS8PrivateKey( load(in) );
	}

	public void loadPKCS8PrivateKey(String base64privateKey){
		try {
			byte[] keys = Base64.decodeBase64(base64privateKey);	//解码base64
			
			KeyFactory keyFactory= KeyFactory.getInstance(ALGORITHM, new BouncyCastleProvider());
			PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(keys);
			
			this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			
		} catch (Exception e) {
			throw new RuntimeException(e);	
		}
	}

	public void loadPKCS1PrivateKey(String base64privateKey){
		try {
			byte[] keys = Base64.decodeBase64(base64privateKey);	//解码base64
			
			org.bouncycastle.asn1.pkcs.RSAPrivateKey asn1PrivateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance( ASN1Sequence.getInstance(keys) );
			
			KeyFactory keyFactory= KeyFactory.getInstance(ALGORITHM, new BouncyCastleProvider());
			RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivateKey.getModulus(), asn1PrivateKey.getPrivateExponent());
			
			this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKeySpec);
			
		} catch (Exception e) {
			throw new RuntimeException(e);	
		}
	}

	public void loadPKCS1PrivateKey(InputStream input){
		loadPKCS1PrivateKey( load(input));
	}

	/**
	 * 加密过程
	 * @param publicKey 公钥
	 * @param data 明文数据
	 * @return
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] data){
		try {
			Cipher cipher= Cipher.getInstance(TRANSFORMATION, new BouncyCastleProvider());
			
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, new SecureRandom());
			
			byte[] result = cipher.doFinal(data);
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
	 * 解密过程
	 * @param privateKey 私钥
	 * @param data 密文数据
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] data) {
		try {
			Cipher cipher= Cipher.getInstance(TRANSFORMATION, new BouncyCastleProvider());
			
			cipher.init(Cipher.DECRYPT_MODE, privateKey, new SecureRandom());
			
			byte[] result = cipher.doFinal(data);
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
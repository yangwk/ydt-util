package com.github.yangwk.ydtutil.security;

import java.io.FileInputStream;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.github.yangwk.ydtutil.security.RSA;

public class RSATest {

	@Test
	public void testEncrypt() throws Exception{
		RSA rsa = new RSA();
		
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w"
				+ "/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRht"
				+ "Vx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNg"
				+ "XIlk3gdhnzh+uoEQywIDAQAB";
		rsa.loadPublicKey(publicKey);
		
		String content = "abcd";
		
		byte[] result = rsa.encrypt(rsa.getPublicKey(), content.getBytes("UTF-8"));
		
		System.out.println(Hex.encodeHexString(result));
		//2737949665f836508ef9197bd5f7665bf690d86e8d822a968cdef76571b998abcd9363a56f4a5919956b945833956c4d4d258417c0465e1e662345c8a20c6d1aae0480c01d823fa38ed1f0606b9735d38169b6b534a3eef712f7b1672f4a33cdce661ee3a857a9c3200b6b3d0c85185ba39f3d12651b1f4032d4cdaee374429f

	}
	
	@Test
	public void testDecrypt() throws Exception {
		RSA rsa = new RSA();
		
		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG"
		+ "6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4"
		+ "1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA"
		+ "ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm"
		+ "u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j"
		+ "DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo"
		+ "MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ"
		+ "DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM"
		+ "6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ"
		+ "FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA"
		+ "2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX"
		+ "TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh"
		+ "Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo"
		+ "1NMLzI2ZfUoX";
		
		rsa.loadPKCS8PrivateKey(privateKey);
		
		String src = "2737949665f836508ef9197bd5f7665bf690d86e8d822a968cdef76571b998abcd9363a56f4a5919956b945833956c4d4d258417c0465e1e662345c8a20c6d1aae0480c01d823fa38ed1f0606b9735d38169b6b534a3eef712f7b1672f4a33cdce661ee3a857a9c3200b6b3d0c85185ba39f3d12651b1f4032d4cdaee374429f";
		byte[] data = Hex.decodeHex(src.toCharArray());
		
		byte[] result = rsa.decrypt(rsa.getPrivateKey(), data);
		
		System.out.println(new String(result, "UTF-8"));
	}

	@Test
	public void testEncryptFromFile() throws Exception{
		RSA rsa = new RSA();
		
		String filePath = "f:/test/rsa/gen/xxx-pub.key";
		FileInputStream input = new FileInputStream(filePath);
		rsa.loadPublicKey(input);
		
		String content = "abcd";
		
		byte[] result = rsa.encrypt(rsa.getPublicKey(), content.getBytes("UTF-8"));
		
		System.out.println(Hex.encodeHexString(result));
		//a64e2d9157b8753de959a9850667fbd8ee4e1ea48e0ca17c31a6c816b60b9b8d5f759d3cabc9f71484395a266da4f723807cca9827c48110024bc4f9a7dfdd2c07361ea377e9f9ebbb5f6b7c8830aa903151809be6157f0767ad57706ffaca40de432c5ef79edaa85617309cb4abe4a5ea797b12664fd8949f9676f2346e4136


	}

	@Test
	public void testDecryptFromFile() throws Exception {
		RSA rsa = new RSA();
		
		String filePath = "f:/test/rsa/gen/xxx-pri.key";
		FileInputStream input = new FileInputStream(filePath);
		rsa.loadPKCS8PrivateKey(input);
		
		String src = "a64e2d9157b8753de959a9850667fbd8ee4e1ea48e0ca17c31a6c816b60b9b8d5f759d3cabc9f71484395a266da4f723807cca9827c48110024bc4f9a7dfdd2c07361ea377e9f9ebbb5f6b7c8830aa903151809be6157f0767ad57706ffaca40de432c5ef79edaa85617309cb4abe4a5ea797b12664fd8949f9676f2346e4136";
		byte[] data = Hex.decodeHex(src.toCharArray());
		
		byte[] result = rsa.decrypt(rsa.getPrivateKey(), data);
		
		System.out.println(new String(result, "UTF-8"));
	}
	
	@Test
	public void testGenKeyPair() throws Exception{
		RSA rsa = new RSA();
		rsa.genKeyPair("f:/test/rsa/gen/xxx-pri.key", "f:/test/rsa/gen/xxx-pub.key");
	}
	
	
}

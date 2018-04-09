package com.github.yangwk.ydtutil.test.security;

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
		//8bcdbb19f1ae93f4c638205ff677d00cf10b9ae96561a1e454c9c10a8c36771c6282335253cc14c01d3dab603c5b223f2c071ce67e2eab582af4c9e21b147480ab077c171ae527a253ccdb7a20bee0a86f778e8e5b54c3a063469d2227569d0cffa595b080b942a37218fb4c94b1a7654dd82d28c0e850597b4b5f8e947d2203

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
		
		String src = "8bcdbb19f1ae93f4c638205ff677d00cf10b9ae96561a1e454c9c10a8c36771c6282335253cc14c01d3dab603c5b223f2c071ce67e2eab582af4c9e21b147480ab077c171ae527a253ccdb7a20bee0a86f778e8e5b54c3a063469d2227569d0cffa595b080b942a37218fb4c94b1a7654dd82d28c0e850597b4b5f8e947d2203";
		byte[] data = Hex.decodeHex(src.toCharArray());
		
		byte[] result = rsa.decrypt(rsa.getPrivateKey(), data);
		
		System.out.println(new String(result, "UTF-8"));
	}

	@Test
	public void testEncryptFromFile() throws Exception{
		RSA rsa = new RSA();
		
		String filePath = "d:/yangwk/tmp/rsa/gen/xxx-pub.key";
		FileInputStream input = new FileInputStream(filePath);
		rsa.loadPublicKey(input);
		
		String content = "abcd";
		
		byte[] result = rsa.encrypt(rsa.getPublicKey(), content.getBytes("UTF-8"));
		
		System.out.println(Hex.encodeHexString(result));
		//2906ab1d42fba3c96456d0d6c3f26cb4035fc1c6da44f6721da40f42666ad375c9dc6e4b1e3978393855b44bb07324f4b5940ff30d3fad3651196f8a063587bd80c162b466af619fa47cf61559981340ccf05da8b815780946eab8e2372b502465110651fe0e0aec380355feb6213d8b68127474a1e9e3eae2b257ed27514282


	}

	@Test
	public void testDecryptFromFile() throws Exception {
		RSA rsa = new RSA();
		
		String filePath = "d:/yangwk/tmp/rsa/gen/xxx-pri.key";
		FileInputStream input = new FileInputStream(filePath);
		rsa.loadPKCS8PrivateKey(input);
		
		String src = "2906ab1d42fba3c96456d0d6c3f26cb4035fc1c6da44f6721da40f42666ad375c9dc6e4b1e3978393855b44bb07324f4b5940ff30d3fad3651196f8a063587bd80c162b466af619fa47cf61559981340ccf05da8b815780946eab8e2372b502465110651fe0e0aec380355feb6213d8b68127474a1e9e3eae2b257ed27514282";
		byte[] data = Hex.decodeHex(src.toCharArray());
		
		byte[] result = rsa.decrypt(rsa.getPrivateKey(), data);
		
		System.out.println(new String(result, "UTF-8"));
	}
	
	@Test
	public void testGenKeyPair() throws Exception{
		RSA rsa = new RSA();
		rsa.genKeyPair("d:/yangwk/tmp/rsa/gen/xxx-pri.key", "d:/yangwk/tmp/rsa/gen/xxx-pub.key");
	}
	
	
}

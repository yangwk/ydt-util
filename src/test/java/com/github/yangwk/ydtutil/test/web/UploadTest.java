package com.github.yangwk.ydtutil.test.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import org.junit.Test;

import com.github.yangwk.ydtutil.web.upload.Chunk;
import com.github.yangwk.ydtutil.web.upload.Upload;

public class UploadTest {
	
	@Test
	public void test() throws Exception{
		String uuid = UUID.randomUUID().toString();
		uuid = "a076d1dc-54ef-4481-b886-75a809eba0c1";
		Chunk chunk = new Chunk(null, 1, 2, uuid);
		String absolutePath = "d:/yangwk/tmp/2017-01-05/2";
		Upload upload = new Upload(chunk, absolutePath);
		boolean ok = upload.isFinished();
		System.out.println(ok);
		FileInputStream input = new FileInputStream("d:/yangwk/tmp/test.txt");
		upload.start(input);
		ok = upload.isFinished();
		System.out.println(ok);
	}


	@Test
	public void testFile() {
		File f = new File("d:/yangwk/tmp/uosfdjls/sfd.txt");
		System.out.println(f.getParentFile());
		
		f.getParentFile().mkdirs();
	}
}

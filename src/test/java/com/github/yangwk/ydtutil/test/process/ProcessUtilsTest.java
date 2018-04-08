package com.github.yangwk.ydtutil.test.process;

import com.github.yangwk.ydtutil.process.ProcessUtils;

public class ProcessUtilsTest {

	public static void test(){
		String[] commands = {"c:/windows/system32/calc.exe"};
		String result = ProcessUtils.executeProcess(false, 200, commands);
		System.out.println(result);
	}
	
	public static void main(String[] args) {
		test();
	}
}

package com.github.yangwk.ydtutil.test.antlr;

import java.util.List;

import org.junit.Test;

import com.github.yangwk.ydtutil.antlr.JsLexerParser;

public class TestJs {
	
	
	@Test
	public void testExtractMemberExp() {
		String expression = "update.params.switches[3].switch = 'off' && update.params.currentTemperature >= 15 || update['params'].switch == 'on' "
				+ "|| update.params[update.subfield+'_name'] != 0";
		
		long start = System.currentTimeMillis();
		
		JsLexerParser jsLexerParser = new JsLexerParser(expression);
		List<String> memberExp = jsLexerParser.getMemberExpression();
		
		System.out.println(System.currentTimeMillis() - start);
		
		System.out.println( memberExp );
	}
	
	
}

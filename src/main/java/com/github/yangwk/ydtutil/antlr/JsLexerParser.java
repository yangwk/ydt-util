package com.github.yangwk.ydtutil.antlr;

import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.github.yangwk.ydtutil.antlr.js.JavaScriptLexer;
import com.github.yangwk.ydtutil.antlr.js.JavaScriptParser;

/**
 * JavaScript 语法解析
 * @author yangwk
 *
 */
public class JsLexerParser {
	private CharStream charStream;
	
	public JsLexerParser(String input) {
		this.charStream = CharStreams.fromString(input);
	}
	
	/**
	 * 获取所有成员表达式
	 * @author yangwk
	 * @return
	 */
	public List<String> getMemberExpression(){
	    JavaScriptLexer lexer = new JavaScriptLexer(charStream);
	    
	    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
	    tokenStream.setTokenSource(lexer);	//clear TokenSource
	    JavaScriptParser parser = new JavaScriptParser(tokenStream);
	    
	    JsMemberExpressionListener listener = new JsMemberExpressionListener();
	    parser.addParseListener( listener );	//listener
	    
	    parser.setBuildParseTree(true);
	    parser.program();
	    
		return listener.getMemberExpression();
	}
	
}

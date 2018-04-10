package com.github.yangwk.ydtutil.antlr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.yangwk.ydtutil.antlr.js.JavaScriptParser;
import com.github.yangwk.ydtutil.antlr.js.JavaScriptParserBaseListener;

public class JsMemberExpressionListener extends JavaScriptParserBaseListener{
	
	private Map<Token2, String> token2Map = new HashMap<Token2, String>();
	
	@Override
	public void enterMemberDotExpression(JavaScriptParser.MemberDotExpressionContext ctx) { 
	}
	
	@Override
	public void exitMemberDotExpression(JavaScriptParser.MemberDotExpressionContext ctx) { 
		Token2 token2 = new Token2( ctx.getStart() );
		token2Map.put(token2, ctx.getText());
	}
	
	@Override 
	public void enterMemberIndexExpression(JavaScriptParser.MemberIndexExpressionContext ctx) {
	}
	
	@Override
	public void exitMemberIndexExpression(JavaScriptParser.MemberIndexExpressionContext ctx) { 
		Token2 token2 = new Token2( ctx.getStart() );
		token2Map.put(token2, ctx.getText());
	}
	
	public List<String> getMemberExpression(){
		return new ArrayList<String>( token2Map.values() );
	}
}

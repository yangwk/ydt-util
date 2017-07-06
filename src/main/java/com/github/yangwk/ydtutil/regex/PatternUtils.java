package com.github.yangwk.ydtutil.regex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

public class PatternUtils {
	
	/**
	 * 获取转义Pattern
	 * @author yangwk
	 * @return
	 */
	public static Pattern getEscapePattern(){
		// :!=}^]$*+?{.[-()|\
		return Pattern.compile( "(:|!|=|}|\\^|\\]|\\$|\\*|\\+|\\?|\\{|\\.|\\[|\\-|\\(|\\)|\\||\\\\)" );
	}
	
	/**
	 * 转义正则关键词
	 * @author yangwk
	 * @param escapePattern 转义Pattern
	 * @param source 要进行转义的源字符串
	 * @return 转义后的正则
	 */
	public static String escapeKeywords(Pattern escapePattern, String source){
		if(escapePattern == null){
			escapePattern = getEscapePattern();
		}
		Matcher matcher = escapePattern.matcher(source);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String g = matcher.group();
			if(g.equals(":") || g.equals("!") || g.equals("=") || g.equals("}") ){
				continue ;
			}
			if(g.equals("\\")){
				matcher.appendReplacement(sb, "\\\\\\"+g);
			}else if(g.equals("$")){
				matcher.appendReplacement(sb, "\\\\\\"+g);
			}else{
				matcher.appendReplacement(sb, "\\\\"+g);
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	
	/**
	 * 检索字符串是否包含某个匹配源。与String.indexOf()相比，此方法所用时间要长
	 * @author yangwk
	 * @param content 要检索的字符串
	 * @param sources 匹配源
	 * @return 如果包含，则返回2个长度的数组，数组第0个是第一次匹配的下标，数组第1个是检索到的匹配源。
	 * 如果不包含，则返回null
	 */
	public static Object[] indexOf(final String content, List<String> sources){
		if(CollectionUtils.isEmpty(sources)){
			return null;
		}
		
		Pattern escapePattern = getEscapePattern();
		StringBuilder sb = new StringBuilder();
		int r = 0;
		// (s1)|(s2)|(s3)...
		for(final String s : sources){
			sb.append("(").
				append( escapeKeywords(escapePattern, s) )
				.append(")");
			if(r < sources.size() - 1)
				sb.append("|");
			r ++;
		}
		
		String indexRegex = sb.toString();
		Pattern indexPattern = Pattern.compile( indexRegex );
		Matcher matcher = indexPattern.matcher(content);
		
		String matched = null;
		int index = -1;
		while (matcher.find()) {
			matched = matcher.group();
			index = matcher.start();
			break;
		}
		
		if(matched != null){
			Object[] retval = {index, matched};
			return retval;
		}
		
		return null;
	}
	
}

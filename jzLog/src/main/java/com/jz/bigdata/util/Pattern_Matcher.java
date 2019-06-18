package com.jz.bigdata.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern_Matcher {

	/**
	 * 正则匹配
	 * @param soap 内容
	 * @param rgex 正则
	 * @return 返回匹配的内容
	 */
 	public static String getMatchedContent(String soap,String rgex){  
         Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
         Matcher m = pattern.matcher(soap);  
         while(m.find()){
             return m.group(0);
         }  
         return "";  
    }
 	
 	/**
	 * 正则匹配
	 * @param soap 内容
	 * @param rgex 正则
	 * @return 返回正则（）中匹配的内容
	 */
   	public static String getMatchedContentByParentheses(String soap, String rgex) {
   		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
   		Matcher m = pattern.matcher(soap);
   		while (m.find()) {
   			return m.group(1);
   		}
   		return "";
   	}
   	
   	public static void main(String [] args) {
   		
   		String log ="{\"snapshots\":[{\"snapshot\":\"snapshot\",\"uuid\":\"6gOQWizJQWWrl-KuCwAysQ\",\"version_id\":5040099,\"version\":\"5.4.0\",\"indices\":[\"packet-analysis3\"],\"state\":\"SUCCESS\",\"start_time\":\"2019-05-21T03:05:52.123Z\",\"start_time_in_millis\":1558407952123,\"end_time\":\"2019-05-21T03:07:30.410Z\",\"end_time_in_millis\":1558408050410,\"duration_in_millis\":98287,\"failures\":[],\"shards\":{\"total\":5,\"failed\":0,\"successful\":5}}]}\r\n" + 
   				"";
	   	// 日志过滤
		if (Pattern_Matcher.getMatchedContentByParentheses(log, "\"state\":\"(.*?)\"")!="") {
			System.out.println(Pattern_Matcher.getMatchedContentByParentheses(log, "\"state\":\"(.*?)\""));
		}
	}
}

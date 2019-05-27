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
   		
   		String log ="5157: Windows 筛选平台阻止了数据包。 应用程序信息: 进程 ID: 808 应用程序名称: \\device\\harddiskvolume2\\windows\\system32\\svchost.exe 网络信息: 方向: 入站 源地址: 255.255.255.255 源端口: 67 目标地址: 0.0.0.0 目标端口: 68 协议: 0 筛选器信息: 筛选器运行时 ID: 65691 层名称: 接收/接受 层运行时 ID: 44";
	   	// 日志过滤
		if (Pattern_Matcher.getMatchedContent(log, "Windows 筛选平台阻止了数据包")!=""||Pattern_Matcher.getMatchedContent(log, "Windows 筛选平台已阻止连接")!="") {
			System.out.println(log);
		}
	}
}

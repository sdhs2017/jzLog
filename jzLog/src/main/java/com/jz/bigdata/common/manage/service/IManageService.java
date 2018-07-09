package com.jz.bigdata.common.manage.service;

import java.util.Map;

public interface IManageService {

	public Map<String, String> getDiskUsage(String user,String passwd,String host) ;
	
	public void doCutl(String type,String url) ;

	public void doshell(String url, String host_user, String host_passwd, String host_ip);
	
}

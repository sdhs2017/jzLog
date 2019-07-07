package com.jz.bigdata.common.manage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jz.bigdata.common.manage.service.IManageService;
import com.jz.bigdata.util.ConfigProperty;
import com.jz.bigdata.util.DescribeLog;
import com.jz.bigdata.util.Pattern_Matcher;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/manage")
public class ManageController {
	
	@Resource(name ="configProperty")  
    private ConfigProperty configProperty;

	@Resource(name="manageService")
	private IManageService iManageService;
	
	
	@ResponseBody
	@RequestMapping("/getDiskUsage")
	@DescribeLog(describe="获取服务器磁盘使用情况")
	public Map<String, String> getDiskUsage(HttpServletRequest request) {
		
		Map<String, String> map = iManageService.getDiskUsage(configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		
		return map;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/createRepertory",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="创建备份仓库")
	public String createRepertory() {
		
		// 创建备份仓库
		//String url = "curl -XPUT http://"+configProperty.getEs_path_snapshot()+"/_snapshot/EsBackup -d '{\"type\":\"fs\",\"settings\":{\"location\":\"/home/elsearch/es_backups/my_backup/\"}}'";
		String url = "curl -XPUT http://"+configProperty.getEs_path_snapshot()+"/_snapshot/EsBackup -d '{\"type\":\"fs\",\"settings\":{\"location\":\"/mnt/disk1/elsearch/es_backups/\"}}'";

		//iManageService.doCutl("-XPUT", url);
		iManageService.doshell(url,configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		Map<String, Object> map= new HashMap<>();
		map.put("state", true);
		map.put("msg", "创建备份仓库成功！");
		return JSONArray.fromObject(map).toString();
	}
	
	@ResponseBody
	@RequestMapping(value="/createSnapshotByIndices",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="创建快照")
	public String createSnapshotByIndices() {
		
		Map<String, String> MessageResult= iManageService.doshell("curl -X GET http://"+configProperty.getHost_ip()+":9200/_snapshot/EsBackup/snapshot", configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
//		System.out.println(MessageResult.get("curl -X GET http://"+configProperty.getHost_ip()+":9200/_snapshot/EsBackup/snapshot"));
		
		String resultSuccess=Pattern_Matcher.getMatchedContentByParentheses(MessageResult.get("curl -X GET http://"+configProperty.getHost_ip()+":9200/_snapshot/EsBackup/snapshot"), "\"state\":\"(.*?)\"");

		Map<String, Object> map= new HashMap<>();
		//createRepertory();
		if(resultSuccess.equals("SUCCESS")){
		// 删除快照
		String deleteUrl = "curl -XDELETE http://"+configProperty.getEs_path_snapshot()+"/_snapshot/EsBackup/snapshot";
		//iManageService.doCutl("-XDELETE ", deleteUrl);
		iManageService.doshell(deleteUrl,configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		// 创建快照并指定索引
		String snapshotUrlByIndices = "curl -XPUT http://"+configProperty.getEs_path_snapshot()+"/_snapshot/EsBackup/snapshot -d \'{\"indices\":\""+configProperty.getEs_index()+"\",\"wait_for_completion\":true}\'";
		//iManageService.doCutl("-XPUT", snapshotUrlByIndices);
		Map<String, String> result = iManageService.doshell(snapshotUrlByIndices,configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		map.put("state", true);
		map.put("msg", "日志数据备份成功！");
		return JSONArray.fromObject(map).toString();
		}else{
			map.put("state", false);
			map.put("msg", "日志数据备份未结束！");
			return JSONArray.fromObject(map).toString();
		}
		
		
		
	}
	
	@ResponseBody
	@RequestMapping(value="/restore",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="恢复索引")
	public String restore() {
		
		// 首先关闭需要恢复的索引
		String  closeUrl = "curl -XPOST http://"+configProperty.getEs_path_snapshot()+"/"+configProperty.getEs_index()+"/_close";
		//iManageService.doCutl("-XPOST", closeUrl);
		iManageService.doshell(closeUrl,configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		// 回复快照
		String restoreUrl = "curl -XPOST http://"+configProperty.getEs_path_snapshot()+"/_snapshot/EsBackup/snapshot/_restore";
		//iManageService.doCutl("-XPOST", restoreUrl);
		iManageService.doshell(restoreUrl,configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		// 恢复完成之后打开索引
		String openUrl = "curl -XPOST http://"+configProperty.getEs_path_snapshot()+"/"+configProperty.getEs_index()+"/_open";
		//iManageService.doCutl("-XPOST", openUrl);
		iManageService.doshell(openUrl,configProperty.getHost_user(), configProperty.getHost_passwd(), configProperty.getHost_ip());
		
		Map<String, Object> map= new HashMap<>();
		map.put("state", true);
		map.put("msg", "恢复备份数据成功！");
		return JSONArray.fromObject(map).toString();
		
	}
}

package com.jz.bigdata.business.logAnalysis.zts.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.business.logAnalysis.log.LogType;
import com.jz.bigdata.business.logAnalysis.log.controller.LogController;
import com.jz.bigdata.business.logAnalysis.log.entity.Log4j;
import com.jz.bigdata.business.logAnalysis.log.entity.Syslog;
import com.jz.bigdata.business.logAnalysis.log.entity.Unknown;
import com.jz.bigdata.business.logAnalysis.log.entity.Winlog;
import com.jz.bigdata.business.logAnalysis.log.entity.ZtsSyslog;
import com.jz.bigdata.business.logAnalysis.zts.service.impl.ZtsLogServiceImpl;
import com.jz.bigdata.util.ConfigProperty;
import com.jz.bigdata.util.DescribeLog;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/log")
public class ZtsLogController extends LogController{
	
	private static Logger logger = Logger.getLogger(ZtsLogController.class);

	@Resource(name ="configProperty")  
    ConfigProperty configProperty;
	
	@Resource(name="ZtsLogServiceImpl")
	private ZtsLogServiceImpl ztsLogService;
	
	@ResponseBody
	@RequestMapping(value="/createIndexAndMapping",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="初始化数据结构")
	public String createIndexAndMapping(HttpServletRequest request) {
		Map<String, Object> map= new HashMap<>();
		try {
			ztsLogService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_WINLOG, new Winlog().toMapping());
			ztsLogService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_LOG4J, new Log4j().toMapping());
			ztsLogService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_UNKNOWN, new Unknown().toMapping());
			// 定制业务
			ztsLogService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_SYSLOG, new ZtsSyslog().toMapping());
			
			map.put("state", true);
			map.put("msg", "数据结构初始化成功！");
			logger.info(map);
			return JSONArray.fromObject(map).toString();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			map.put("state", false);
			map.put("msg", "数据结构初始化失败！");
			map.put("message", e.getMessage());
			logger.error(map);
			return JSONArray.fromObject(map).toString();
		}
		
	}
}

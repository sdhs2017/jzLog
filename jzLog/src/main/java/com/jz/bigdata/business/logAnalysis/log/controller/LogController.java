package com.jz.bigdata.business.logAnalysis.log.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jz.bigdata.business.logAnalysis.log.LogType;
import com.jz.bigdata.business.logAnalysis.log.entity.Log4j;
import com.jz.bigdata.business.logAnalysis.log.entity.Mysql;
import com.jz.bigdata.business.logAnalysis.log.entity.PacketFilteringFirewal;
import com.jz.bigdata.business.logAnalysis.log.entity.Syslog;
import com.jz.bigdata.business.logAnalysis.log.entity.Unknown;
import com.jz.bigdata.business.logAnalysis.log.entity.Winlog;
import com.jz.bigdata.business.logAnalysis.log.service.IlogService;
import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.alarm.service.IAlarmService;
import com.jz.bigdata.common.equipment.EquipmentConstant;
import com.jz.bigdata.common.equipment.entity.Equipment;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.safeStrategy.entity.SafeStrategy;
import com.jz.bigdata.common.safeStrategy.service.ISafeStrategyService;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;
import com.jz.bigdata.util.DescribeLog;
import com.jz.bigdata.util.Sendmail;

import net.sf.json.JSONArray;

@Controller
//@RequestMapping("/log")
public class LogController {

	@Resource(name="logService")
	private IlogService logService;
	
	@Resource(name = "EquipmentService")
	private IEquipmentService equipmentService;
	
	@Resource(name ="configProperty")  
    private ConfigProperty configProperty;
	
	@Resource(name ="SafeStrategyService")  
    private ISafeStrategyService safeStrategyService;
	
	@Resource(name="AlarmService")
	private IAlarmService alarmService;
	
	@Resource(name ="UsersService")
	private IUsersService usersService;
	
	@Autowired protected ClientTemplate clientTemplate;
//	/**
//	 * @param request
//	 * @return 插入索引数据
//	 */
//	@ResponseBody
//	@RequestMapping("/insert")
//	public void insert() {
//		String log = "2017-09-10 11:00:34,132 [INFO] 192.168.4.123  [com.jz.bigdata.common.department.dao.IDepartmentDao.selectAll] debug()[139] [210348] \r\n";
//		Log4j log4j = new Log4j(log);
//		Gson gson = new Gson();
//		String json = gson.toJson(log4j);
//		this.logService.insert("estest", "Log4j", json);
//	}
	
	/**
	 * @param request
	 * @return 获取索引数据ById
	 */
	@ResponseBody
	@RequestMapping("/getListById")
	@DescribeLog(describe="通过日志ID获取日志信息")
	public String getListById(HttpServletRequest request) {
		//index=estest&type=Log4j&id=AV6FBY7KXlvRoY9aku1N
		String index = request.getParameter("index");
		String type = request.getParameter("type");
		String id = request.getParameter("id");
//		List<Map<String, Object>> list = logService.searchById("estest", "Log4j", "AV6FBY7KXlvRoY9aku1N");
//		String result = JSONArray.fromObject(list).toString();
//		String result = logService.searchById("estest", "Log4j", "AV6FBY7KXlvRoY9aku1N");
		String result = logService.searchById(index, type, id);
		System.out.println("-----------------result:----------------------");
		System.out.println(result);
		return result;
	}
	
	/**
	 * @param request
	 * @return 删除结果String（DELETED、NOT_FOUND、NOOP）
	 */
	@ResponseBody
	@RequestMapping("/deleteById")
	@DescribeLog(describe="通过日志ID删除日志信息")
	public String deleteById(HttpServletRequest request) {
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String [] types = type.split(",");
		String [] ids = id.split(",");
		String result ="false";
		if (types.length<2) {
			for(int i=0;i<ids.length;i++) {
				result = logService.deleteById(configProperty.getEs_index(), types[0], ids[i]);
			}
		}else {
			for(int i=0;i<ids.length;i++) {
				result = logService.deleteById(configProperty.getEs_index(), types[i], ids[i]);
			}
			
		}
		
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/createIndexAndMapping",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="初始化数据结构")
	public String createIndexAndMapping(HttpServletRequest request) {
		Map<String, Object> map= new HashMap<>();
		try {
			logService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_SYSLOG, new Syslog().toMapping());
			logService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_WINLOG, new Winlog().toMapping());
			logService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_LOG4J, new Log4j().toMapping());
			logService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_MYSQLLOG, new Mysql().toMapping());
			logService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG, new PacketFilteringFirewal().toMapping());
			logService.createIndexAndmapping(configProperty.getEs_index(),LogType.LOGTYPE_UNKNOWN, new Unknown().toMapping());
			
			map.put("state", true);
			map.put("msg", "数据结构初始化成功！");
			return JSONArray.fromObject(map).toString();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			map.put("state", false);
			map.put("msg", "数据结构初始化失败！");
			return JSONArray.fromObject(map).toString();
		}
		
	}
	
	
	/**
	 * @param request
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getListByType")
	@DescribeLog(describe="通过日志类型获取数据")
	public List<Map<String, Object>> getListByType(HttpServletRequest request) {
		String index = request.getParameter("index");
		String type = request.getParameter("type");
		
		List<Map<String, Object>> list = logService.index(index, type);
		
		return list;
		
	}
	
	/**
	 * @param request
	 * 统计各个日志级别的数据量
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getCountGroupByParam")
	@DescribeLog(describe="读取日志级别数据量")
	public List<Map<String, Object>> getCountGroupByParam(HttpServletRequest request) {
//		String index = request.getParameter("index");
		String index = configProperty.getEs_index();
		String type = request.getParameter("type");
		String param = request.getParameter("param");
		String time = request.getParameter("time");
		String equipmentid = request.getParameter("equipmentid");
		
		Map<String, String> map = new HashMap<>();
		if (equipmentid!=null&&!equipmentid.equals("")) {
			map.put("equipmentid", equipmentid);
		}
		if (time!=null&&!time.equals("")) {
			map.put("logdate", time);
		}
		
		List<Map<String, Object>> list = logService.groupBy(index, type, param, map);
		
		return list;
	}
	
	/**
	 * @param request
	 * 统计事件的数据量
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getCountGroupByEvent")
	@DescribeLog(describe="统计事件的数据量")
	public List<Map<String, Object>> getCountGroupByEvent(HttpServletRequest request) {
		String index = configProperty.getEs_index();
		String type = request.getParameter("type");
		String dates = request.getParameter("param");
		String equipmentid = request.getParameter("equipmentid");
		String groupby = "event_type";
		
		List<Map<String, Object>> list = logService.getEventListGroupByEventType(index, type, dates, equipmentid, groupby);
		
		return list;
	}
	
	/**
	 * @param request
	 * 统计各个事件的数据量
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getCountGroupByEventType")
	@DescribeLog(describe="统计各个事件的数据量")
	public List<Map<String, Object>> getCountGroupByEventType(HttpServletRequest request) {
		String index = configProperty.getEs_index();
		String type = request.getParameter("type");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String param = request.getParameter("param");
		String equipmentid = request.getParameter("equipmentid");
		String [] hours = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
		// 0全部，1高危，2中等，3普通
		for(int i=0;i<4;i++) {
			List<Map<String, Object>> list1 = logService.getEventListGroupByTime(index, type, param,equipmentid,"event_type",i);
			Map<String, Object> map = new HashMap<>();
			for(String hour : hours) {
				map.put(hour, list1.get(0).get(hour)!=null?list1.get(0).get(hour):0);
			}
			list.add(map);
		}
		
		return list;
	}
	
	
	/**
	 * @param request
	 * 统计各时间段的数据量
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getCountGroupByTime")
	@DescribeLog(describe="读取各时段日志数据量")
	public List<Map<String, Object>> getCountGroupByTime(HttpServletRequest request) {
		String index = configProperty.getEs_index();
		String type = request.getParameter("type");
		String param = request.getParameter("param");
		String equipmentid = request.getParameter("equipmentid");
		String [] hours = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
		List<Map<String, Object>> list = logService.getListGroupByTime(index, type, param,equipmentid);
		
		Map<String, Object> map = new HashMap<>();
		for(String hour : hours) {
			map.put(hour, list.get(0).get(hour)!=null?list.get(0).get(hour):0);
		}
		list.clear();
		list.add(map);
		return list;
	}
	
	/**
	 * @param request
	 * 统计某时间段内的事件数量
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getCountGroupByEventstype")
	@DescribeLog(describe="统计某时间段内的事件数量")
	public List<Map<String, Object>> getCountGroupByEventstype(HttpServletRequest request) {
		String index = configProperty.getEs_index();
		String type = request.getParameter("type");
		String equipmentid = request.getParameter("equipmentid");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date enddate = new Date();
		String endtime = format.format(enddate);
		DecimalFormat df = new DecimalFormat("#.00");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<SafeStrategy> safelist = safeStrategyService.selectByEquipmentId(equipmentid);
		for (SafeStrategy safeStrategy : safelist) {
			String dates = safeStrategy.getTime();
			String event_type = safeStrategy.getEvent_type();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(enddate);
			calendar.add(Calendar.MINUTE, -Integer.valueOf(dates));
			Date startdate = calendar.getTime();
			String starttime = format.format(startdate);
			List<Map<String, Object>> loglist = logService.getListGroupByEvent(index, type, equipmentid,event_type,starttime,endtime);
			
			if (!loglist.get(0).isEmpty()) {
				float per = Float.valueOf(loglist.get(0).get(safeStrategy.getEvent_type()).toString())/safeStrategy.getNumber();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("event_type", safeStrategy.getEvent_type());
				map.put("per",df.format(per*100));
				map.put("starttime",starttime);
				map.put("endtime", endtime);
				list.add(map);
			}else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("event_type", safeStrategy.getEvent_type());
				map.put("per",df.format(0));
				map.put("starttime",starttime);
				map.put("endtime", endtime);
				list.add(map);
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getListOrderByParam")
	@DescribeLog(describe="获取排序后的日志数据")
	public String getListOrderByParam(HttpServletRequest request) {
//		String index = request.getParameter("index");
		String type = request.getParameter("type");
		String param = request.getParameter("param");
		String  order = request.getParameter("order");
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		
		List<Map<String, Object>> list = logService.orderBy(configProperty.getEs_index(), type, param, order,page,size);
		Map<String, Object> map = new HashMap<>();
		map = list.get(0);
		list.remove(0);
		map.put("list", list);
		
		String result = JSONArray.fromObject(map).toString();
		
		System.out.println("---------------result----------------");
		System.err.println(result);
		return result;
	}
	
	
	
	/**
	 * 通过设备id获取该设备日志列表
	 * @param requestt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLogListByEquipment.do", produces = "application/json; charset=utf-8")
	@DescribeLog(describe="条件获取设备日志数据")
	public String getLogListByEquipment(HttpServletRequest request,Equipment equipment) {
		equipment=equipmentService.selectOneEquipment(equipment);
		String starttime = request.getParameter("startTime");
		String endtime = request.getParameter("endTime");
		String level = request.getParameter("level");
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		
		
		String type = null;
		
		int logType = equipment.getLogType();
		if(EquipmentConstant.LOGTYPE_SYSLOG==logType){
			type = EquipmentConstant.LOGTYPE_SYSLOG_EN;
		}else if(EquipmentConstant.LOGTYPE_WMI==logType){
			type = EquipmentConstant.LOGTYPE_WMI_EN;
		}else if(EquipmentConstant.LOGTYPE_SNMP==logType){
			type = EquipmentConstant.LOGTYPE_SNMP_EN;
		}else if(EquipmentConstant.LOGTYPE_LOG4J==logType){
			type = EquipmentConstant.LOGTYPE_LOG4J_EN;
		}else if(EquipmentConstant.LOGTYPE_MYSQL==logType){
			type = EquipmentConstant.LOGTYPE_MYSQL_EN;
		}else if(EquipmentConstant.LOGTYPE_FIREWALL==logType){
			type = EquipmentConstant.LOGTYPE_FIREWALL_EN;
		}else{
			type = null;
		}
		
		String equipmentId = equipment.getId();
		
		Map<String, String> map = new HashMap<>();
		map.put("equipmentid", equipmentId);
		if (level!=null&&!level.equals("")) {
			map.put("operation_level", level);
		}
		if (starttime==null) {
			starttime = "";
		}
		if (endtime==null) {
			endtime = "";
		}
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add(type);
		String [] types = null;
		if (type!=null) {
			types = arrayList.toArray(new String[arrayList.size()]);
		}
		
		List<Map<String, Object>> list = logService.getListByMap(configProperty.getEs_index(), types, starttime, endtime, map,page,size);
		
		Map<String, Object> allmap = new HashMap<>();
		allmap = list.get(0);
		list.remove(0);
		allmap.put("list", list);
		String result = JSONArray.fromObject(allmap).toString();
		
		String replace=result.replace("\\\\005", "<br/>");
		System.out.println("---------------result----------------");
		System.err.println(result);
		return replace;
	}
	
	/**
	 * 通过关键字获取日志信息
	 * @param requestt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLogListByContent",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="查询日志数据")
	public String getLogListByContent(HttpServletRequest request,HttpSession session) {
		
		String keyWords = request.getParameter("words");
		Object userrole = session.getAttribute(Constant.SESSION_USERROLE);
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		System.err.println("-----------关键词：	"+keyWords);
		
		String[] types = {LogType.LOGTYPE_LOG4J,LogType.LOGTYPE_WINLOG,LogType.LOGTYPE_SYSLOG,LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG,LogType.LOGTYPE_UNKNOWN,LogType.LOGTYPE_MYSQLLOG};
		

		List<Map<String, Object>> list =null;
		
		try {
			if (userrole.equals("1")) {
				list = logService.getListByContent(configProperty.getEs_index(), types, keyWords,page,size);
			}else {
				list = logService.getListByContent(configProperty.getEs_index(), types, keyWords,session.getAttribute(Constant.SESSION_USERID).toString(),page,size);
			}
			
		} catch (Exception e) {
			
		}

		Map<String, Object> map = new HashMap<>();
		map = list.get(0);
		list.remove(0);
		map.put("list", list);
		
		String result = JSONArray.fromObject(map).toString();
//		System.out.println("result ="+result);
		String replace=result.replace("\\\\005", "<br/>");
//		System.out.println("---------------result----------------");
		
		return replace;
	}
	
	/**
	 * 组合查询
	 * @param requestt
	 * @author jiyourui
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/getLogListByBlend",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="组合查询日志数据")
	public String getLogListByBlend(HttpServletRequest request,HttpSession session) {
		// receive parameter
		
		String type = request.getParameter("type");
		String starttime = request.getParameter("startTime");
		String endtime = request.getParameter("endTime");
		String ip = request.getParameter("ip");
		String hostname = request.getParameter("hostname");
		String operation_level = request.getParameter("operation_level");
		String event_level = request.getParameter("event_level");
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		Object userrole = session.getAttribute(Constant.SESSION_USERROLE);
		
		Map<String, String> map = new HashMap<String, String>();
		if (starttime!=null&&!starttime.equals("")) {
			map.put("starttime", starttime);
		}
		if (endtime!=null&&!endtime.equals("")) {
			map.put("endtime", endtime);
		}
		if (ip!=null&&!ip.equals("")) {
			map.put("ip", ip);
		}
		if (hostname!=null&&!hostname.equals("")) {
			map.put("hostname", hostname);
		}
		if (operation_level!=null&&!operation_level.equals("")) {
			map.put("operation_level", operation_level);
		}
		if (event_level!=null&&!event_level.equals("")) {
			map.put("event_level", event_level);
		}
		
		ArrayList<String> arrayList = new ArrayList<>();
		List<Map<String, Object>> list =null;
		
		if (type!=null&&!type.equals("")) {
			arrayList.add(type);
			String [] types = arrayList.toArray(new String[arrayList.size()]);
			if (userrole.equals("1")) {
				list = logService.getListByBlend(configProperty.getEs_index(), types, map,page,size);
			}else {
				list = logService.getListByBlend(configProperty.getEs_index(), types, map,session.getAttribute(Constant.SESSION_USERID).toString(),page,size);
			}
		}else {
			String[] types = {LogType.LOGTYPE_LOG4J,LogType.LOGTYPE_WINLOG,LogType.LOGTYPE_SYSLOG,LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG,LogType.LOGTYPE_UNKNOWN,LogType.LOGTYPE_MYSQLLOG};
			if (userrole.equals("1")) {
				list = logService.getListByBlend(configProperty.getEs_index(), types, map,page,size);
			}else {
				list = logService.getListByBlend(configProperty.getEs_index(), types, map,session.getAttribute(Constant.SESSION_USERID).toString(),page,size);
			}
		}
		Map<String, Object> allmap = new HashMap<>();
		allmap = list.get(0);
		list.remove(0);
		allmap.put("list", list);
		String result = JSONArray.fromObject(allmap).toString();
		String replace=result.replace("\\\\005", "<br/>");
		
		return replace;
	}
	
	
	/**
	 * 组合查询日志事件
	 * @param requestt
	 * @author jiyourui
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/getEventListByBlend",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="组合查询日志事件")
	public String getEventListByBlend(HttpServletRequest request,HttpSession session) {
		// receive parameter
		
		String type = request.getParameter("type");
		String starttime = request.getParameter("startTime");
		String endtime = request.getParameter("endTime");
		String ip = request.getParameter("ip");
		String equipmentid = request.getParameter("equipmentid");
		String hostname = request.getParameter("hostname");
		String event_level = request.getParameter("event_level");
		String event_levels = request.getParameter("event_levels");
		String event_type = request.getParameter("event_type");
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		Object userrole = session.getAttribute(Constant.SESSION_USERROLE);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("event", "event");
		if (starttime!=null&&!starttime.equals("")) {
			map.put("starttime", starttime);
		}
		if (endtime!=null&&!endtime.equals("")) {
			map.put("endtime", endtime);
		}
		if (ip!=null&&!ip.equals("")) {
			map.put("ip", ip);
		}
		if (hostname!=null&&!hostname.equals("")) {
			map.put("hostname", hostname);
		}
		if (event_level!=null&&!event_level.equals("")) {
			map.put("event_level", event_level);
		}
		if (event_levels!=null&&!event_levels.equals("")) {
			map.put("event_levels", event_levels);
		}
		if (event_type!=null&&!event_type.equals("")) {
			map.put("event_type", event_type);
		}
		if (equipmentid!=null&&!equipmentid.equals("")) {
			map.put("equipmentid", equipmentid);
		}
		
		List<Map<String, Object>> list =null;
		
		String[] types = {LogType.LOGTYPE_LOG4J,LogType.LOGTYPE_WINLOG,LogType.LOGTYPE_SYSLOG,LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG,LogType.LOGTYPE_UNKNOWN,LogType.LOGTYPE_MYSQLLOG};
		if (userrole.equals("1")) {
			list = logService.getListByBlend(configProperty.getEs_index(), types, map,page,size);
		}else {
			list = logService.getListByBlend(configProperty.getEs_index(), types, map,session.getAttribute(Constant.SESSION_USERID).toString(),page,size);
		}
		
		Map<String, Object> allmap = new HashMap<>();
		allmap = list.get(0);
		list.remove(0);
		allmap.put("list", list);
		
		String result = JSONArray.fromObject(allmap).toString();
		String replace=result.replace("\\\\005", "<br/>");
		
		return replace;
	}
	

	/**
	 * 通过日志级别获取日志信息
	 * @param requestt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLogListByLevel",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="通过日志级别查询数据")
	public String getLogListByLevel(HttpServletRequest request) {
		
		String keyWords = request.getParameter("words");
		
		System.out.println("-----------关键词：	"+keyWords);
		
		String[] types = {LogType.LOGTYPE_LOG4J,LogType.LOGTYPE_WINLOG,LogType.LOGTYPE_SYSLOG,LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG,LogType.LOGTYPE_UNKNOWN,LogType.LOGTYPE_MYSQLLOG};
		
		Map<String, String> map = new HashMap<>();
		map.put("operation_level", keyWords);
		List<Map<String, Object>> list =null;
		try {
			list = logService.getListByMap(configProperty.getEs_index(), types, map);
		} catch (Exception e) {
		}
		String result = JSONArray.fromObject(list).toString();
		String replace=result.replace("\\\\005", "<br/>");
		
		return replace;
	}
	
	/**
	 * 获取索引数据的数量
	 * @param requestt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getIndicesCount",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="获取索引数据的数量")
	public String getIndicesCount(HttpServletRequest request) {
		
		
		String[] types = {LogType.LOGTYPE_LOG4J,LogType.LOGTYPE_WINLOG,LogType.LOGTYPE_SYSLOG,LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG,LogType.LOGTYPE_UNKNOWN,LogType.LOGTYPE_MYSQLLOG};
		String equipmentid = request.getParameter("equipmentid");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try {
			Map<String, String> mappram = new HashMap<>();
			mappram.put("operation_level", "error");
			if (equipmentid!=null&&!equipmentid.equals("")) {
				mappram.put("equipmentid", equipmentid);
			}
			long count = 0;
			count = logService.getCount(configProperty.getEs_index(), types, mappram);
			map.put("indiceserror", count);
		} catch (Exception e) {
			map.put("indiceserror", "获取异常");
		}
		
		try {
			long count = 0;
			
			if (equipmentid!=null&&!equipmentid.equals("")) {
				Map<String, String> mappram = new HashMap<>();
				mappram.put("equipmentid", equipmentid);
				count = logService.getCount(configProperty.getEs_index(), types,mappram);
			}else {
				count = logService.getCount(configProperty.getEs_index(), types,null);
			}
			
			map.put("indices", count);
		} catch (Exception e) {
			map.put("indices", "获取异常");
		}
		list.add(map);
		String result = JSONArray.fromObject(list).toString();
		
		return result;
	}
	
	/**
	 * 获取索引数据的数量
	 * @param requestt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEventsCount",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="获取事件数据的数量")
	public String getEventsCount(HttpServletRequest request) {
		
		
		String[] types = {LogType.LOGTYPE_LOG4J,LogType.LOGTYPE_WINLOG,LogType.LOGTYPE_SYSLOG,LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG,LogType.LOGTYPE_UNKNOWN,LogType.LOGTYPE_MYSQLLOG};
		String equipmentid = request.getParameter("equipmentid");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try {
			Map<String, String> mappram = new HashMap<>();
			mappram.put("event", "event");
			mappram.put("event_level", "error");
			if (equipmentid!=null&&!equipmentid.equals("")) {
				mappram.put("equipmentid", equipmentid);
			}
			long count = 0;
			count = logService.getCount(configProperty.getEs_index(), types, mappram);
			map.put("eventserror", count);
		} catch (Exception e) {
			map.put("eventserror", "获取异常");
		}
		
		try {
			long count = 0;
			Map<String, String> mappram = new HashMap<>();
			mappram.put("event", "event");
			if (equipmentid!=null&&!equipmentid.equals("")) {
				mappram.put("equipmentid", equipmentid);
				count = logService.getCount(configProperty.getEs_index(), types,mappram);
			}else {
				count = logService.getCount(configProperty.getEs_index(), types,mappram);
			}
			
			map.put("events", count);
		} catch (Exception e) {
			map.put("events", "获取异常");
		}
		list.add(map);
		String result = JSONArray.fromObject(list).toString();
		
		return result;
	}
	
	/**
	 * 导入历史数据
	 * @param requestt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/importHistoricalData",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="导入历史数据")
	public String importHistoricalData(HttpServletRequest request) {
		
		String filepath = request.getParameter("filepath");
		String DEFAULT_REGEX = "^ java.|^   at";
		//日志类型
		String logType="unknown";
		Log4j log4j = null;
		Winlog winlog;
		PacketFilteringFirewal packetFilteringFirewal;
		Mysql mysql;
		Syslog syslog;
		
		//初始化：获取设备列表、map
		Map<String, Equipment> equipmentMap = equipmentService.selectAllEquipment();
				
		Set<String> ipadressSet = equipmentService.selectAllIPAdress();
				
		Map<String, String> equipmentLogType = equipmentService.selectLog_level();
				
		Set<String> eventType = alarmService.selectByEmailState();
		
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<IndexRequest> requests = new ArrayList<IndexRequest>();
		
		File file = new File(filepath);
		
		try{
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
			String json;
			
			//	资产、ip地址
			Equipment equipment;
			String ipadress;
			
			StringBuilder builder = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String log = null;
            
            //使用readLine方法，一次读一行
            while((log = br.readLine())!=null){
				
				// 日志过滤正则
				// log4j日志信息过滤条件
				Pattern facility_pattern = Pattern.compile("local3:");
				Matcher facility_matcher = facility_pattern.matcher(log);
				Pattern pattern = Pattern.compile(DEFAULT_REGEX);
				// 防火墙-包过滤日志信息过滤条件
				Pattern logtype_pattern = Pattern.compile("logtype=1");
				Matcher logtype_matcher = logtype_pattern.matcher(log);
				Pattern dmg_pattern = Pattern.compile("包过滤日志");
				Matcher dmg_matcher = dmg_pattern.matcher(log);
				// 防火墙-其他日志信息过滤条件
				Pattern logothertype_pattern = Pattern.compile("logtype=");
				Matcher logtotherype_matcher = logothertype_pattern.matcher(log);
				Pattern dmgother_pattern = Pattern.compile("dsp_msg=");
				Matcher dmgother_matcher = dmgother_pattern.matcher(log);
				// windows安全审计
				Pattern win2008pattern = Pattern.compile("Security-Auditing:");
				Matcher win2008matcher = win2008pattern.matcher(log);
				Pattern win2003pattern = Pattern.compile("Security:");
				Matcher win2003matcher = win2003pattern.matcher(log);
				// mysql日志
				Pattern mysqlpattern = Pattern.compile("timestamp");
				Matcher mysqlmatcher = mysqlpattern.matcher(log);
				
				if (facility_matcher.find()) {
					logType = LogType.LOGTYPE_LOG4J;
					synchronized (log) {
						String logleft = log.substring(0, log.indexOf(facility_matcher.group(0))+facility_matcher.group(0).length());
						
						Matcher m = pattern.matcher(log.replace(logleft, ""));
						//判断是否符合正则表达式 如果符合，表明这是一条开始数据
						if(m.find()) {
							log = log.replace(logleft, "");
							//添加数据
							builder.append(" \\005 "+log);
						}else {
							//添加builder
							if (builder.length()!=0) {
								//进入范式化
								try {
									log4j = new Log4j(builder.toString());
								} catch (Exception e) {
									continue;
								}
								ipadress = log4j.getIp();
								//判断是否在资产ip地址池里
								if(ipadressSet.contains(ipadress)){
									//判断是否在已识别资产里————日志类型可识别
									equipment=equipmentMap.get(log4j.getIp() +logType);
									if(equipment!=null){
										log4j.setUserid(equipment.getUserId());
										log4j.setDeptid(String.valueOf(equipment.getDepartmentId()));
										log4j.setEquipmentname(equipment.getName());
										log4j.setEquipmentid(equipment.getId());
										json = gson.toJson(log4j);
										requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_LOG4J, json));
									}else{
										log4j.setUserid(LogType.LOGTYPE_UNKNOWN);
										log4j.setDeptid(LogType.LOGTYPE_UNKNOWN);
										log4j.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
										log4j.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
										json = gson.toJson(log4j);
										requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_LOG4J, json));
									}
								}else{
									//不在资产ip池里，暂不处理
									//TODO
								}

								//清空数据
								builder.delete(0, builder.length());
							}
							builder.append(log);
							
						}
					}
				}else if (logtype_matcher.find()&&dmg_matcher.find()) {
					logType = LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG;
					try {
						packetFilteringFirewal = new PacketFilteringFirewal(log);
					} catch (Exception e) {
						continue;
					}
					ipadress = packetFilteringFirewal.getIp();
					if (ipadressSet.contains(ipadress)) {
						equipment=equipmentMap.get(packetFilteringFirewal.getIp() +logType);
						if (equipment!=null) {
							packetFilteringFirewal.setUserid(equipment.getUserId());
							packetFilteringFirewal.setDeptid(String.valueOf(equipment.getDepartmentId()));
							packetFilteringFirewal.setEquipmentid(equipment.getId());
							packetFilteringFirewal.setEquipmentname(equipment.getName());
							json = gson.toJson(packetFilteringFirewal);
							requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG, json));
						}else {
							packetFilteringFirewal.setUserid(LogType.LOGTYPE_UNKNOWN);
							packetFilteringFirewal.setDeptid(LogType.LOGTYPE_UNKNOWN);
							packetFilteringFirewal.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							packetFilteringFirewal.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(packetFilteringFirewal);
							requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG, json));
						}
					}else {
						//不在资产ip池里，暂不处理
					}
					//es暂无防火墙包过滤日志对应的mapping，暂未入库es
				}else if(logtotherype_matcher.find()&&dmgother_matcher.find()){
					//防火墙、不包括包过滤日志，暂不处理
					System.out.println("-------不做处理-------------");
				}/*else if (mysqlmatcher.find()) {
					logType = LogType.LOGTYPE_MYSQLLOG;
					mysql = new Mysql(log);
					ipadress = mysql.getIp();
					if (ipadressSet.contains(ipadress)) {
						equipment=equipmentMap.get(mysql.getIp() +logType);
						if (equipment!=null) {
							mysql.setUserid(equipment.getUserId());
							mysql.setDeptid(String.valueOf(equipment.getDepartmentId()));
							mysql.setEquipmentname(equipment.getName());
							mysql.setEquipmentid(equipment.getId());
							json = gson.toJson(mysql);
							requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_MYSQLLOG, json));
						}else {
							mysql.setUserid(LogType.LOGTYPE_UNKNOWN);
							mysql.setDeptid(String.valueOf(LogType.LOGTYPE_UNKNOWN));
							mysql.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							mysql.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(mysql);
							requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_MYSQLLOG, json));
						}
					}else {
						//不在资产ip池里，暂不处理
					}
				}*/
				else if(win2003matcher.find()||win2008matcher.find()){
					//windows、evtsys组件收集日志
					logType = LogType.LOGTYPE_WINLOG;
					try {
						winlog = new Winlog(log);
					} catch (Exception e) {
						continue;
					}
					ipadress = winlog.getIp();
					//判断是否在资产ip地址池里
					if(ipadressSet.contains(ipadress)){
						//判断是否在已识别资产里————日志类型可识别
						equipment=equipmentMap.get(winlog.getIp() +logType);
						if(equipment != null){
							if (equipmentLogType.get(equipment.getId()).indexOf(winlog.getOperation_level().toLowerCase())!=-1) {
								winlog.setUserid(equipment.getUserId());
								winlog.setDeptid(String.valueOf(equipment.getDepartmentId()));
								winlog.setEquipmentname(equipment.getName());
								winlog.setEquipmentid(equipment.getId());
								json = gson.toJson(winlog);
								requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_WINLOG, json));
							}
						}else{
							winlog.setUserid(LogType.LOGTYPE_UNKNOWN);
							winlog.setDeptid(LogType.LOGTYPE_UNKNOWN);
							winlog.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							winlog.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(winlog);
							requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_WINLOG, json));
						}
					}else{
						//不在资产ip池里，暂不处理
					}
				}else {
					logType = LogType.LOGTYPE_SYSLOG;
					try {
						syslog = new Syslog(log);
					} catch (Exception e) {
						continue;
					}
					ipadress = syslog.getIp();
					//判断是否在资产ip地址池里
					if(ipadressSet.contains(ipadress)){
						//判断是否在已识别资产里————日志类型可识别
						equipment = equipmentMap.get(syslog.getIp() +logType);
						if(null != equipment){
							if (equipmentLogType.get(equipment.getId()).indexOf(syslog.getOperation_level().toLowerCase())!=-1) {
								syslog.setUserid(equipment.getUserId());
								syslog.setDeptid(String.valueOf(equipment.getDepartmentId()));
								syslog.setEquipmentid(equipment.getId());
								syslog.setEquipmentname(equipment.getName());
								if (eventType.contains(syslog.getEvent_type())) {
									Sendmail sendmail = new Sendmail(syslog.getIp(), syslog.getEquipmentname(), syslog.getEvent_des(), usersService.selectById(syslog.getUserid()).getEmail());
								}
								json = gson.toJson(syslog);
								requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_SYSLOG, json));
							}
						}else{
							//在资产ip地址池里，但是无法识别日志类型
							syslog.setUserid(LogType.LOGTYPE_UNKNOWN);
							syslog.setDeptid(LogType.LOGTYPE_UNKNOWN);
							syslog.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							syslog.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(syslog);
							requests.add(clientTemplate.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_UNKNOWN, json));
						}
					}else{
						//不在资产ip池里，暂不处理
					}
				}
				// 根据配置参数进行批量提交
				if (requests.size()==configProperty.getEs_bulk()) {
					clientTemplate.bulk(requests);
					requests.clear();
				}
            }
            br.close();
            map.put("result", "导入历史数据成功！");
        }catch(Exception e){
        	requests.clear();
            map.put("result", "导入历史数据失败！");
            e.printStackTrace();
        }finally {
			if (requests.size()>0) {
				clientTemplate.bulk(requests);
			}
		}
		
		list.add(map);
		String result = JSONArray.fromObject(list).toString();
		
		return result;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

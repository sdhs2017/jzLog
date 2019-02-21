package com.jz.bigdata.business.logAnalysis.collector.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.business.logAnalysis.collector.service.ICollectorService;
import com.jz.bigdata.common.alarm.service.IAlarmService;
import com.jz.bigdata.common.assets.service.IAssetsService;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;
import com.jz.bigdata.util.DescribeLog;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/collector")
public class CollectorController {

	@Resource(name = "CollectorService")
	private ICollectorService collectorService;

	@Resource(name = "EquipmentService")
	private IEquipmentService equipmentService;

	@Resource(name = "configProperty")
	private ConfigProperty configProperty;

	@Resource(name = "AlarmService")
	private IAlarmService alarmService;

	@Resource(name = "UsersService")
	private IUsersService usersService;

	@Autowired
	protected ClientTemplate clientTemplate;

//	@Resource
//	private MascanCollector mascanCollector;
	@Resource(name="assetsService")
	private IAssetsService masscanipService;

	// 获取采集器开启或关闭状态，true为开启，false为关闭
	@ResponseBody
	@RequestMapping("/getCollectorState")
	public boolean getCollectorState() {
		return collectorService.getKafkaCollectorState();
	}

	// 开启采集器
	@ResponseBody
	@RequestMapping(value = "/startCollectorState", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "开启数据采集器")
	public String startKafkaCollector() {
		boolean result = collectorService.startKafkaCollector(equipmentService, clientTemplate, configProperty,
				alarmService, usersService);
		Map<String, Object> map = new HashMap<>();
		if (result) {
			map.put("state", result);
			map.put("msg", "数据采集器开启成功");
			return JSONArray.fromObject(map).toString();
		} else {
			map.put("state", result);
			map.put("msg", "数据采集器开启失败，请勿重复开启");
			return JSONArray.fromObject(map).toString();
		}
	}

	// 关闭采集器
	@ResponseBody
	@RequestMapping(value = "/stopKafkaCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "关闭数据采集器")
	public String stopKafkaCollector() {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean result = collectorService.stopKafkaCollector();
			if (result) {
				map.put("state", result);
				map.put("msg", "数据采集器关闭成功");
				return JSONArray.fromObject(map).toString();
			} else {
				map.put("state", result);
				map.put("msg", "数据采集器关闭失败，已关闭");
				return JSONArray.fromObject(map).toString();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			map.put("state", false);
			map.put("msg", "数据采集器关闭失败");
			return JSONArray.fromObject(map).toString();
		}
	}

	// 监听采集器状态
	@ResponseBody
	@RequestMapping(value = "/stateKafkaCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "监控数据采集器状态")
	public String stateKafkaCollector() {
		Map<String, Object> map = new HashMap<>();
		boolean result = collectorService.stateKafkaCollector();
		map.put("state", result);
		return JSONArray.fromObject(map).toString();
	}

	// 监听采集器状态
	@ResponseBody
	@RequestMapping(value = "/startMasscanCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "开启masscan扫描")
	public String startMasscanCollector(String startip,String endip) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("192.168.0.1");
		list.add("192.168.0.2");
		list.add("192.168.0.3");
		list.add("192.168.0.4");
		list.add("192.168.0.5");
		list.add("192.168.0.6");
		list.add("192.168.0.7");
		list.add("192.168.0.8");
		list.add("192.168.0.9");
		list.add("192.168.0.10");
		list.add("192.168.0.11");
		list.add("192.168.0.12");
		list.add("192.168.0.13");
		list.add("192.168.0.14");
		list.add("192.168.0.15");
		list.add("192.168.0.16");
		list.add("192.168.0.17");
		list.add("192.168.0.18");
		list.add("192.168.0.19");
		list.add("192.168.0.20");
		String [] ports = {};
		boolean resultstate = collectorService.stateMasscanCollector();
		Map<String, Object> map = new HashMap<>();
		if(resultstate==false){
			map.put("state", resultstate);
			map.put("msg", "数据采集器开启失败，请勿重复开启");
			return JSONArray.fromObject(map).toString();
		}else{
			boolean result = collectorService.startMasscanCollector(list, ports,masscanipService);
			if(result==true){
				map.put("state", result);
				map.put("msg", "数据采集器开启成功");
				return JSONArray.fromObject(map).toString();
			}else{
				map.put("state", result);
				map.put("msg", "数据采集器开启失败");
				return JSONArray.fromObject(map).toString();
			}
		}
		
	}
	
	// 监听采集器状态
	@ResponseBody
	@RequestMapping(value = "/stateMasscanCollector", produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "监控Masscan状态")
	public String stateMasscanCollector() {
		Map<String, Object> map = new HashMap<>();
		boolean result = collectorService.stateMasscanCollector();
		map.put("state", result);
		return JSONArray.fromObject(map).toString();
	}
	

}

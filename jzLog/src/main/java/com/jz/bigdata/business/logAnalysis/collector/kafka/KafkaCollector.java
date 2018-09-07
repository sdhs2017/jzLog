package com.jz.bigdata.business.logAnalysis.collector.kafka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.elasticsearch.action.index.IndexRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jz.bigdata.business.logAnalysis.log.LogType;
import com.jz.bigdata.business.logAnalysis.log.entity.Log4j;
import com.jz.bigdata.business.logAnalysis.log.entity.Mysql;
import com.jz.bigdata.business.logAnalysis.log.entity.PacketFilteringFirewal;
import com.jz.bigdata.business.logAnalysis.log.entity.Syslog;
import com.jz.bigdata.business.logAnalysis.log.entity.Winlog;
import com.jz.bigdata.business.logAnalysis.log.entity.ZtsApp;
import com.jz.bigdata.business.logAnalysis.log.entity.ZtsLog4j;
import com.jz.bigdata.business.logAnalysis.log.entity.ZtsSyslog;
import com.jz.bigdata.common.alarm.service.IAlarmService;
import com.jz.bigdata.common.equipment.entity.Equipment;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KafkaCollector implements Runnable {
	
	private final ConsumerConnector consumer;
	Map<String, List<KafkaStream<String, String>>> consumerMap;
	String topic = "all";
	
    private ClientTemplate template;
    private ConfigProperty configProperty;
    private IUsersService usersService;
	public static Map<String,Object> map=new HashMap<String,Object>();
	
	public static List<Object> list=new ArrayList<Object>();
	
	
	/**
	 * 线程操作
	 */
	//暂停
	//TODO
	//关闭
//	boolean suspended=false;
	boolean started = false;
	public boolean isStarted() {
		return started;
	}
	public void setStarted(boolean started) {
		this.started = started;
		if(started){
			System.out.println("<<<--------开启--------->>");
		}else{
			System.out.println("<<<--------关闭--------->>");
		}
	}
	
	
	/**
	 * 资产列表
	 */
	Map<String, Equipment> equipmentMap;
	Set<String> ipadressSet;
	Map<String, String> equipmentLogType;
	/**
	 * 告警事件
	 */
	Set<String> eventType;
	
	//日志类型
	String logType="unknown";
	Log4j log4j;
	Winlog winlog;
	PacketFilteringFirewal packetFilteringFirewal;
	Mysql mysql;
	Syslog syslog;
	ZtsSyslog ztsSyslog;
	ZtsLog4j ztsLog4j;
	ZtsApp ztsapp;

	/**
	 * @param equipment
	 * @param clientTemplate
	 */
	public KafkaCollector(IEquipmentService equipmentService,ClientTemplate clientTemplate,ConfigProperty configProperty,IAlarmService alarmService,IUsersService usersService) {
		Properties props = new Properties();
		//zookeeper 配置
//		props.put("zookeeper.connect", "124.133.246.61:2281");
//		props.put("zookeeper.connect", "10.29.175.201:2181");
		props.put("zookeeper.connect", configProperty.getZookeeper_path());
//		props.put("zookeeper.connect", "124.133.246.61:2181");
		//group 代表一个消费组
		props.put("group.id", "jd-group");

		//zk连接超时
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		//序列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");

		ConsumerConfig config = new ConsumerConfig(props);

		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
//		equ=equipment;
		template = clientTemplate;
		this.configProperty = configProperty;
		this.usersService = usersService;
		
		//状态设置为开启
//		setStarted(true);
		
		//初始化：获取设备列表、map
		equipmentMap = equipmentService.selectAllEquipment();
		
		ipadressSet = equipmentService.selectAllIPAdress();
		
		equipmentLogType = equipmentService.selectLog_level();
		
		eventType = alarmService.selectByEmailState();
		
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		
		// 设置译码器
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

		consumerMap = consumer.createMessageStreams(topicCountMap,
				keyDecoder, valueDecoder);
		
	}

	public static final String REGEX = "lineStartRegex";
//	public static final String DEFAULT_REGEX = "\\s?\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";
	public static final String DEFAULT_REGEX = "^ java.|^   at";
	
	Calendar cal = Calendar.getInstance();

	/**
	 * kafka流对象管理
	 */
	
//	public Map<String, List<KafkaStream<String, String>>> getConsumerMap() {
//		return consumerMap;
//	}
//	public void setConsumerMap(Map<String, List<KafkaStream<String, String>>> consumerMap) {
//		this.consumerMap = consumerMap;
//	}
	
//	public void createKafkaStreamManage(String topic){
//		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//		topicCountMap.put(topic, new Integer(1));
//		
//		// 设置译码器
//		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
//		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
//
//		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
//				keyDecoder, valueDecoder);
//		
//		
//		setConsumerMap(consumerMap);
//	}
	
	public void closeKafkaStream(){
		consumer.shutdown();
	}
	
	
	@Override
	public void run() {
		
//		String topicName = "testFlume";
//		String topicDBName = "flume-log4j-db";
//		String topicServerName = "flume-log4j-server";
		
		
		

//		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//		topicCountMap.put(topic, new Integer(1));
//		
//		// 设置译码器
//		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
//		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
//
//		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
//				keyDecoder, valueDecoder);
		
//		Map<String, List<KafkaStream<String, String>>> consumerMap = createKafkaStreamManage(topic);
		
		// 获取数据
		KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		
		try {
			
//			System.err.println(equ.getId()+equ.getUserId()+equ.getDepartmentId());
			Gson gson = new GsonBuilder()
					 .setDateFormat("yyyy-MM-dd HH:mm:ss")  
					 .create(); 
			String json;
			
			/*
			 * 	资产、ip地址
			 */
			Equipment equipment;
			String ipadress;
			
			List<IndexRequest> requests = new ArrayList<IndexRequest>();
			StringBuilder builder = new StringBuilder();
			while (it.hasNext() && isStarted()) {
				//System.out.println("---中泰数据接收-----"+it.next().message().toString());
				
				String log = it.next().message().toString();
				
				// 日志过滤正则
				// log4j日志信息过滤条件
				Pattern facility_pattern = Pattern.compile("local3:");
				Matcher facility_matcher = facility_pattern.matcher(log);
				Pattern pattern = Pattern.compile(DEFAULT_REGEX);
				// log4j from logstash
				Pattern log4j_pattern = Pattern.compile("\"type\":\"log4j\"");
				Matcher log4j_matcher = log4j_pattern.matcher(log);
				
				// zts 应用c#日志
				Pattern c_pattern = Pattern.compile("\"type\":\"c#\"");
				Matcher c_matcher = c_pattern.matcher(log);
				
				// 定制化syslog中的业务数据
				Pattern ztspattern = Pattern.compile("dname=themis");
				Matcher ztsmatcher = ztspattern.matcher(log);
				
				if (facility_matcher.find()) {
					logType = LogType.LOGTYPE_LOG4J;
					synchronized (log) {
						String logleft = log.substring(0, log.indexOf(facility_matcher.group(0))+facility_matcher.group(0).length());
						
						Matcher m = pattern.matcher(log.replace(logleft, ""));
						//判断是否符合正则表达式 如果符合，表明这是一条开始数据
//						System.err.println(m.find());
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
									if(null != equipment){
										log4j.setUserid(equipment.getUserId());
										log4j.setDeptid(String.valueOf(equipment.getDepartmentId()));
										log4j.setEquipmentname(equipment.getName());
										log4j.setEquipmentid(equipment.getId());
										json = gson.toJson(log4j);
										requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_LOG4J, json));
									}else{
										log4j.setUserid(LogType.LOGTYPE_UNKNOWN);
										log4j.setDeptid(LogType.LOGTYPE_UNKNOWN);
										log4j.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
										log4j.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
										json = gson.toJson(log4j);
										requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_LOG4J, json));
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
				}else if (log4j_matcher.find()) {
					logType = LogType.LOGTYPE_LOG4J;
					System.out.println(log);
					try {
						ztsLog4j = new ZtsLog4j(log, cal);
					} catch (Exception e) {
						continue;
					}
					
					ipadress = ztsLog4j.getIp();
					if (ipadressSet.contains(ipadress)) {
						equipment = equipmentMap.get(ztsLog4j.getIp()+logType);
						if (equipment!=null) {
							ztsLog4j.setUserid(equipment.getUserId());
							ztsLog4j.setDeptid(String.valueOf(equipment.getDepartmentId()));
							ztsLog4j.setEquipmentname(equipment.getName());
							ztsLog4j.setEquipmentid(equipment.getId());
							json = gson.toJson(ztsLog4j);
							requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_LOG4J, json));
						}else {
							ztsLog4j.setUserid(LogType.LOGTYPE_UNKNOWN);
							ztsLog4j.setDeptid(LogType.LOGTYPE_UNKNOWN);
							ztsLog4j.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							ztsLog4j.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(ztsLog4j);
							requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_LOG4J, json));
						}
					}
					
				}else if(c_matcher.find()){
					logType = LogType.LOGTYPE_APPLOG;
					System.out.println(log);
					try {
						ztsapp = new ZtsApp(log);
					} catch (Exception e) {
						continue;
					}
					
					ipadress = ztsapp.getIp();
					if (ipadressSet.contains(ipadress)) {
						equipment = equipmentMap.get(ztsapp.getIp()+logType);
						if (equipment!=null) {
							ztsapp.setUserid(equipment.getUserId());
							ztsapp.setDeptid(String.valueOf(equipment.getDepartmentId()));
							ztsapp.setEquipmentname(equipment.getName());
							ztsapp.setEquipmentid(equipment.getId());
							json = gson.toJson(ztsapp);
							requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_APPLOG, json));
						}else {
							ztsapp.setUserid(LogType.LOGTYPE_UNKNOWN);
							ztsapp.setDeptid(LogType.LOGTYPE_UNKNOWN);
							ztsapp.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							ztsapp.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(ztsapp);
							requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_APPLOG, json));
						}
					}
					
				}else if (ztsmatcher.find()) {
					logType = LogType.LOGTYPE_SYSLOG;
					try {
						ztsSyslog = new ZtsSyslog(log.trim());
					} catch (Exception e) {
						continue;
					}
					ipadress = ztsSyslog.getIp();
					//判断是否在资产ip地址池里
					if(ipadressSet.contains(ipadress)){
						//判断是否在已识别资产里————日志类型可识别
						equipment = equipmentMap.get(ztsSyslog.getIp() +logType);
						if(null != equipment){
							if (equipmentLogType.get(equipment.getId()).indexOf(ztsSyslog.getOperation_level().toLowerCase())!=-1) {
								ztsSyslog.setUserid(equipment.getUserId());
								ztsSyslog.setDeptid(String.valueOf(equipment.getDepartmentId()));
								ztsSyslog.setEquipmentid(equipment.getId());
								ztsSyslog.setEquipmentname(equipment.getName());
								
								json = gson.toJson(ztsSyslog);
								requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_SYSLOG, json));
							}
						}else{
							//在资产ip地址池里，但是无法识别日志类型
							syslog.setUserid(LogType.LOGTYPE_UNKNOWN);
							syslog.setDeptid(LogType.LOGTYPE_UNKNOWN);
							syslog.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							syslog.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(syslog);
							requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_UNKNOWN, json));
						}
					}else{
						//不在资产ip池里，暂不处理
						//TODO
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
								
								/*if (eventType.contains(syslog.getEvent_type())) {
									Sendmail sendmail = new Sendmail(syslog.getIp(), syslog.getEquipmentname(), syslog.getEvent_des(), usersService.selectById(syslog.getUserid()).getEmail());
								}*/
								json = gson.toJson(syslog);
								requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_SYSLOG, json));
							}
							
							
							
						}else{
							//在资产ip地址池里，但是无法识别日志类型
							syslog.setUserid(LogType.LOGTYPE_UNKNOWN);
							syslog.setDeptid(LogType.LOGTYPE_UNKNOWN);
							syslog.setEquipmentid(LogType.LOGTYPE_UNKNOWN);
							syslog.setEquipmentname(LogType.LOGTYPE_UNKNOWN);
							json = gson.toJson(syslog);
							requests.add(template.insertNo(configProperty.getEs_index(), LogType.LOGTYPE_UNKNOWN, json));
						}
					}else{
						//不在资产ip池里，暂不处理
						//TODO
					}

					
				}
				
				if (requests.size()==configProperty.getEs_bulk()) {
					template.bulk(requests);
					requests.clear();
				}
				
				
				// Thread.sleep(2000);
			}
				
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getMessage());
			System.out.println(e1);
		}

	}

}

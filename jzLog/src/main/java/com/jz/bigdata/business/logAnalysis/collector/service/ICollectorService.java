package com.jz.bigdata.business.logAnalysis.collector.service;

import java.util.List;

import com.jz.bigdata.common.alarm.service.IAlarmService;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;

public interface ICollectorService {
	
	public boolean getKafkaCollectorState();
	
	public boolean startKafkaCollector(IEquipmentService equipmentService,ClientTemplate clientTemplate,ConfigProperty configProperty,IAlarmService alarmService,IUsersService usersService);
	
	public boolean stopKafkaCollector()throws InterruptedException;
	
	public boolean stateKafkaCollector() ;

	
	public boolean startMasscanCollector(List<String> list,String [] ports);
}

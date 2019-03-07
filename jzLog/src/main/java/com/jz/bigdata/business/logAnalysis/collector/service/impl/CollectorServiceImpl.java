package com.jz.bigdata.business.logAnalysis.collector.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.jz.bigdata.business.logAnalysis.collector.kafka.KafkaCollector;
import com.jz.bigdata.business.logAnalysis.collector.masscan.MascanCollector;
import com.jz.bigdata.business.logAnalysis.collector.service.ICollectorService;
import com.jz.bigdata.common.alarm.service.IAlarmService;
import com.jz.bigdata.common.assets.entity.Assets;
import com.jz.bigdata.common.assets.service.IAssetsService;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.users.service.IUsersService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.ConfigProperty;

@Service(value="CollectorService")
public class CollectorServiceImpl implements ICollectorService{
	
	//开关
	boolean flag = false;
	
	//kafka采集器
	KafkaCollector kc = null;
	
	MascanCollector Masscan = null;
	
	boolean flagMasscan = false;
	
	Thread t;
	Thread masscanThread;
//	public Thread getT() {
//		return t;
//	}
//	public void setT(Thread t) {
//		this.t = t;
//	}

	/**
	 * 获取采集器开启或关闭状态，true为开启，false为关闭
	 * @return
	 */
	public boolean getKafkaCollectorState(){
		boolean result = false;
		if(null!=kc){
			result = kc.isStarted();
		}
		return result;
	}
	
	@SuppressWarnings("finally")
	public boolean initKafkaCollector(IEquipmentService equipmentService,ClientTemplate clientTemplate,ConfigProperty configProperty,IAlarmService alarmService,IUsersService usersService){
		boolean result = false;
		try{
//			if(!flag){
				kc = new KafkaCollector(equipmentService,clientTemplate,configProperty,alarmService,usersService);
				flag = true;
//			}
			result = true;
		}finally{
			return result;
		}

		
	}
	
	/**
	 * 启动kafka采集器
	 * @param equipment
	 * @param clientTemplate
	 * @return
	 */
	public boolean startKafkaCollector(IEquipmentService equipmentService,ClientTemplate clientTemplate,ConfigProperty configProperty,IAlarmService alarmService,IUsersService usersService){
		boolean result = false;
		//如果为true，则表示已经开启，反之，则为未开启，需要初始化
		
		initKafkaCollector(equipmentService,clientTemplate,configProperty,alarmService,usersService);
		if(!kc.isStarted()){

			t = new Thread(kc);
			kc.setStarted(true);
			t.start();
			
			result = true;
		}else{
//				kc.setStarted(true);
//				result = true;
		}
		
		
		return result;
	}
	
	/**
	 * 关闭kafka采集器
	 * @return
	 * @throws InterruptedException
	 */
	public boolean stopKafkaCollector() throws InterruptedException{
		boolean result = false;
		//如果为true，则表示已经开启，需要关闭，反之，则为未开启
		
		if(flag){
			kc.setStarted(false);
			kc.closeKafkaStream();
//			flag = false;
			result = true;
		}else{
			
		}
		
//		if(null!=kc && kc.isStarted()){
//			kc.setStarted(false);
//			
//			
////			t.join();
//			
////			t.sleep(10000);
//			//阻塞
////			t.interrupt();
//			
////			t.stop();
////			t=null;
////			kc = null;
//			
//			result = true;
//		}
		return result;
	}
	

	public boolean stateKafkaCollector() {
		if (null==kc) {
			return false;
		}
		return kc.isStarted();
	}

	
	/**
	 * @return
	 * @description
	 * 启动Masscan
	 */
	@Override
	public boolean startMasscanCollector(String startip,String endip,String ports,IAssetsService masscanipService) {
		
		boolean result = false;
		//如果为true，则表示已经开启
		List<String> list =new ArrayList<>();
		
		String[] startips= startip.split("\\.");
		String[] endips= endip.split("\\.");
		
		int start=Integer.valueOf(startips[3]);
		int end=Integer.valueOf(endips[3]);
		List<Assets> assetsList=masscanipService.selectAll();
		Boolean isIn=false;
		for(int i=start;i<=end;i++){
			for(Assets assets:assetsList){
				if(assets.getIp().equals((startips[0]+"."+startips[1]+"."+startips[2]+"."+i))==true){
					isIn=true;
					break;
				}
			}
			if(isIn==false){
				list.add((startips[0]+"."+startips[1]+"."+startips[2]+"."+i));
			}
			isIn=false;
		}
		Masscan = new MascanCollector(list,ports,masscanipService);
		if(!Masscan.getStarted()){
			result=true;
		}
		
		return result;
	}

	@Override
	public boolean stateMasscanCollector() {
		if(null==Masscan){
			return true;
		}else{
			return Masscan.getStarted();
		}
		
	}
	
	
}

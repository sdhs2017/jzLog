package com.jz.bigdata.common.equipment.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.equipment.entity.Equipment;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.components.kafka.logAnalysis.SysLogKafkaConsumer;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;
import com.jz.bigdata.util.DescribeLog;


/**
 * @author shichengyu
 * @date 2017年8月16日 下午2:40:21
 * @description
 */
@Controller
@RequestMapping("/equipment")
public class EquipmentController {
	
	@Resource(name = "EquipmentService")
	private IEquipmentService equipmentService;
	
	
	@Autowired protected ClientTemplate clientTemplate;


	
	@ResponseBody
	@RequestMapping("/insert")
	@DescribeLog(describe="添加或修改资产")
	public String insert(HttpServletRequest request, Equipment equipment,HttpSession session) {
	
		// 结果一般命名为result
		int result = 0;
		// 如果id为空，进行添加操作
		if (equipment.getId() == null || equipment.getId().isEmpty()) {
			result = this.equipmentService.insert(equipment,session);
		} else {// 更新操作
			result = this.equipmentService.updateById(equipment,session);
		}
		return result >= 1 ? Constant.successMessage() : Constant.failureMessage();
	}

	/**
	 * @param request
	 * @return 根据id删除数据
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@DescribeLog(describe="删除资产")
	public String delete(HttpServletRequest request) {
		int result = 0;
//		获取id数组
		String[] ids = request.getParameter("id").split(",");
//		数组长度大于0删除数据
		if (ids.length > 0) {
			result = this.equipmentService.delete(ids);
		}
		return result >= 1 ? Constant.successMessage() : Constant.failureMessage();
	}

	
	/**
	 * @param request
	 * @param equipment
	 * @return 查询数据
	 */
	@ResponseBody
	@RequestMapping("/selectAll")
	@DescribeLog(describe="查询所有资产")
	public String selectAll(HttpServletRequest request, Equipment equipment){
		return equipmentService.selectAll(equipment);
	}
	
	/**
	 * @param request
	 * @param equipment
	 * @return
	 * 查询单个实体
	 */
	@ResponseBody
	@RequestMapping("/selectEquipment")
	@DescribeLog(describe="查询单个资产")
	public List<Equipment> selectEquipment(HttpServletRequest request, Equipment equipment) {
		List<Equipment> list=this.equipmentService.selectEquipment(equipment);
		return list;
	}

	
	/**
	 * @param request
	 * @param page
	 * @return 分页测试例子
	 */
	@ResponseBody
//	@RequestMapping("/selectPage")
	@RequestMapping(value="/selectPage.do", produces = "application/json; charset=utf-8")
	@DescribeLog(describe="分页查询资产")
	public String selectPage(HttpServletRequest request) {
		//页码数
		int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		//每页显示的数量
		int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		//主机名
		String hostName=request.getParameter("hostName");
		//名字
		String name=request.getParameter("name");
		//ip地址
		String ip=request.getParameter("ip");
		//日志类型
		String logtype =request.getParameter("logType");
		int logType=0;
		if(request.getParameter("logType") !=null && !request.getParameter("logType").equals("")){
			 logType =Integer.valueOf(logtype);
		}
		
//		Integer logType =Integer.valueOf(logtype);
//		if(logtype !=null){
//			int logType=Integer.valueOf(logtype);
//		}else{
//			
//		}
		
		
		System.err.println(logType);
		
		return equipmentService.selectAllByPage(hostName,name,ip,logType, pageIndex, pageSize);
	}
	/**
	 * 获取id
	 * @param request
	 * @param equipment
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertBeathLogByEquipmentId")
//	@DescribeLog(describe="添加部门")
	public String getId(HttpServletRequest request,Equipment equipment){
//		equipment=equipmentService.selectEquipment(equipment);
////		System.out.println(equipment.getId());
////		System.out.println(equipment.getUserId());
////		System.out.println(equipment.getDepartmentId());
//		
//		KafkaConsumer m = new KafkaConsumer(equipment,clientTemplate);
//		Thread t = new Thread(m);
//		t.start();
//		equipment=this.equipmentService.selectOneEquipment(equipment);
//		System.out.println(equipment.getId());
//		System.out.println(equipment.getUserId());
//		System.out.println(equipment.getDepartmentId());
//		System.out.println(equipment.getId());
//		int num =0;
		/**
		 * TODO
		 * 返回值为空：未实现
		 * 返回值为SNMP：未实现
//		 */
//		if(EquipmentConstant.LOGTYPE_SYSLOG==equipment.getLogType()){
//			SysLogKafkaConsumer m = new SysLogKafkaConsumer(equipment,clientTemplate);
//			Thread t = new Thread(m);
//			t.start();
//			return Constant.successMessage();
//		}else if(EquipmentConstant.LOGTYPE_WMI==equipment.getLogType()){
//			WinLogKafkaConsumer m = new WinLogKafkaConsumer(equipment,clientTemplate);
//			Thread t = new Thread(m);
//			t.start();
//			return Constant.successMessage();
//		}else if(EquipmentConstant.LOGTYPE_SNMP==equipment.getLogType()){
//			//TODO
//			return Constant.successMessage();
//		}else if(EquipmentConstant.LOGTYPE_LOG4J==equipment.getLogType()){
//			Log4jKafkaConsumer m = new Log4jKafkaConsumer(equipment,clientTemplate);
//			Thread t = new Thread(m);
//			if(num == 0){
//				
//				t.start();
//				return Constant.successMessage();
//				
//			}else if(num == 1){
//				t.suspend();
//			}else if(num == 2){
//				t.resume();
//				
//				t.interrupt();
//			}else if(num ==3){
//				t.stop();
//			}
//			Log4jKafkaConsumer m = new Log4jKafkaConsumer(equipment,clientTemplate);
//			Thread t = new Thread(m);
//			t.start();
//			return Constant.successMessage();
//		}else{
//			SysLogKafkaConsumer m = new SysLogKafkaConsumer(equipment,clientTemplate);
//			Thread t = new Thread(m);
//			t.start();
//			return Constant.successMessage();
//			//TODO
////			return  Constant.failureMessage();
//		}
		
		SysLogKafkaConsumer m = new SysLogKafkaConsumer(equipment,clientTemplate);
		Thread t = new Thread(m);
		t.start();
		return Constant.successMessage();
		
		
		//TODO
		/**
		 * 处理成功、失败，返回条件
		 */
//		return null;
		
//		KafkaConsumer m = new KafkaConsumer(equipment,clientTemplate);
//		Thread t = new Thread(m);
//		t.start();
//		return null;
		
	}

}

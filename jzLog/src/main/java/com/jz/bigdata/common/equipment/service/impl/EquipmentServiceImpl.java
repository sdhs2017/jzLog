package com.jz.bigdata.common.equipment.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.jz.bigdata.business.logAnalysis.log.LogType;
import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.equipment.dao.IEquipmentDao;
import com.jz.bigdata.common.equipment.entity.Equipment;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.users.dao.IUsersDao;
import com.jz.bigdata.common.users.entity.User;
import com.jz.bigdata.util.BASE64Util;
import com.jz.bigdata.util.ConfigProperty;
import com.jz.bigdata.util.Uuid;

import net.sf.json.JSONArray;

/**
 * @author shichengyu
 * @date 2017年8月16日 下午2:39:47
 * @description
 */
@Service(value = "EquipmentService")
public class EquipmentServiceImpl implements IEquipmentService {

	@Resource
	private IEquipmentDao equipmentDao;

	@Resource
	private IUsersDao userDao;

	@Resource(name = "configProperty")
	private ConfigProperty configProperty;

	/**
	 * @param equipment
	 * @return 添加数据
	 */
	@Override
	public int insert(Equipment equipment, HttpSession session) {

		// 获取总数
		List<Object> count = equipmentDao.count_Number();
		// 获取总数集合

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = (List<Map<String, Object>>) count.get(0);

		BASE64Util base64Util = new BASE64Util();
		// 判断资产数是否超过限定
		if (Integer.valueOf((String) resultList.get(0).get("count")) < Integer
				.valueOf(base64Util.decode(configProperty.getNumber()).trim())) {
			// 获取uuid
			equipment.setId(Uuid.getUUID());
			User user = userDao.selectById(session.getAttribute(Constant.SESSION_USERID).toString());
			equipment.setDepartmentId(user.getDepartmentId());
			equipment.setUserId(session.getAttribute(Constant.SESSION_USERID).toString());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			// 获取日期
			equipment.setCreateTime(df.format(new Date()));
			return equipmentDao.insert(equipment);
		} else {
			return 0;
		}

	}

	/**
	 * @param equipment
	 * @return 查询数据
	 */
	@Override
	public String selectAll(Equipment equipment) {
		List<Equipment> list = equipmentDao.selectAll(equipment);

		// list转json
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * @param equipment
	 * @return 修改数据
	 */
	@Override
	public int updateById(Equipment equipment, HttpSession session) {
		// 设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取日期
		equipment.setUpDateTime(df.format(new Date()));
		// equipment.setDepartmentId(Integer.valueOf(session.getAttribute(Constant.SESSION_DEPARTMENTID).toString()));
		// equipment.setUserId(session.getAttribute(Constant.SESSION_USERID).toString());
		// if(equipment)
		return equipmentDao.updateById(equipment);
	}

	/**
	 * @param id
	 * @return 删除数据
	 */
	@Override
	public int delete(String[] ids) {
		return equipmentDao.delete(ids);
	}

	/**
	 * @param equipment
	 * @return
	 * @description 根据id查询一个数据
	 */
	@Override
	public List<Equipment> selectEquipment(Equipment equipment) {
		List<Equipment> list = equipmentDao.selectEquipment(equipment);
		List<Equipment> listEquipment = (List<Equipment>) list.get(0);
		// equipment=listEquipment.get(0);
		// System.out.println(equipment.getConfidentiality());
		return listEquipment;
	}

	/**
	 * @param equipment
	 * @param startRecord
	 * @param pageSize
	 * @return
	 * @description 分页查询数据
	 */
	@Override
	public String selectAllByPage(String hostName, String name, String ip, int logType, int pageIndex, int pageSize) {
		// 获取起始数
		int startRecord = (pageSize * (pageIndex - 1));
		// 获取总数
		List count = equipmentDao.count(hostName, name, ip, logType);
		List listCount = new ArrayList<>();
		// 获取总数集合
		listCount = (List) count.get(0);

		Map<String, Object> map = new HashMap<String, Object>();
		// 总数添加到map
		map.put("count", (listCount.get(0)));
		// 查询所有数据
		List<Equipment> listEquipment = equipmentDao.selectAllByPage(hostName, name, ip, logType, startRecord,
				pageSize);
		// System.err.println(listEquipment.get(0).getCreateTime());
		// 数据添加到map
		map.put("equipment", listEquipment);
		return JSONArray.fromObject(map).toString();
	}

	/**
	 * @param equipment
	 * @return
	 * @description 查询单个数据
	 */
	@Override
	public Equipment selectOneEquipment(Equipment equipment) {
		return equipmentDao.selectOneEquipment(equipment);
	}

	@Override
	public List<Equipment> selectAllHostName() {
		return equipmentDao.selectAllHostName();
	}

	/**
	 * @return
	 * @description 查询所有数据map
	 * 
	 */
	@Override
	public Map<String, Equipment> selectAllEquipment() {

		Map<String, Equipment> map = new HashMap<String, Equipment>();
		List<Equipment> list = equipmentDao.selectAllHostName();
		Equipment e;
		String logType = "syslog";
		for (int i = 0; i < list.size(); i++) {
			e = list.get(i);
			if (1 == e.getLogType()) {
				logType = LogType.LOGTYPE_SYSLOG;
			} else if (2 == e.getLogType()) {
				logType = LogType.LOGTYPE_WINLOG;
			} else if (3 == e.getLogType()) {
				// snmp
			} else if (4 == e.getLogType()) {
				logType = LogType.LOGTYPE_LOG4J;
			} else if (5 == e.getLogType()) {
				logType = LogType.LOGTYPE_MYSQLLOG;
			} else if (6 == e.getLogType()) {
				logType = LogType.LOGTYPE_PACKETFILTERINGFIREWALL_LOG;
			} else {
				logType = LogType.LOGTYPE_SYSLOG;
			}
			map.put(list.get(i).getIp() + logType, list.get(i));
		}
		// System.out.println("111111111");
		// Equipment equipment = map.get("192.168.0.129");
		// System.out.println(equipment.getId());
		return map;
	}

	@Override
	public Set<String> selectAllIPAdress() {

		Set<String> set = new HashSet<String>();
		List<Equipment> list = equipmentDao.selectAllHostName();
		for (int i = 0; i < list.size(); i++) {
			set.add(list.get(i).getIp());
		}
		return set;
	}

	@Override
	public Map<String, String> selectLog_level() {

		Map<String, String> map = new HashMap<String, String>();
		List<Equipment> list = equipmentDao.selectAllHostName();
		for (Equipment equipment : list) {
			map.put(equipment.getId(), equipment.getLog_level());
		}
		return map;
	}

	/**
	 * @param high_risk
	 * @param moderate_risk
	 * @param low_risk
	 * @return
	 * @description 修改高中低危的数据
	 */
	@Override
	public int upRiskById(String id, int high_risk, int moderate_risk, int low_risk) {
		return equipmentDao.upRiskById(id, high_risk, moderate_risk, low_risk);
	}

	/**
	 * @return
	 * @description 查询所有数据，用于安全策略
	 */
	@Override
	public List<Equipment> selectAllEquipmentByRisk() {
		return equipmentDao.selectAllEquipmentByRisk();
	}

}

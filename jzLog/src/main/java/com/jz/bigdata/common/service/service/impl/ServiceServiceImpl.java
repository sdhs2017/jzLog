package com.jz.bigdata.common.service.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.equipment.entity.Equipment;
import com.jz.bigdata.common.service.dao.IServiceDao;
import com.jz.bigdata.common.service.service.IServiceService;

import net.sf.json.JSONArray;

/**
 * @ClassName ServiceServiceImpl
 * @Description
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午10:13:15
 */
@Service(value = "ServiceService")
public class ServiceServiceImpl implements IServiceService {

	private IServiceDao serviceDao;

	/**
	 * @param service
	 * @return
	 * @description 添加方法
	 */
	@Override
	public int insert(com.jz.bigdata.common.service.entity.Service service) {
		// TODO Auto-generated method stub
		return serviceDao.insert(service);
	}

	/**
	 * @param service
	 * @return
	 * @description 查询所有数据
	 */
	@Override
	public List<com.jz.bigdata.common.service.entity.Service> selectAll(
			com.jz.bigdata.common.service.entity.Service service) {
		// TODO Auto-generated method stub
		return serviceDao.selectAll(service);
	}

	/**
	 * @param ids
	 * @return
	 * @description 删除数据
	 */
	@Override
	public int delete(String[] ids) {
		// TODO Auto-generated method stub
		return serviceDao.delete(ids);
	}

	/**
	 * @param service
	 * @return
	 * @description 需改数据
	 */
	@Override
	public int updateById(com.jz.bigdata.common.service.entity.Service service) {
		// TODO Auto-generated method stub
		return serviceDao.updateById(service);
	}

	/**
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @description
	 * 分页查询
	 */
	@Override
	public String selectAllByPage( int pageIndex, int pageSize) {
		// 获取起始数
		int startRecord = (pageSize * (pageIndex - 1));
		// 获取总数
		List count = serviceDao.count();
		List listCount = new ArrayList<>();
		// 获取总数集合
		listCount = (List) count.get(0);

		Map<String, Object> map = new HashMap<String, Object>();
		// 总数添加到map
		map.put("count", (listCount.get(0)));
		// 查询所有数据
		List<com.jz.bigdata.common.service.entity.Service> listService = serviceDao.selectAllByPage(startRecord, pageSize);
		// System.err.println(listEquipment.get(0).getCreateTime());
		// 数据添加到map
		map.put("service", listService);
		return JSONArray.fromObject(map).toString();
	}

}

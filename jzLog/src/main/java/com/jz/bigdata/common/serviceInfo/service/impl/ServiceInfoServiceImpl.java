package com.jz.bigdata.common.serviceInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.serviceInfo.dao.IServiceInfoDao;
import com.jz.bigdata.common.serviceInfo.entity.ServiceInfo;
import com.jz.bigdata.common.serviceInfo.service.IServiceInfoService;

import net.sf.json.JSONArray;

/**
 * @ClassName ServiceServiceImpl
 * @Description
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午10:13:15
 */
@Service(value = "ServiceInfoService")
public class ServiceInfoServiceImpl implements IServiceInfoService {

	@Resource
	private IServiceInfoDao serviceInfoDao;

	/**
	 * @param service
	 * @return
	 * @description 添加方法
	 */
	@Override
	public int insert(ServiceInfo serviceInfo) {
		return serviceInfoDao.insert(serviceInfo);
	}

	/**
	 * @param service
	 * @return
	 * @description 查询所有数据
	 */
	@Override
	public List<ServiceInfo> selectAll(ServiceInfo serviceInfo) {
		return serviceInfoDao.selectAll(serviceInfo);
	}

	/**
	 * @param ids
	 * @return
	 * @description 删除数据
	 */
	@Override
	public int delete(String[] ids) {
		return serviceInfoDao.delete(ids);
	}

	/**
	 * @param service
	 * @return
	 * @description 修改数据
	 */
	@Override
	public int updateById(ServiceInfo serviceInfo) {
		return serviceInfoDao.updateById(serviceInfo);
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
		List count = serviceInfoDao.count();
		List listCount = new ArrayList<>();
		// 获取总数集合
		listCount = (List) count.get(0);

		Map<String, Object> map = new HashMap<String, Object>();
		// 总数添加到map
		map.put("count", (listCount.get(0)));
		// 查询所有数据
		List<ServiceInfo> listService = serviceInfoDao.selectAllByPage(startRecord, pageSize);
		// System.err.println(listEquipment.get(0).getCreateTime());
		// 数据添加到map
		map.put("serviceInfo", listService);
		return JSONArray.fromObject(map).toString();
	}

	/**
	 * @param url
	 * @return
	 * @description
	 * 查询url
	 */
	@Override
	public ServiceInfo selectServiceByUrl(String url) {
		return serviceInfoDao.selectServiceByUrl(url);
	}

}

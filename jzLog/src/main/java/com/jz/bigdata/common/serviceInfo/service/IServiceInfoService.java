package com.jz.bigdata.common.serviceInfo.service;

import java.util.List;

import com.jz.bigdata.common.serviceInfo.entity.ServiceInfo;



/**
 * @ClassName IServiceService
 * @Description
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午10:13:11
 */
public interface IServiceInfoService {
	int insert(ServiceInfo serviceInfo);

	List<ServiceInfo> selectAll(ServiceInfo serviceInfo);

	int delete(String[] ids);

	int updateById(ServiceInfo serviceInfo);
	
	String selectAllByPage( int pageIndex, int pageSize);
	
	ServiceInfo selectServiceByUrl(String url);

}

package com.jz.bigdata.common.serviceInfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jz.bigdata.common.serviceInfo.entity.ServiceInfo;

/**
 * @ClassName IServiceDao
 * @Description
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午11:12:29
 */
public interface IServiceInfoDao {
	int insert(List<ServiceInfo> list);

	List<ServiceInfo> selectAll(ServiceInfo serviceInfo);

	int delete(String[] ids);

	int updateById(ServiceInfo serviceInfo);

	List<ServiceInfo> selectAllByPage(@Param("startRecord")int startRecord,@Param("pageSize")int pageSize);

	List<String> count();
	
	ServiceInfo selectServiceByUrl(@Param("url")String url);
	
	ServiceInfo selectServiceById(@Param("id")String id);

}

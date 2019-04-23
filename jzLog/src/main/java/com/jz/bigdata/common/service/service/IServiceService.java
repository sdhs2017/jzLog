package com.jz.bigdata.common.service.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jz.bigdata.common.service.entity.Service;

/**
 * @ClassName IServiceService
 * @Description
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午10:13:11
 */
public interface IServiceService {
	int insert(Service service);

	List<Service> selectAll(Service service);

	int delete(String[] ids);

	int updateById(Service service);
	
	String selectAllByPage( int pageIndex, int pageSize);

}

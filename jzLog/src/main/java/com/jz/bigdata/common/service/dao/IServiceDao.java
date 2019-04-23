package com.jz.bigdata.common.service.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jz.bigdata.common.equipment.entity.Equipment;
import com.jz.bigdata.common.service.entity.Service;

/**
 * @ClassName IServiceDao
 * @Description
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午11:12:29
 */
public interface IServiceDao {
	int insert(Service service);

	List<Service> selectAll(Service service);

	int delete(String[] ids);

	int updateById(Service service);

	List<Service> selectAllByPage(@Param("startRecord")int startRecord,@Param("pageSize")int pageSize);

	List<String> count();

}

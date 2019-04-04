package com.jz.bigdata.common.department.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.jz.bigdata.common.department.entity.Department;

/**
 * @author shichengyu
 * @date 2017年8月4日 上午10:56:31
 * @description
 */
public interface IDepartmentService {
	
	int insert(Department department);
	
	Map<String,Object> selectAll(Department department,HttpSession session);
	
	int updateById(Department department);
	
	Boolean delete(int id);
	
	String selectAllDepartment();
	
//	int updateSuperiorId(int id);

}

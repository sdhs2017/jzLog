package com.jz.bigdata.common.service.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.service.entity.Service;
import com.jz.bigdata.common.service.service.IServiceService;
import com.jz.bigdata.util.DescribeLog;

/**
 * @ClassName ServiceController
 * @Description 
 * @Author shi cheng yu
 * @Date 2019年4月23日 上午10:13:35
 */
@Controller
@RequestMapping("/service")
public class ServiceController {
	
	@Resource(name = "ServiceService")
	private IServiceService serviceService;
	
	
	/**
	 * @param request
	 * @param event
	 * @return 添加事件
	 */
	@ResponseBody
	@RequestMapping(value="/insert",produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "添加事件")
	public String insert(HttpServletRequest request, Service service) {

		// 结果一般命名为result
		int result = 0;
		result = serviceService.insert(service);
		return result >= 1 ? Constant.successMessage() : Constant.failureMessage();
	}

	/**
	 * @param request
	 * @param department
	 * @return 查询信息
	 */
	@ResponseBody
	@RequestMapping(value="/selectAll",produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "查询所有事件信息")
	public List<Service> selectAll(HttpServletRequest request,Service service) {

		// 结果一般命名为result
		// int result = 0;
		// result=departmentService.insert(department);
		return serviceService.selectAll(service);
	}

	/**
	 * @param request
	 * @param department
	 * @return 修改信息
	 */
	@ResponseBody
	@RequestMapping(value="/updateById",produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "修改事件信息")
	public String updataById(HttpServletRequest request, Service service) {

		// 结果一般命名为result
		int result = 0;
		result = serviceService.updateById(service);
		return result >= 1 ? Constant.successMessage() : Constant.failureMessage();
	}

	/**
	 * @param request
	 * @return 根据id删除信息
	 */
	@ResponseBody
	@RequestMapping(value="/delete",produces = "application/json; charset=utf-8")
	@DescribeLog(describe = "删除事件")
	public String delete(HttpServletRequest request) {

		// 结果一般命名为result
		int result = 0;
		String[] ids = request.getParameter("id").split(",");
		//数组长度大于0删除数据
		if (ids.length > 0) {
			result = serviceService.delete(ids);
		}
		return result >= 1 ? Constant.successMessage() : Constant.failureMessage();
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
	public String selectPage(HttpServletRequest request,HttpSession session) {
		//页码数
		int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		//每页显示的数量
		int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		return serviceService.selectAllByPage( pageIndex, pageSize);
	}
	
	

}

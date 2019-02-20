package com.jz.bigdata.common.masscanip.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.common.Constant;
import com.jz.bigdata.common.masscanip.entity.Masscanip;
import com.jz.bigdata.common.masscanip.service.IMasscanipService;
import com.jz.bigdata.util.DescribeLog;
@Controller(value="MasscanipController")
public class MasscanipController {
	
	@Resource(name="MasscanipService")
	private IMasscanipService masscanipService;
	
	@ResponseBody
//	@RequestMapping("/insert")
	@RequestMapping(value="/insert",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="添加动作")
	public String insert(HttpServletRequest request,Masscanip masscanip){
		System.out.println(masscanip.getStartip());
		System.out.println(masscanip.getEndip());
		//结果一般命名为result
		int result = 0;
//		result=masscanipService.insert(masscanip);
		return result>=1?Constant.successMessage():Constant.failureMessage();
	}
	
	
	/**
	 * @param request
	 * @param department
	 * @return 查询信息
	 */
	@ResponseBody
//	@RequestMapping("/selectAll")
	@RequestMapping(value="/selectAll",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="查询所有动作信息")
	public List<Masscanip> selectAll(HttpServletRequest request){

		//结果一般命名为result
//		int result = 0;
//		result=departmentService.insert(department);
		return masscanipService.selectAll();
	}
	
	/**
	 * @param request
	 * @param department
	 * @return 修改信息
	 */
	@ResponseBody
//	@RequestMapping("/updateById")
	@RequestMapping(value="/updateById",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="修改动作信息")
	public String updateById(HttpServletRequest request,Masscanip masscanip){

		//结果一般命名为result
		int result = 0;
		result=masscanipService.updateById(masscanip);
		return result>=1?Constant.successMessage():Constant.failureMessage();
	}

	
	/**
	 * @param request
	 * @return
	 * 根据id删除信息
	 */
	@ResponseBody
//	@RequestMapping("/delete")
	@RequestMapping(value="/delete",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="删除动作")
	public String delete(HttpServletRequest request){

		//结果一般命名为result
		int result = 0;
		String id =request.getParameter("id");
		result=masscanipService.delete(id);
		return result >= 1 ? Constant.successMessage() : Constant.failureMessage();
	}
	

}

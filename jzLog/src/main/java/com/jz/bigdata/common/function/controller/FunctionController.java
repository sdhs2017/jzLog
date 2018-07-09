package com.jz.bigdata.common.function.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jz.bigdata.common.function.service.IFunctionService;
import com.jz.bigdata.common.users.util.Page;
import com.jz.bigdata.util.DescribeLog;

import net.sf.json.JSONObject;

/**
 * @author shichengyu
 * @date 2018年2月8日 下午3:11:49
 * @description
 */
@Controller
@RequestMapping("/function")
public class FunctionController {
	@Resource(name = "FunctionService")
	private IFunctionService functionService;
	
	@ResponseBody
//	@RequestMapping("/selectAll")
	@RequestMapping(value="/selectAll",produces = "application/json; charset=utf-8")
	@DescribeLog(describe="查询所有权限")
	public  String selectAll() {
		JSONObject jsonObject = JSONObject.fromObject(functionService.selectAll());
		System.out.println(jsonObject.toString());
//		System.out.println(functionService.selectAll().toString());
		return jsonObject.toString();
	}

}

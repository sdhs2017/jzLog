package com.jz.bigdata.common.function.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.function.dao.IFunctionDao;
import com.jz.bigdata.common.function.entity.Function;
import com.jz.bigdata.common.function.service.IFunctionService;

/**
 * @author shichengyu
 * @date 2018年2月8日 上午11:40:17
 * @description
 */
@Service(value = "FunctionService")
public class FunctionServiceImpl implements IFunctionService {

	@Resource
	private IFunctionDao functionDao;

	public Map<String, Map<String, String>> map;

	@Override
	public List<Function> selectAllByRole(int role) {
		List<Function> list = functionDao.selectAllByRole(role);
		System.out.println(list.get(0).getResource());
		// Map<String,String> mapFunction= new HashMap<String,String>();
		// System.out.println(list.get(0));
		// for(Function function : list) {
		//// System.out.println(function.get);
		// mapFunction.put(function.getResource(), function.getDescribes());
		// }
		// map.put(String.valueOf(role), mapFunction);

		return functionDao.selectAllByRole(role);
	}

	@Override
	public Map<String, List<Function>> selectAll() {
		List<Function> list = functionDao.selectAll();
//		Map<String, Map<String, List<Function>>> roleMap = new HashMap<String, Map<String, List<Function>>>();
		Map<String, List<Function>> roleMap = new HashMap<String, List<Function>>();
//		Map<String,>
		List<Function> listfun1 = new ArrayList<Function>();
		List<Function> listfun2 = new ArrayList<Function>();
		List<Function> listfun3 = new ArrayList<Function>();
		List<Function> listfun4 = new ArrayList<Function>();
		for (int i = 0; i < list.size(); i++) {
//			String[] b = list.get(i).getResource().split("/");
//			System.out.println(b[1]);

			switch (list.get(i).getRole()) {
			case 1:
				// resourceMap.get(b[2]);
				listfun1.add(list.get(i));
				roleMap.put("administrators", listfun1);
				break;
			case 2:
				listfun2.add(list.get(i));
				roleMap.put("operator", listfun2);;
				break;

			case 3:
				listfun3.add(list.get(i));
				roleMap.put("examiner", listfun3);;
				break;
			case 4:
				listfun4.add(list.get(i));
				roleMap.put("visitor", listfun4);;
				break;

			}

		}
		System.out.println(roleMap.toString());
		return roleMap;
	}

}

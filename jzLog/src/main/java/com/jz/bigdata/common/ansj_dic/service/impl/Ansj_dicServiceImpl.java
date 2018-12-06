package com.jz.bigdata.common.ansj_dic.service.impl;

import javax.annotation.Resource;

import com.jz.bigdata.common.ansj_dic.dao.IAnsj_dicDao;
import com.jz.bigdata.common.ansj_dic.entity.Dic;
import com.jz.bigdata.common.ansj_dic.service.IAnsj_dic;

public class Ansj_dicServiceImpl implements IAnsj_dic {

	@Resource
	private IAnsj_dicDao ansj_dicDao;
	@Override
	public void insert(Dic dic) {
		// TODO Auto-generated method stub
		ansj_dicDao.insert(dic);
	}

}

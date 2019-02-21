package com.jz.bigdata.common.assets.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jz.bigdata.common.assets.entity.Assets;


public interface IAssetsService {
	int insert(Assets assets);

	List<Assets> selectAll();

	int updateById(Assets assets);

	int delete(String[] ids);
	
	String selectAllByPage(int pageIndex,int pageSize);

	List<String> count();
	
	Assets selectOneAssets(Assets assets);


}

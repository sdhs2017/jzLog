package com.jz.bigdata.common.assets.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jz.bigdata.common.assets.dao.IAssetsDao;
import com.jz.bigdata.common.assets.entity.Assets;
import com.jz.bigdata.common.assets.service.IAssetsService;
import net.sf.json.JSONArray;


@Service(value = "assetsService")
public class AssetsServiceimpl implements IAssetsService{
	
	@Resource
	private IAssetsDao assetsDao;
	
	private String oldDate;
	
	@Override
	public int insert(Assets assets) {
		// TODO Auto-generated method stub
		return assetsDao.insert(assets);
	}

	@Override
	public List<Assets> selectAll() {
		return assetsDao.selectAll();
	}

	@Override
	public int updateById(Assets assets) {
		// TODO Auto-generated method stub
		return assetsDao.updateById(assets);
	}

	@Override
	public int delete(String[] ids) {
		// TODO Auto-generated method stub
		return assetsDao.delete(ids);
	}

	@Override
	public String selectAllByPage(int pageIndex, int pageSize) {
		// 获取起始数
		int startRecord = (pageSize * (pageIndex - 1));
		// 获取总数
		List count = assetsDao.count();
		List listCount = new ArrayList<>();
		// 获取总数集合
		listCount = (List) count.get(0);

		Map<String, Object> map = new HashMap<String, Object>();
		// 总数添加到map
		map.put("count", (listCount.get(0)));
		// 查询所有数据
		List<Assets> listAssets = assetsDao.selectAllByPage(startRecord,pageSize);
		// System.err.println(listEquipment.get(0).getCreateTime());
		// 数据添加到map
		map.put("assets", listAssets);
		return JSONArray.fromObject(map).toString();
	}

	@Override
	public List<String> count() {
		// TODO Auto-generated method stub
		return assetsDao.count();
	}

	@Override
	public Assets selectOneAssets(Assets assets) {
		// TODO Auto-generated method stub
		return assetsDao.selectOneAssets(assets);
	}

	@Override
	public List<Assets> selectByIncrement() {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date =format.format(new Date());
		List<Assets> list  = assetsDao.selectByIncrement(oldDate, date);
		oldDate=date;
		return list;
	}
	


}

package com.jz.bigdata.business.logAnalysis.log.service.impl;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.From;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jz.bigdata.business.logAnalysis.log.service.IlogService;
import com.jz.bigdata.common.equipment.service.IEquipmentService;
import com.jz.bigdata.common.safeStrategy.entity.SafeStrategy;
import com.jz.bigdata.common.safeStrategy.service.ISafeStrategyService;
import com.jz.bigdata.framework.spring.es.elasticsearch.ClientTemplate;

import joptsimple.internal.Strings;

@Service(value="logService")
public class LogServiceImpl implements IlogService {
	
	@Autowired protected ClientTemplate clientTemplate;
	
	@Resource(name ="SafeStrategyService")  
    private ISafeStrategyService safeStrategyService;
	
	@Resource(name = "EquipmentService")
	private IEquipmentService equipmentService;
	
	// es 排序方式
	private SortOrder sortOrder;

	@Override
	public List<Map<String, Object>> index(String index,String type) {
		// TODO Auto-generated method stub
		return clientTemplate.selectAll(index, type,"logdate",SortOrder.DESC);
	}

	@Override
	public void insert(String index,String type,String json) {
		// TODO Auto-generated method stub
		clientTemplate.insert(index, type, json);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String deleteById(String index,String type,String id) {
		// TODO Auto-generated method stub
		return clientTemplate.delete(index, type, id);
	}

	@Override
	public void createIndex(String index) {
		// TODO Auto-generated method stub
		clientTemplate.createIndex(index);
	}
	
	@Override
	public List<Map<String, Object>> groupBy(String index, String[] types, String param,Map<String, String> map) {

		List<Map<String, Object>> list = null;
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (map!=null&&!map.isEmpty()) {
			for(Map.Entry<String, String> entry : map.entrySet()){
				if (entry.getKey().equals("logdate")) {
					queryBuilder.must(QueryBuilders.rangeQuery(entry.getKey()).format("yyyy-MM-dd").gte(entry.getValue()));
				}else if (entry.getKey().equals("domain_url")||entry.getKey().equals("complete_url")) {
					// 短语匹配
					queryBuilder.must(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
				}else {
					queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
				}
			}
			list = clientTemplate.getListGroupByQueryBuilder(index, types, param,queryBuilder);
		}else {
			list = clientTemplate.getListGroupByQueryBuilder(index, types, param,null);
		}
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> groupBy(String index, String[] types, String param,Map<String, String> map,int size) {

		List<Map<String, Object>> list = null;
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (map!=null&&!map.isEmpty()) {
			for(Map.Entry<String, String> entry : map.entrySet()){
				if (entry.getKey().equals("logdate")) {
					queryBuilder.must(QueryBuilders.rangeQuery(entry.getKey()).format("yyyy-MM-dd").gte(entry.getValue()));
				}else {
					queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
				}
			}
			list = clientTemplate.getListGroupByQueryBuilder(index, types, param,queryBuilder,size);
		}else {
			list = clientTemplate.getListGroupByQueryBuilder(index, types, param,null,size);
		}
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> groupBy(String index, String[] types, String[] param,Map<String, String> map,int size) {

		List<Map<String, Object>> list = null;
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (map!=null&&!map.isEmpty()) {
			for(Map.Entry<String, String> entry : map.entrySet()){
				if (entry.getKey().equals("logdate")) {
					queryBuilder.must(QueryBuilders.rangeQuery(entry.getKey()).format("yyyy-MM-dd").gte(entry.getValue()));
				}else {
					queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
				}
			}
			list = clientTemplate.getListGroupByQueryBuilder(index, types, param,queryBuilder,size);
		}else {
			list = clientTemplate.getListGroupByQueryBuilder(index, types, param,null,size);
		}
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param map
	 * @return
	 * service层 
	 */
	public List<Map<String, Object>> getListGroupByTime(String index,String types,String param,String equipmentid) {
		
		String groupby = "logtime_hour";
		List<Map<String, Object>> list = clientTemplate.getListGroupByQueryBuilder(index, types, param,groupby,equipmentid);
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param eventtype
	 * @return
	 * service层 
	 */
	@Override
	public List<Map<String, Object>> getListGroupByEvent(String index,String[] types,String equipmentid,String event_type,String starttime,String endtime) {
		
		QueryBuilder queryBuilder = null;
		BoolQueryBuilder Queryeid = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("equipmentid", equipmentid));
		BoolQueryBuilder Queryevent = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("event_type", event_type));
		BoolQueryBuilder Querydate = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime).lte(endtime));
		queryBuilder =  QueryBuilders.boolQuery()
				.must(Queryeid)
				.must(Queryevent)
				.must(Querydate);
		List<Map<String, Object>> list = clientTemplate.getListGroupByQueryBuilder(index, types, "event_type", queryBuilder);
		
		return list;
	}
	/**
	 * @param index
	 * @param types
	 * @param equipmentid
	 * @param enddate
	 * @return List<Map<String, Object>>
	 * service层 
	 */
	@Override
	public List<Map<String, Object>> getEventstypeCountByEquipmentid(String index,String[] types,String equipmentid,Date enddate) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endtime = format.format(enddate);
		DecimalFormat df = new DecimalFormat("#.00");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<SafeStrategy> safelist = safeStrategyService.selectByEquipmentId(equipmentid);
		
		Integer high_risk = 0;
		Integer moderate_risk = 0;
		Integer low_risk = 0;
		
		for (SafeStrategy safeStrategy : safelist) {
			String dates = safeStrategy.getTime();
			String event_type = safeStrategy.getEvent_type();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(enddate);
			calendar.add(Calendar.MINUTE, -Integer.valueOf(dates));
			Date startdate = calendar.getTime();
			String starttime = format.format(startdate);
			List<Map<String, Object>> loglist = getListGroupByEvent(index, types, equipmentid,event_type,starttime,endtime);
			
			if (!loglist.get(0).isEmpty()) {
				float per = Float.valueOf(loglist.get(0).get(safeStrategy.getEvent_type()).toString())/safeStrategy.getNumber();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("event_type", safeStrategy.getEvent_type());
				map.put("per",df.format(per));
				map.put("starttime",starttime);
				map.put("endtime", endtime);
				
				if (per>0.8) {
					// 高危数据
					high_risk++;
				}else if (per>0.5&&per<=0.8) {
					// 中危数据
					moderate_risk++;
				}else if (per<=0.5) {
					// 低危数据
					low_risk++;
				}
				list.add(map);
			}else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("event_type", safeStrategy.getEvent_type());
				map.put("per",df.format(0));
				map.put("starttime",starttime);
				map.put("endtime", endtime);
				low_risk++;
				list.add(map);
			}
		}
		
		equipmentService.upRiskById(equipmentid, high_risk, moderate_risk, low_risk);
		
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.jz.bigdata.business.logAnalysis.log.service.IlogService#getEventListGroupByTime(java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public List<Map<String, Object>> getEventListGroupByTime(String index,String[] types,String dates,String equipmentid,String eventtype,int i) {
		
		String groupby = "logtime_hour";
		int gte = 0;
		int lte = 7;
		QueryBuilder queryBuilder = null;
		String [] date = dates.split("-");
		QueryBuilder querytime = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery("logtime_year", date[0]))
				.must(QueryBuilders.matchPhraseQuery("logtime_month", date[1]))
				.must(QueryBuilders.matchPhraseQuery("logtime_day", date[2]));
		QueryBuilder existquery = QueryBuilders.boolQuery()
				.must(QueryBuilders.constantScoreQuery(QueryBuilders.existsQuery("event_type")));
		if (i==1) {
			gte = 0;
			lte = 3;
		}else if (i==2) {
			gte = 4;
			lte = 5;
		}else if (i==3) {
			gte = 6;
			lte = 7;
		}
		QueryBuilder rangequery = QueryBuilders.boolQuery()
				.must(QueryBuilders.rangeQuery("event_level").gte(gte).lte(lte));
		
		if(equipmentid!=null&&!equipmentid.equals("")) {
			QueryBuilder queryequipmentid = QueryBuilders.boolQuery()
					.must(QueryBuilders.termQuery("equipmentid", equipmentid));
			queryBuilder = QueryBuilders.boolQuery()
					.must(querytime)
					.must(existquery)
					.must(rangequery)
					.must(queryequipmentid);
		}else {
			queryBuilder = QueryBuilders.boolQuery()
					.must(existquery)
					.must(rangequery)
					.must(querytime);
		}
		List<Map<String, Object>> list = clientTemplate.getListGroupByQueryBuilder(index, types, groupby, queryBuilder,24);
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param dates
	 * @param equipmentid
	 * @param groupby
	 * @return
	 * todo
	 * service层 
	 */
	@Override
	public List<Map<String, Object>> getEventListGroupByEventType(String index,String[] types,String dates,String equipmentid,String groupby) {
		
		
		QueryBuilder queryBuilder = null;
		String [] date = dates.split("-");
		QueryBuilder querytime = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery("logtime_year", date[0]))
				.must(QueryBuilders.matchPhraseQuery("logtime_month", date[1]))
				.must(QueryBuilders.matchPhraseQuery("logtime_day", date[2]));
		QueryBuilder existquery = QueryBuilders.boolQuery()
				.must(QueryBuilders.constantScoreQuery(QueryBuilders.existsQuery("event_type")));
		
		if(equipmentid!=null&&!equipmentid.equals("")) {
			QueryBuilder queryequipmentid = QueryBuilders.boolQuery()
					.must(QueryBuilders.termQuery("equipmentid", equipmentid));
			queryBuilder = QueryBuilders.boolQuery()
					.must(querytime)
					.must(existquery)
					.must(queryequipmentid);
		}else {
			queryBuilder = QueryBuilders.boolQuery()
					.must(existquery)
					.must(querytime);
		}
		List<Map<String, Object>> list = clientTemplate.getListGroupByQueryBuilder(index, types, groupby, queryBuilder);
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> orderBy(String index, String type, String param,String order,String page,String size) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;

		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		if (param.isEmpty()) {
			return list;
		}
		if (order == null) {
			sortOrder = SortOrder.ASC;
		}else if (order.isEmpty()||order.equals("asc")) {
			sortOrder = SortOrder.ASC;
		}else if (order.equals("desc")) {
			sortOrder = SortOrder.DESC;
		}
		Map<String,String> map = new HashMap<String,String>();
		list = clientTemplate.getListOrderByParam(index, type, param, sortOrder,map,fromInt,sizeInt);
		return list;
	}
	
	/**
	 * 通过资产获取日志
	 *//*
	@Override
	public List<Map<String, Object>> getLogListByEquipmentId(String index, String type, String param,String order,String equipmentId,String page,String size) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;

		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		if (param.isEmpty()) {
			return list;
		}
		if (order == null) {
			sortOrder = SortOrder.ASC;
		}else if (order.isEmpty()||order.equals("asc")) {
			sortOrder = SortOrder.ASC;
		}else if (order.equals("desc")) {
			sortOrder = SortOrder.DESC;
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("equipmentid", equipmentId);
		
		String [] types = {type};
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.matchQuery("equipmentid", equipmentId));
		count = clientTemplate.count(index, types, queryBuilder);
		
		Map<String, Object> mapcount = new HashMap<String,Object>();
		//日志总量
		mapcount.put("count", count);
		list.add(mapcount);
		list.addAll(clientTemplate.getListOrderByParam(index, type, param, sortOrder,map,fromInt,sizeInt));
		//list = clientTemplate.getListOrderByParam(index, type, param, sortOrder,map,fromInt,sizeInt);
		return list;
	}*/
	
	@Override
	public void createIndexAndmapping(String index, String type,String mappingproperties) {
		
		if (mappingproperties!=null&&!mappingproperties.equals("")) {
			clientTemplate.addMapping(index, type, mappingproperties);
		}
		
		
	}
	
	/**
	 * @param index
	 * @param types
	 * @param content
	 * @return
	 * service层 混合查询 列+正文内容
	 */
	@Override
	public List<Map<String, Object>> getListByContent(String index,String[] types,String content,String page,String size) {
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		SearchHit[] hits = null;
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;

		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		// "多个匹配"  匹配的列进行归纳,包括设备id，设备ip，日志类型，日志内容
		if(content!=null&&!content.equals("")) {
			content = content.trim();
			QueryBuilder multiMatchQueryBuilder;
			if (content.contains(" ")&&content.length()>10) {
				//String length  = (content.split(" ").length)+"";
				
				//multiMatchQueryBuilder  = QueryBuilders.multiMatchQuery(content, "operation_des").fuzziness("AUTO");
				//multiMatchQueryBuilder = QueryBuilders.disMaxQuery().add(QueryBuilders.matchQuery("operation_des", content));
				//multiMatchQueryBuilder = QueryBuilders.queryStringQuery(content).field("operation_des").minimumShouldMatch(length);
				//multiMatchQueryBuilder = QueryBuilders.matchPhraseQuery("operation_des",content);
				multiMatchQueryBuilder = QueryBuilders.matchQuery("operation_des", content).operator(Operator.AND);
				count = clientTemplate.count(index, types, multiMatchQueryBuilder);
				if (count<1) {
					multiMatchQueryBuilder  = QueryBuilders.multiMatchQuery(content, "operation_level","operation_des","ip","hostname","process","operation_facility","userid").fuzziness("AUTO");
				}
			}else {
				multiMatchQueryBuilder  = QueryBuilders.multiMatchQuery(content, "operation_level","operation_des","ip","hostname","process","operation_facility","userid");
			}
			//MultiMatchQueryBuilder multiMatchQueryBuilder  = QueryBuilders.multiMatchQuery(content, "operation_level","operation_des","ip","hostname","process","operation_facility","userid").fuzziness("AUTO");
			count = clientTemplate.count(index, types, multiMatchQueryBuilder);
			hits = clientTemplate.getHitsByQueryBuilder(index, types, multiMatchQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
			// 在通过分词查询会后没有结果的情况下，在通过多字段模糊匹配查询，以达到查询效果的目的（该查询效率比较低）
			if (hits.length<1) {
				content = "*"+content.toLowerCase()+"*";
				
				QueryBuilder wildcardqueryBuilder1 = QueryBuilders.wildcardQuery("operation_des", content);
				QueryBuilder wildcardqueryBuilder2 = QueryBuilders.wildcardQuery("operation_level", content);
				QueryBuilder wildcardqueryBuilder3 = QueryBuilders.wildcardQuery("ip", content);
				QueryBuilder wildcardqueryBuilder4 = QueryBuilders.wildcardQuery("hostname", content);
				QueryBuilder wildcardqueryBuilder5 = QueryBuilders.wildcardQuery("process", content);
				QueryBuilder wildcardqueryBuilder6 = QueryBuilders.wildcardQuery("operation_facility", content);
				QueryBuilder wildcardqueryBuilder7 = QueryBuilders.wildcardQuery("userid", content);
				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
				boolQueryBuilder.should(wildcardqueryBuilder1);
				boolQueryBuilder.should(wildcardqueryBuilder2);
				boolQueryBuilder.should(wildcardqueryBuilder3);
				boolQueryBuilder.should(wildcardqueryBuilder4);
				boolQueryBuilder.should(wildcardqueryBuilder5);
				boolQueryBuilder.should(wildcardqueryBuilder6);
				boolQueryBuilder.should(wildcardqueryBuilder7);
				count = clientTemplate.count(index, types, boolQueryBuilder);
				hits = clientTemplate.getHitsByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
			}
		}else {
			count = clientTemplate.count(index, types, null);
			hits = clientTemplate.getHitsByQueryBuilder(index, types, null,"logdate",SortOrder.DESC,fromInt,sizeInt);
		}
			
		Map<String, Object> mapcount = new HashMap<String,Object>();
		//日志总量
		mapcount.put("count", count);
		list.add(mapcount);
		for(SearchHit hit : hits) {
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			HighlightField operation_desField = highlightFields.get("operation_des");
			HighlightField operation_levelField = highlightFields.get("operation_level");
			Map<String, Object> map = hit.getSourceAsMap();
			map.put("index", hit.getIndex());
			map.put("type", hit.getType());
			map.put("id", hit.getId());
			
			if (operation_desField!=null) {
				Text[] texts = operation_desField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_des",name);
			}
			if (operation_levelField!=null) {
				Text[] texts = operation_levelField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_level",name);
			}
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param content
	 * @param userid
	 * @return
	 * service层 混合查询 列+正文内容
	 */
	@Override
	public List<Map<String, Object>> getListByContent(String index,String[] types,String content,String userid,String page,String size) {
		/**
		 * 1.功能菜单点击日志检索，弹出页面
		 * 2.日志检索页面点击检索按钮
		 * 2.1有条件
		 * 2.2无条件
		 */
		
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		SearchHit[] hits = null;
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;
		
		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		QueryBuilder user = QueryBuilders.termQuery("userid", userid);
		// "多个匹配"  匹配的列进行归纳,包括设备id，设备ip，日志类型，日志内容
		if(content!=null&&!content.equals("")) {
			MultiMatchQueryBuilder multiMatchQueryBuilder  = QueryBuilders.multiMatchQuery(content, "operation_level","operation_des","ip","hostname","process","operation_facility","userid");
			BoolQueryBuilder QueryBuilder = QueryBuilders.boolQuery();
			QueryBuilder.should(multiMatchQueryBuilder);
			QueryBuilder.must(user);
			count = clientTemplate.count(index, types, QueryBuilder);
			hits = clientTemplate.getHitsByQueryBuilder(index, types, QueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
			//2.1无查询结果，在分词查询之后无结果的情况下，需要通过多字段模糊查询达到查询效果（查询效率较低）
			if (hits.length<1) {
				content = "*"+content.toLowerCase()+"*";
				
				QueryBuilder wildcardqueryBuilder1 = QueryBuilders.wildcardQuery("operation_des", content);
				QueryBuilder wildcardqueryBuilder2 = QueryBuilders.wildcardQuery("operation_level", content);
				QueryBuilder wildcardqueryBuilder3 = QueryBuilders.wildcardQuery("ip", content);
				QueryBuilder wildcardqueryBuilder4 = QueryBuilders.wildcardQuery("hostname", content);
				QueryBuilder wildcardqueryBuilder5 = QueryBuilders.wildcardQuery("process", content);
				QueryBuilder wildcardqueryBuilder6 = QueryBuilders.wildcardQuery("operation_facility", content);
				QueryBuilder wildcardqueryBuilder7 = QueryBuilders.wildcardQuery("userid", content);
				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
				boolQueryBuilder.should(wildcardqueryBuilder1);
				boolQueryBuilder.should(wildcardqueryBuilder2);
				boolQueryBuilder.should(wildcardqueryBuilder3);
				boolQueryBuilder.should(wildcardqueryBuilder4);
				boolQueryBuilder.should(wildcardqueryBuilder5);
				boolQueryBuilder.should(wildcardqueryBuilder6);
				boolQueryBuilder.should(wildcardqueryBuilder7);
				boolQueryBuilder.must(user);
				count = clientTemplate.count(index, types, boolQueryBuilder);
				hits = clientTemplate.getHitsByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
			}
		}else {
			//条件为空，全量提取，时间倒序排，只校验用户
			BoolQueryBuilder QueryBuilder = QueryBuilders.boolQuery();
			QueryBuilder.must(user);
			count = clientTemplate.count(index, types, QueryBuilder);
			hits = clientTemplate.getHitsByQueryBuilder(index, types, QueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
		}
		
		
		
		Map<String, Object> mapcount = new HashMap<String,Object>();
		//日志总量
		mapcount.put("count", count);
		list.add(mapcount);
		
		for(SearchHit hit : hits) {
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			HighlightField operation_desField = highlightFields.get("operation_des");
			HighlightField operation_levelField = highlightFields.get("operation_level");
			Map<String, Object> map = hit.getSourceAsMap();
			map.put("index", hit.getIndex());
			map.put("type", hit.getType());
			map.put("id", hit.getId());
			if (operation_desField!=null) {
				Text[] texts = operation_desField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_des",name);
			}
			if (operation_levelField!=null) {
				Text[] texts = operation_levelField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_level",name);
			}
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param map
	 * @return
	 * service层  组合查询+关键字
	 */
	@Override
	public List<Map<String, Object>> getListByBlend(String index,String[] types,Map<String, String> pamap,String page,String size) {
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		SearchHit[] hits = null;
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;

		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 时间段
		if (pamap.get("starttime")!=null&&pamap.get("endtime")!=null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(pamap.get("starttime")).lte(pamap.get("endtime")));
		}else if (pamap.get("starttime")!=null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(pamap.get("starttime")));
		}else if (pamap.get("endtime")!=null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").lte(pamap.get("endtime")));
		}
		if (pamap.get("event")!=null) {
			boolQueryBuilder.must(QueryBuilders.constantScoreQuery(QueryBuilders.existsQuery("event_type")));
		}
		if (pamap.get("event_levels")!=null) {
			if (pamap.get("event_levels").equals("高危")) {
				boolQueryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(0).lte(3));
			}else if (pamap.get("event_levels").equals("中危")) {
				boolQueryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(4).lte(5));
			}else if (pamap.get("event_levels").equals("普通")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(6).lte(7));
			}
			
		}
				
		// IP
		if (pamap.get("ip")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("ip", pamap.get("ip")));
		}
		// src_IP
		if (pamap.get("ipv4_src_addr")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("ipv4_src_addr", pamap.get("ipv4_src_addr")));
		}
		// dst_IP
		if (pamap.get("ipv4_dst_addr")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("ipv4_dst_addr", pamap.get("ipv4_dst_addr")));
		}
		// src_port
		if (pamap.get("l4_src_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("l4_src_port", pamap.get("l4_src_port")));
		}
		// dst_port
		if (pamap.get("l4_dst_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("l4_dst_port", pamap.get("l4_dst_port")));
		}
		// 协议名（传输层协议）
		if (pamap.get("protocol_name")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("protocol_name", pamap.get("protocol_name").toLowerCase()));
		}
		// 应用层协议
		if (pamap.get("application_layer_protocol")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("application_layer_protocol", pamap.get("application_layer_protocol").toLowerCase()));
		}
		// 应用层加密协议
		if (pamap.get("encryption_based_protection_protocol")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("encryption_based_protection_protocol", pamap.get("encryption_based_protection_protocol").toLowerCase()));
		}
		// equipmentid
		if (pamap.get("equipmentid")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("equipmentid", pamap.get("equipmentid")));
		}
		// event_level
		if (pamap.get("event_level")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("event_level", pamap.get("event_level")));
		}
		// operation_level
		if (pamap.get("operation_level")!=null) {
			//boolQueryBuilder.must(QueryBuilders.termQuery("operation_level", pamap.get("operation_level")));
			String [] operation_level = pamap.get("operation_level").split(",");
			boolQueryBuilder.must(QueryBuilders.termsQuery("operation_level", operation_level));
		}
		// event_type
		if (pamap.get("event_type")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("event_type", pamap.get("event_type")));
		}
		// equipmentname查询资产名称，局限性第一个字
		if (pamap.get("hostname")!=null) {
			//boolQueryBuilder.must();
			count = clientTemplate.count(index, types, QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.AND));
			if (count<1) {
				count = clientTemplate.count(index, types, QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.OR));
				if (count<1) {
					boolQueryBuilder.must(QueryBuilders.wildcardQuery("equipmentname", "*"+pamap.get("hostname")+"*"));
				}else {
					boolQueryBuilder.must(QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.OR));
				}
			}else {
				boolQueryBuilder.must(QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.AND));
			}
			//boolQueryBuilder.must(QueryBuilders.fuzzyQuery("equipmentname", pamap.get("hostname")));
			//boolQueryBuilder.must(QueryBuilders.matchQuery("equipmentname", pamap.get("hostname")).fuzziness("AUTO"));
			
		}
		// event_type
		if (pamap.get("event_type")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("event_type", pamap.get("event_type")));
		}
		
		//  针对HTTP日志的查询条件
		// src_IP
		if (pamap.get("source_ip")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("source_ip", pamap.get("source_ip")));
		}
		// dst_IP
		if (pamap.get("des_ip")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("des_ip", pamap.get("des_ip")));
		}
		// source_port
		if (pamap.get("source_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("source_port", pamap.get("source_port")));
		}
		// des_port
		if (pamap.get("des_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("des_port", pamap.get("des_port")));
		}
		// requestorresponse
		if (pamap.get("requestorresponse")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("requestorresponse", pamap.get("requestorresponse")));
		}
		// 请求方式
		if (pamap.get("request_type")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("request_type", pamap.get("request_type").toLowerCase()));
		}
		// 请求url
		if (pamap.get("request_url")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("request_url", pamap.get("request_url")));
		}
		// 响应状态
		if (pamap.get("response_state")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("response_state", pamap.get("response_state")));
		}
		if (pamap.get("packet_source")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("packet_source", pamap.get("packet_source")));
		}
		
		count = clientTemplate.count(index, types, boolQueryBuilder);
		hits = clientTemplate.getHitsByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
		
		Map<String, Object> mapcount = new HashMap<String,Object>();
		//日志总量
		mapcount.put("count", count);
		list.add(mapcount);
		
		for(SearchHit hit : hits) {
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			HighlightField operation_desField = highlightFields.get("operation_des");
			HighlightField operation_levelField = highlightFields.get("operation_level");
			Map<String, Object> map = hit.getSourceAsMap();
			map.put("index", hit.getIndex());
			map.put("type", hit.getType());
			map.put("id", hit.getId());
			if (operation_desField!=null) {
				Text[] texts = operation_desField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_des",name);
			}
			if (operation_levelField!=null) {
				Text[] texts = operation_levelField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_level",name);
			}
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param map
	 * @param userid
	 * @return
	 * service层  组合查询+关键字
	 */
	@Override
	public List<Map<String, Object>> getListByBlend(String index,String[] types,Map<String, String> pamap,String userid,String page,String size) {
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		SearchHit[] hits = null;
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;
		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 时间段
		if (pamap.get("starttime")!=null&&pamap.get("endtime")!=null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(pamap.get("starttime")).lte(pamap.get("endtime")));
		}else if (pamap.get("starttime")!=null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(pamap.get("starttime")));
		}else if (pamap.get("endtime")!=null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").lte(pamap.get("endtime")));
		}
		if (pamap.get("event")!=null) {
			boolQueryBuilder.must(QueryBuilders.constantScoreQuery(QueryBuilders.existsQuery("event_type")));
		}
		if (pamap.get("event_levels")!=null) {
			if (pamap.get("event_levels").equals("高危")) {
				boolQueryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(0).lte(3));
			}else if (pamap.get("event_levels").equals("中危")) {
				boolQueryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(4).lte(5));
			}else if (pamap.get("event_levels").equals("普通")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(6).lte(7));
			}
			
		}
		
		// IP
		if (pamap.get("ip")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("ip", pamap.get("ip")));
		}
		// src_IP
		if (pamap.get("ipv4_src_addr")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("ipv4_src_addr", pamap.get("ipv4_src_addr")));
		}
		// dst_IP
		if (pamap.get("ipv4_dst_addr")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("ipv4_dst_addr", pamap.get("ipv4_dst_addr")));
		}
		// src_port
		if (pamap.get("l4_src_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("l4_src_port", pamap.get("l4_src_port")));
		}
		// dst_port
		if (pamap.get("l4_dst_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("l4_dst_port", pamap.get("l4_dst_port")));
		}
		// 协议名
		if (pamap.get("protocol_name")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("protocol_name", pamap.get("protocol_name").toLowerCase()));
		}
		// equipmentid
		if (pamap.get("equipmentid")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("equipmentid", pamap.get("equipmentid")));
		}
		// event_level
		if (pamap.get("event_level")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("event_level", pamap.get("event_level")));
		}
		// operation_level
		if (pamap.get("operation_level")!=null) {
			//boolQueryBuilder.must(QueryBuilders.termQuery("operation_level", pamap.get("operation_level")));
			String [] operation_level = pamap.get("operation_level").split(",");
			boolQueryBuilder.must(QueryBuilders.termsQuery("operation_level", operation_level));
		}
		// event_type
		if (pamap.get("event_type")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("event_type", pamap.get("event_type")));
		}
		// equipmentname查询资产名称，局限性第一个字
		if (pamap.get("hostname")!=null) {
			//boolQueryBuilder.must();
			count = clientTemplate.count(index, types, QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.AND));
			if (count<1) {
				count = clientTemplate.count(index, types, QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.OR));
				if (count<1) {
					boolQueryBuilder.must(QueryBuilders.wildcardQuery("equipmentname", "*"+pamap.get("hostname")+"*"));
				}else {
					boolQueryBuilder.must(QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.OR));
				}
			}else {
				boolQueryBuilder.must(QueryBuilders.queryStringQuery(pamap.get("hostname")).field("equipmentname").defaultOperator(Operator.AND));
			}
			//boolQueryBuilder.must(QueryBuilders.fuzzyQuery("equipmentname", pamap.get("hostname")));
			//boolQueryBuilder.must(QueryBuilders.matchQuery("equipmentname", pamap.get("hostname")).fuzziness("AUTO"));
			
		}
		// event_type
		if (pamap.get("event_type")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("event_type", pamap.get("event_type")));
		}
		
		//  针对HTTP日志的查询条件
		// src_IP
		if (pamap.get("source_ip")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("source_ip", pamap.get("source_ip")));
		}
		// dst_IP
		if (pamap.get("des_ip")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("des_ip", pamap.get("des_ip")));
		}
		// source_port
		if (pamap.get("source_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("source_port", pamap.get("source_port")));
		}
		// des_port
		if (pamap.get("des_port")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("des_port", pamap.get("des_port")));
		}
		// requestorresponse
		if (pamap.get("requestorresponse")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("requestorresponse", pamap.get("requestorresponse")));
		}
		// 请求方式
		if (pamap.get("request_type")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("request_type", pamap.get("request_type").toLowerCase()));
		}
		// 请求url
		if (pamap.get("request_url")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("request_url", pamap.get("request_url")));
		}
		// 响应状态
		if (pamap.get("response_state")!=null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("response_state", pamap.get("response_state")));
		}
		// 流量日志来源
		if (pamap.get("packet_source")!=null) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("packet_source", pamap.get("packet_source")));
		}
		
		boolQueryBuilder.must(QueryBuilders.termQuery("userid", userid));
		
		count = clientTemplate.count(index, types, boolQueryBuilder);
		hits = clientTemplate.getHitsByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt);
		
		Map<String, Object> mapcount = new HashMap<String,Object>();
		//日志总量
		mapcount.put("count", count);
		list.add(mapcount);
		
		for(SearchHit hit : hits) {
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			HighlightField operation_desField = highlightFields.get("operation_des");
			HighlightField operation_levelField = highlightFields.get("operation_level");
			Map<String, Object> map = hit.getSourceAsMap();
			map.put("index", hit.getIndex());
			map.put("type", hit.getType());
			map.put("id", hit.getId());
			if (operation_desField!=null) {
				Text[] texts = operation_desField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_des",name);
			}
			if (operation_levelField!=null) {
				Text[] texts = operation_levelField.fragments();
				String name = "";
				for(Text text :texts){
					name += text;
				}
				map.put("operation_level",name);
			}
			
			list.add(map);
		}
		
		return list;
	}
	
	
	/**
	 * @param index
	 * @param types
	 * @param content
	 * @return
	 * service层  时间段+map
	 */
	@Override
	public List<Map<String, Object>> getListByMap(String index,String[] types,String starttime,String endtime,Map<String, String> map) {
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 时间段
		if (!starttime.equals("")&&!endtime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime).lte(endtime));
		}else if (!starttime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime));
		}else if (!endtime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").lte(endtime));
		}
		// 遍历map
		for(Map.Entry<String, String> entry : map.entrySet()){
			QueryBuilder queryBuilder = QueryBuilders.termQuery(entry.getKey(), entry.getValue());
			boolQueryBuilder.must(queryBuilder);
		}
		
		list = clientTemplate.getListByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC);
		
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param content
	 * @return
	 * service层  时间段+map+分页
	 */
	@Override
	public List<Map<String, Object>> getListByMap(String index,String[] types,String starttime,String endtime,Map<String, String> map,String page,String size) {
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;
		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 时间段
		if (!starttime.equals("")&&!endtime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime).lte(endtime));
		}else if (!starttime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime));
		}else if (!endtime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").lte(endtime));
		}
		// 遍历map
		for(Map.Entry<String, String> entry : map.entrySet()){
			if (entry.getKey().equals("operation_level")) {
				String [] operation_level = entry.getValue().split(",");
				boolQueryBuilder.must(QueryBuilders.termsQuery("operation_level", operation_level));
			}else if(entry.getKey().equals("dns_domain_name")){
				QueryBuilder queryBuilder = QueryBuilders.matchQuery(entry.getKey(), entry.getValue());
				boolQueryBuilder.must(queryBuilder);
			}else {
				QueryBuilder queryBuilder = QueryBuilders.matchQuery(entry.getKey(), entry.getValue());
				boolQueryBuilder.must(queryBuilder);
			}
			
		}
		//日志总量
		count = clientTemplate.count(index, types, boolQueryBuilder);
		Map<String, Object> mapcount = new HashMap<String,Object>();
		mapcount.put("count", count);
		
		list.add(mapcount);
		list.addAll(clientTemplate.getListByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt));
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param content
	 * @return
	 * service层  时间段+map+分页
	 */
	@Override
	public List<Map<String, Object>> getListByMap(String index,String[] types,String starttime,String endtime,Map<String, String> map,String userid, String page,String size) {
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
		Integer fromInt = 0;
		Integer sizeInt = 10;
		long count = 0;
		if (page!=null&&size!=null) {
			fromInt = (Integer.parseInt(page)-1)*Integer.parseInt(size);
			sizeInt = Integer.parseInt(size);
		}
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 时间段
		if (!starttime.equals("")&&!endtime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime).lte(endtime));
		}else if (!starttime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").gte(starttime));
		}else if (!endtime.equals("")) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("logdate").format("yyyy-MM-dd HH:mm:ss").lte(endtime));
		}
		// 遍历map
		for(Map.Entry<String, String> entry : map.entrySet()){
			if (entry.getKey().equals("operation_level")) {
				String [] operation_level = entry.getValue().split(",");
				boolQueryBuilder.must(QueryBuilders.termsQuery("operation_level", operation_level));
			}else {
				QueryBuilder queryBuilder = QueryBuilders.termQuery(entry.getKey(), entry.getValue());
				boolQueryBuilder.must(queryBuilder);
			}
			
		}
		//日志总量
		count = clientTemplate.count(index, types, boolQueryBuilder);
		Map<String, Object> mapcount = new HashMap<String,Object>();
		mapcount.put("count", count);
		
		list.add(mapcount);
		list.addAll(clientTemplate.getListByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC,fromInt,sizeInt));
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param map
	 * @return
	 * service层 
	 */
	@Override
	public List<Map<String, Object>> getListByMap(String index,String[] types,Map<String, String> map) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		for(Map.Entry<String, String> entry : map.entrySet()){
			/*QueryBuilder matchqueryBuilder = QueryBuilders.matchQuery(entry.getKey(), entry.getValue());
			boolQueryBuilder.must(matchqueryBuilder);*/
			QueryBuilder wildcardqueryBuilder = QueryBuilders.wildcardQuery(entry.getKey(), "*"+entry.getValue()+"*");
			boolQueryBuilder.must(wildcardqueryBuilder);
		}
		
		List<Map<String, Object>> list = clientTemplate.getListByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC);
		
		return list;
	}
	
	/**
	 * @param index
	 * @param types
	 * @param map + content
	 * @return
	 * service层 
	 */
	public List<Map<String, Object>> getListByMapAndContent(String index,String[] types,Map<String, String> map,String content) {
		
		String [] keys = {"equipmentid","operation_level","operation_des"};
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (!Strings.isNullOrEmpty(content)) {
			for(String key : keys){
				if (!map.containsKey(key)) {
					map.put(key, content);
				}
			}
		}
		for(Map.Entry<String, String> entry : map.entrySet()){
			/*QueryBuilder matchqueryBuilder = QueryBuilders.matchQuery(entry.getKey(), entry.getValue());
			boolQueryBuilder.must(matchqueryBuilder);*/
			QueryBuilder wildcardqueryBuilder = QueryBuilders.wildcardQuery(entry.getKey(), "*"+entry.getValue()+"*");
			boolQueryBuilder.should(wildcardqueryBuilder);
		}
		
		List<Map<String, Object>> list = clientTemplate.getListByQueryBuilder(index, types, boolQueryBuilder,"logdate",SortOrder.DESC);
		
		return list;
	}
	
	public <T> String getClassMapping(T classes) {
		
		StringBuilder fieldstring = new StringBuilder();
		
        Field[] fields = classes.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
             fieldstring.append("\t\t\t\t\"" + fields[i].getName().toLowerCase() + "\": {\n");
             fieldstring.append("\t\t\t\t\t\t\"type\": \""
                        + GetElasticSearchMappingType(fields[i].getType().getSimpleName(),fields[i].getName()) + "\n");
             if (fields[i].getName().equals("id")) {
            	 fieldstring.append("\t\t\t\t\t\t,\"index\": \""
                         + "false\"" + "\n");
			}
             if (!fields[i].getName().equals("operation_des")&&!fields[i].getType().getSimpleName().equals("Date")&&!fields[i].getName().equals("id")) {
				fieldstring.append("\t\t\t\t\t\t,\"fielddata\": "
                        + "true" + "\n");
			}
             if (i == fields.length-1) {
                    fieldstring.append("\t\t\t\t\t}\n");
                } else {
                    fieldstring.append("\t\t\t\t\t},\n");
                }
        }
        return fieldstring.toString();
	}

	private static String GetElasticSearchMappingType(String varType,String name) {
        String es = "text";
        switch (varType) {
        case "Date":
            es = "date\"\n"+"\t\t\t\t\t\t,\"format\":\"yyyy-MM-dd HH:mm:ss,S\"\n"+"\t\t\t\t\t\t";
            break;
        case "Double":
            es = "double\"\n"+"\t\t\t\t\t\t,\"null_value\":\"NaN\"";
            break;
        case "Long":
            es = "long\"";
            break;
        default:
        	if (name.equals("id")) {
        		es = "keyword\"";
			}else {
				es = "text\"";
			}
            
            break;
        }
        return es;
    }

	@Override
	public String searchById(String index, String type, String id) {
		
		return clientTemplate.searchById(index,type,id);
	}

	@Override
	public long getCount(String index, String[] types,Map<String, String> map) {
		
		long result = 0;
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (map!=null) {
			for(Map.Entry<String, String> entry : map.entrySet()){
				if (entry.getKey().equals("event")) {
					// 字段不为null查询
					queryBuilder.must(QueryBuilders.constantScoreQuery(QueryBuilders.existsQuery("event_type")));
				}else if(entry.getKey().equals("event_level")){
					// 范围查询
					queryBuilder.must(QueryBuilders.rangeQuery("event_level").gte(0).lte(3));
				}else if (entry.getKey().equals("domain_url")||entry.getKey().equals("complete_url")) {
					// 短语匹配
					queryBuilder.must(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
				}else {
					// 不分词精确查询
					queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
				}
			}
			try {
				result = clientTemplate.count(index, types,queryBuilder);
			} catch (Exception e) {
				result = 0;
			}
		}else {
			result = clientTemplate.count(index, types,null);
		}
		
		return result;
	}
	

	
	

	

}

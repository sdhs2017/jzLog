package com.jz.bigdata.common.menu.entity;

import java.util.List;

import com.jz.bigdata.common.department.entity.Department;

/**
 * @author shichengyu
 * @date 2018年5月10日 下午5:53:49
 * @description
 */
public class Menu {
	
	private int id;
	private String menuName;
	private Integer superiorId;
	private int childId;
	private String url;
	private String icon;
	
	private List<Menu> menus;
	
	
	
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Integer getSuperiorId() {
		return superiorId;
	}
	public void setSuperiorId(Integer superiorId) {
		this.superiorId = superiorId;
	}
	public int getChildId() {
		return childId;
	}
	public void setChildId(int childId) {
		this.childId = childId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
	
}

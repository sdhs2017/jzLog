package com.jz.bigdata.common.users.entity;

import com.jz.bigdata.common.department.entity.Department;

/**
 * @author shichengyu
 * @date 2017年8月1日 上午9:55:43
 * @description
 */
public class User {
	private String id;//员工id	
	private String phone;//手机号
	private String password;//密码
	private String name;//姓名
	private int sex;//性别
	private Integer age;//年龄
	private String email;//电子邮箱
	private int departmentId;//部门表id
	private int state;
	private int role;//角色
	private String roleName;//角色名称
//	private String ordPassword;//
	
//	private String departmentName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	private Department department;
	
	

	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
//	public String getDepartmentName() {
//		return departmentName;
//	}
//	public void setDepartmentName(String departmentName) {
//		this.departmentName = departmentName;
//	}
	public String getId() { 
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	
}

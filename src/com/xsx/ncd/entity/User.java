package com.xsx.ncd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="USER")
@Entity
public class User {
	
	private Integer id;
	
	private String account;
	
	private String password;
	
	private String fatheraccount;
	
	private String name;
	
	private String age;
	
	private String sex;
	
	private String phone;
	
	private String job;
	
	private String dsc;
	
	private Integer type;					//账号类型， 
											//0 -- 系统运维人员，拥有所有权限
											//1 -- 超级账号,不能上传客户的软件，其他权限全有
											//2 -- 销售，权限：添加和修改类别为3的用户
											//3 -- 纽康度生物研发，只能处理自己管辖的设备的报告
											//4 -- 一级用户（父账户为null）
											//5 -- 二级用户（父账户不为null）
	
	private String adduser;					//添加当前账户的人

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFatheraccount() {
		return fatheraccount;
	}

	public void setFatheraccount(String fatheraccount) {
		this.fatheraccount = fatheraccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAdduser() {
		return adduser;
	}

	public void setAdduser(String adduser) {
		this.adduser = adduser;
	}

	@Override
	public String toString() {
		if(this.type == 5)
			return "审核人-"+name;
		return "姓名" + name;
	}
}

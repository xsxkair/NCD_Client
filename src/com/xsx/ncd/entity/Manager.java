package com.xsx.ncd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="MANAGER")
@Entity
public class Manager {
	
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

	@Override
	public String toString() {
		return "Manager [id=" + id + ", account=" + account + ", password=" + password + ", fatheraccount="
				+ fatheraccount + ", name=" + name + "]";
	}
	
	
}

package com.xsx.ncd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xsx.ncd.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer>{
	
	public Manager findManagerByAccount(String account);
	
	public Manager findManagerByAccountAndPassword(String account, String password);
}

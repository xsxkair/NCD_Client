package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer>{
	
	public Manager findManagerByAccount(String account);
	
	public Manager findManagerByAccountAndPassword(String account, String password);

	@Query("select m from Manager m where m.fatheraccount=:father")
	public List<Manager> queryChildAccountList(@Param("father") String fatheraccount);
}

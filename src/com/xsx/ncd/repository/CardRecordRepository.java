package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Manager;


public interface CardRecordRepository extends JpaRepository<CardRecord, Integer>{
	
	@Query("SELECT cr.card.item, SUM(cr.num) FROM CardRecord cr where cr.manager in (:managers) GROUP BY cr.card.item ")
	public List<Object[]> QueryCardRepertoryNumByItem(@Param("managers") List<Manager> managers);
	

	
	/*
	 * 根据日，查询这天之前所有设备入库的总和
	 */
	//public List<Object[]> QueryCardInNumByTime();
}

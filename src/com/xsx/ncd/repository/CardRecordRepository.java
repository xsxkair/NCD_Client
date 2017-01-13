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
	 * �����գ���ѯ����֮ǰ�����豸�����ܺ�
	 */
	//public List<Object[]> QueryCardInNumByTime();
}

package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;


public interface CardRecordRepository extends JpaRepository<CardRecord, Integer>, JpaSpecificationExecutor<CardRecord>{
	
	/*
	 * 根据测试项目查询总库存
	 */
//	@Query("SELECT c.item, SUM(cr.num) FROM CardRecord cr, Card c where cr.userid in (:users) AND cr.cardid=c.id GROUP BY c.item ")
//	public List<Object[]> QueryCardRepertoryNumByItem(@Param("users") List<Integer> userid);
	

	/*
	 * 截止到当前时间，当前设备的每个项目的总数
	 */
/*	@Query("SELECT c.device, c.card.item as c_item, ABS(SUM(c.num)) as c_num FROM CardRecord cr, Card c, Device d "
			+ "where c.device=:device "
			+ " GROUP BY c.device, c.card.item ")
	public List<Object[]> QueryCardRepertoryGroupByDeviceAndItem(@Param("device")Device devices);
	*/
	/*
	 * 截止到当前时间，当前设备的每个项目的使用量
	 */
/*	@Query("SELECT t.device, t.card.item as t_item, COUNT(t.id) as t_num FROM TestData t "
			+ "where t.device=:device "
			+ " GROUP BY t.device, t.card.item ")
	public List<Object[]> QueryCardUseNumGroupByDeviceAndItem(@Param("device")Device devices);
	*/
	
}


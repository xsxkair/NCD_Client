package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.User;


public interface CardRecordRepository extends JpaRepository<CardRecord, Integer>, JpaSpecificationExecutor<CardRecord>, MyCardRecordDao{
	
	/*
	 * ���ݲ�����Ŀ��ѯ�ܿ��
	 */
	@Query("SELECT c.item, SUM(cr.num) FROM CardRecord cr, Card c where cr.account in (:users) AND cr.cid = c.cid GROUP BY c.item ")
	public List<Object[]> QueryCardRepertoryNumByItem(@Param("users") List<String> userid);
	

	/*
	 * ��ֹ����ǰʱ�䣬��ǰ�豸��ÿ����Ŀ������
	 */
	@Query("SELECT cr.did, c.item , ABS(SUM(cr.num))  FROM CardRecord cr, Card c "
			+ "where cr.did=:device AND cr.cid=c.cid "
			+ " GROUP BY cr.did, c.item ")
	public List<Object[]> QueryCardRepertoryGroupByDeviceAndItem(@Param("device")String devices);
	
	/*
	 * ��ֹ����ǰʱ�䣬��ǰ�豸��ÿ����Ŀ��ʹ����
	 */
	@Query("SELECT t.did, c.item as t_item, COUNT(t.id) as t_num FROM TestData t "
			+ "left join Card c on c.cid=t.cid "
			+ "where t.did=:device "
			+ " GROUP BY t.did, c.item ")
	public List<Object[]> QueryCardUseNumGroupByDeviceAndItem(@Param("device")String devices);
	
}


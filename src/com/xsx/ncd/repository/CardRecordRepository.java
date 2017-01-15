package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.CardRecord;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Manager;


public interface CardRecordRepository extends JpaRepository<CardRecord, Integer>, JpaSpecificationExecutor<CardRecord>{
	
	/*
	 * ���ݲ�����Ŀ��ѯ�ܿ��
	 */
	@Query("SELECT cr.card.item, SUM(cr.num) FROM CardRecord cr where cr.manager in (:managers) GROUP BY cr.card.item ")
	public List<Object[]> QueryCardRepertoryNumByItem(@Param("managers") List<Manager> managers);
	

	/*
	 * ��ֹ����ǰʱ�䣬��ǰ�豸��ÿ����Ŀ������
	 */
	@Query("SELECT c.device, c.card.item as c_item, ABS(SUM(c.num)) as c_num FROM CardRecord c "
			+ "where c.device=:device "
			+ " GROUP BY c.device, c.card.item ")
	public List<Object[]> QueryCardRepertoryGroupByDeviceAndItem(@Param("device")Device devices);
	
	/*
	 * ��ֹ����ǰʱ�䣬��ǰ�豸��ÿ����Ŀ��ʹ����
	 */
	@Query("SELECT t.device, t.card.item as t_item, COUNT(t.id) as t_num FROM TestData t "
			+ "where t.device=:device "
			+ " GROUP BY t.device, t.card.item ")
	public List<Object[]> QueryCardUseNumGroupByDeviceAndItem(@Param("device")Device devices);
	
	
}


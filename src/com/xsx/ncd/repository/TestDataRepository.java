package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.TestData;

public interface TestDataRepository extends JpaRepository<TestData, Integer>, JpaSpecificationExecutor<TestData>, MyTestDataDao{
	
	@Query("select t from TestData t where t.cnum=:cnum and t.card.cid=:cid")
	public TestData queryByCardCidAndCnum(@Param("cid")String cid, @Param("cnum")String cnum);
	
	@Query(value="SELECT t.result,COUNT(t.id) FROM TestData t where DATE(t.testtime)=CURRENT_DATE and t.device in (:devicelist) GROUP BY (t.result)")
	public List<Object[]> queryTodayReportGroupByResult( @Param("devicelist")List<Device> devices);
	
	@Query(value="SELECT t.card.item,COUNT(t.id) FROM TestData t where DATE(t.testtime)=CURRENT_DATE and t.device in (:devicelist) GROUP BY (t.card.item)")
	public List<Object[]> queryTodayReportGroupByItem( @Param("devicelist")List<Device> devices);
	
	@Query(value="SELECT t.device.did, COUNT(t.id) FROM TestData t where DATE(t.testtime)=CURRENT_DATE and t.device in (:devicelist) GROUP BY (t.device.did)")
	public List<Object[]> queryTodayReportGroupByDevice( @Param("devicelist")List<Device> devices);
}

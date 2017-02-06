package com.xsx.ncd.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xsx.ncd.entity.TestData;

public interface TestDataRepository extends JpaRepository<TestData, Integer>, JpaSpecificationExecutor<TestData>, MyTestDataDao{
	
	
	//@Query("select t from TestData t where t.cnum=:cnum and t.card.cid=:cid")
	//public TestData queryByCardCidAndCnum(@Param("cid")String cid, @Param("cnum")String cnum);
	
/*	@Query(value="SELECT t.result,COUNT(t.id) FROM TestData t where DATE(t.testtime)=CURRENT_DATE and t.deviceid in (:devicelist) GROUP BY (t.result)")
	public List<Object[]> queryTodayReportGroupByResult( @Param("devicelist")List<Integer> devices);
	
	@Query(value="SELECT t.card.item,COUNT(t.id) FROM TestData t where DATE(t.testtime)=CURRENT_DATE and t.deviceid in (:devicelist) GROUP BY (t.card.item)")
	public List<Object[]> queryTodayReportGroupByItem( @Param("devicelist")List<Integer> devices);
	
	@Query(value="SELECT t.device.did, COUNT(t.id) FROM TestData t, Device d where DATE(t.testtime)=CURRENT_DATE and t.deviceid in (:devicelist) GROUP BY (t.device.did)")
	public List<Object[]> queryTodayReportGroupByDevice( @Param("devicelist")List<Integer> devices);
	
	//��ͼ�꣬������Ŀ
	@Query("select t.card.item, DATE_FORMAT(t.testtime,'%Y'), count(t.id) from TestData t where t.deviceid in (:devices) group by t.card.item, DATE_FORMAT(t.testtime,'%Y')")
	public List<Object[]> queryReportSummyByYearItem(@Param("devices")List<Integer>devices);
	
	//��ͼ�£�������Ŀ
	@Query("select t.card.item, DATE_FORMAT(t.testtime,'%Y-%m'), count(t.id) from TestData t where t.deviceid in (:devices) group by t.card.item, DATE_FORMAT(t.testtime,'%Y��%m��')")
	public List<Object[]> queryReportSummyByMonthItem(@Param("devices")List<Integer>devices);
	
	//��ͼ�գ�������Ŀ
	@Query("select t.card.item, DATE_FORMAT(t.testtime,'%Y-%m-%d'), count(t.id) from TestData t where t.deviceid in (:devices) group by t.card.item, DATE_FORMAT(t.testtime,'%Y��%m��%d��')")
	public List<Object[]> queryReportSummyByDayItem(@Param("devices")List<Integer>devices);
	
	//��ͼ�꣬�����豸
	@Query("select t.device.did, DATE_FORMAT(t.testtime,'%Y'), count(t.id) from TestData t where t.device in (:devices) group by t.device, DATE_FORMAT(t.testtime,'%Y')")
	public List<Object[]> queryReportSummyByYearDevice(@Param("devices")List<Device>devices);
	
	//��ͼ�£������豸
	@Query("select t.device.did, DATE_FORMAT(t.testtime,'%Y-%m'), count(t.id) from TestData t where t.device in (:devices) group by t.device, DATE_FORMAT(t.testtime,'%Y��%m��')")
	public List<Object[]> queryReportSummyByMonthDevice(@Param("devices")List<Device>devices);
	
	//��ͼ�գ������豸
	@Query("select t.device.did, DATE_FORMAT(t.testtime,'%Y-%m-%d'), count(t.id) from TestData t where t.device in (:devices) group by t.device, DATE_FORMAT(t.testtime,'%Y��%m��%d��')")
	public List<Object[]> queryReportSummyByDayDevice(@Param("devices")List<Device>devices);
*/
}

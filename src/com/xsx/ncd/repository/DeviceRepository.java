package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device>{

	public Device findDeviceByDid(String did);
	
	public Device findById(Integer id);
	
	@Query("SELECT d.did FROM Device d where d.account = :account" )
	public List<String> queryDidByAccount(@Param("account") String account);
	
	@Query("SELECT d FROM Device d where d.account = :account  ORDER BY d.time DESC" )
	public List<Device> findByAccount(@Param("account") String account);
	
	@Query("SELECT DATE_FORMAT(t.testtime,'%Y') ,COUNT(t.id) "
			+"FROM TestData t where t.did=:deviceid GROUP BY DATE_FORMAT(t.testtime,'%Y')" )
	public List<Object[]> queryDeviceActivenessByYear(@Param("deviceid") String deviceid);
	
	@Query("SELECT DATE_FORMAT(t.testtime,'%Y-%m') ,COUNT(t.id) "
			+"FROM TestData t where t.did=:deviceid GROUP BY DATE_FORMAT(t.testtime,'%Y-%m')" )
	public List<Object[]> queryDeviceActivenessByMonth(@Param("deviceid") String deviceid);
	
	@Query("SELECT DATE_FORMAT(t.testtime,'%Y-%m-%d') ,COUNT(t.id) "
			+"FROM TestData t where t.did=:deviceid GROUP BY DATE_FORMAT(t.testtime,'%Y-%m-%d')" )
	public List<Object[]> queryDeviceActivenessByDay(@Param("deviceid") String deviceid);
	
	@Query("SELECT d.did FROM Device d " )
	public List<String> quaryAllDeviceId();
	
	@Query("SELECT d FROM Device d ORDER BY d.time DESC" )
	public List<Device> quaryAllDevice();
}

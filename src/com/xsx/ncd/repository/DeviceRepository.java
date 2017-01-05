package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device>{

	public Device findDeviceByDid(String did);
	
	public List<Device> findByManagerAccount(String account);
	
	@Query(value = "SELECT DATE_FORMAT(t.testtime,'%YÄê%mÔÂ'),COUNT(DATE_FORMAT(t.testtime,'%Y%m')) "
			+"FROM testdata t INNER JOIN(SELECT id FROM device WHERE did=:deviceid)d ON t.DEVICE_id = d.id GROUP BY DATE_FORMAT(t.testtime,'%Y%m')",
			nativeQuery = true)
	public List<Object[]> queryDeviceActiveness(@Param("deviceid") String deviceId);
}

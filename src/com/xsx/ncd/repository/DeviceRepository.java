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
	
	//通过审核人的id查找其管辖的设备
	@Query("select d.id from Device d where d.userid=:userid")
	public List<Integer> queryDeviceIdByUserid(@Param("userid")Integer userid);
	
	public List<Device> findByUserid(Integer userid);
	
	@Query("SELECT DATE_FORMAT(t.testtime,'%Y-%m') ,COUNT(t.id) "
			+"FROM TestData t where t.deviceid=:deviceid GROUP BY DATE_FORMAT(t.testtime,'%Y-%m')" )
	public List<Object[]> queryDeviceActiveness(@Param("deviceid") Integer deviceid);
}

package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xsx.ncd.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer>{

	public Device findDeviceByDid(String did);
	
	public List<Device> findByManagerAccount(String account);
}

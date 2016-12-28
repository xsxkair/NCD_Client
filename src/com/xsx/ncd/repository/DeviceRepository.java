package com.xsx.ncd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xsx.ncd.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer>{

	public Device findDeviceByDid(String did);
}

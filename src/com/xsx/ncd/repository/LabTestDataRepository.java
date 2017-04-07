package com.xsx.ncd.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.xsx.ncd.entity.LabTestData;

public interface LabTestDataRepository extends JpaRepository<LabTestData, Integer>, MyLabTestDataDao{
	

}

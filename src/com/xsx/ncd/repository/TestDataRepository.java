package com.xsx.ncd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.TestData;

public interface TestDataRepository extends JpaRepository<TestData, Integer>{
	
	@Query("select t from TestData t where t.cnum=:cnum and t.card.cid=:cid")
	public TestData queryByCardCidAndCnum(@Param("cid")String cid, @Param("cnum")String cnum);
}

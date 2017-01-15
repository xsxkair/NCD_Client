package com.xsx.ncd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xsx.ncd.entity.NcdSoft;

public interface NcdSoftRepository extends JpaRepository<NcdSoft, Integer>{
	
	public NcdSoft findNcdSoftByName(String softname);
}

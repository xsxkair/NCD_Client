package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xsx.ncd.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer>, MyCardDao{
	
	public Card findCardByCid(String cid);
	
	@Query("select DISTINCT item from Card")
	public List<String> queryItem();
}

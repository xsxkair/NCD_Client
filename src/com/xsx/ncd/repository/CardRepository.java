package com.xsx.ncd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xsx.ncd.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer>{
	
	public Card findCardByCid(String cid);
}

package com.xsx.ncd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xsx.ncd.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByAccount(String account);
	
	public User findByAccountAndPassword(String account, String password);

	@Query("select m.account from User m where m.fatheraccount=:father")
	public List<String> queryChildAccountList(@Param("father") String fatheraccount);
	
	@Query("select m from User m where m.fatheraccount=:father")
	public List<User> queryChildUserList(@Param("father") String fatheraccount);
	
	@Query("select u.id from User u where u.fatheraccount=:father")
	public List<Integer> queryChildIdList(@Param("father") String fatheraccount);
	
	@Query("select u from User u where u.type=1")
	public List<User> queryAllAdministrator();
	
	@Query("select u from User u where u.type=2 and u.fatheraccount IS NULL ")
	public List<User> queryAllFatherSaler();
	
	@Query("select u from User u where u.type=3 and u.fatheraccount IS NULL ")
	public List<User> queryAllFatherNcdLaber();
	
	
	@Query("select u from User u where u.type=4 and u.fatheraccount IS NULL ")
	public List<User> queryAllFatherManager();

}

package com.xsx.ncd.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class TestDataRepositoryImpl implements MyTestDataDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Object[] QueryTodayReport(List<String> deviceids, int firstResultIndex, int maxResult) {
		// TODO Auto-generated method stub
		Object[] result = new Object[2];
		
		StringBuffer sqlHead1 = new StringBuffer("select count(td.id) FROM TestData td ");
		StringBuffer sqlHead2 = new StringBuffer("SELECT td,d,c,u FROM TestData td left join Device d on d.did=td.did "
        		+ "left join Card c on c.cid=td.cid "
        		+ "left join User u on u.account=td.account ");
		//定义SQL   
        StringBuffer sql1 =  new StringBuffer("where td.result='未审核' AND td.did in(:devicelist) ");

        //查询总数
        try {
        	sqlHead1.append(sql1);
            Query query1 = em.createQuery(sqlHead1.toString());
            query1.setParameter("devicelist", deviceids);
            Long num = (Long) query1.getSingleResult();
            result[0] = (num%maxResult == 0)?(num/maxResult):(num/maxResult+1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
        try {
        	sqlHead2.append(sql1);
            Query query2 =  em.createQuery(sqlHead2.toString());
            query2.setParameter("devicelist", deviceids);
            query2.setFirstResult(firstResultIndex);
            query2.setMaxResults(maxResult);
           
            List<Object[]> datas = query2.getResultList();
            result[1] = datas;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

        em.close();
        
		return result;
	}

	@Override
	public List<Object[]> queryTodayReportGroupByResult(List<String> devices) {
		// TODO Auto-generated method stub
		java.sql.Date tempdate = new java.sql.Date(System.currentTimeMillis());
		
		StringBuffer sqlHead1 = new StringBuffer("SELECT t.result,COUNT(t.id) FROM TestData t where DATE(t.testtime)=:testdate "
				+ "and t.did in (:devicelist) GROUP BY (t.result)");
        
        Query query = em.createQuery(sqlHead1.toString());
        
        query.setParameter("testdate", tempdate);
        query.setParameter("devicelist", devices);
        
        List<Object[]> datas = query.getResultList();
		 
        em.close();
	        
		return datas;
	}

	@Override
	public List<Object[]> queryTodayReportGroupByItem(List<String> devices) {
		// TODO Auto-generated method stub
		java.sql.Date tempdate = new java.sql.Date(System.currentTimeMillis());

		StringBuffer sqlHead1 = new StringBuffer("SELECT c.item, COUNT(t.id) FROM TestData t left join Card c on c.cid=t.cid "
				+ "where DATE(t.testtime)=:testdate "
				+ "and t.did in (:devicelist) GROUP BY (c.item)");
        
        Query query = em.createQuery(sqlHead1.toString());
        
        query.setParameter("testdate", tempdate);
        query.setParameter("devicelist", devices);
        
        List<Object[]> datas = query.getResultList();
		 
        em.close();
	        
		return datas;
	}

	@Override
	public List<Object[]> queryTodayReportGroupByDevice(List<String> devices) {
		// TODO Auto-generated method stub
		java.sql.Date tempdate = new java.sql.Date(System.currentTimeMillis());

		StringBuffer sqlHead1 = new StringBuffer("SELECT d.did, COUNT(t.id) FROM TestData t left join Device d on d.did=t.did "
				+ "where DATE(t.testtime)=:testdate "
				+ "and t.did in (:devicelist) GROUP BY (d.did)");
        
        Query query = em.createQuery(sqlHead1.toString());
        
        query.setParameter("testdate", tempdate);
        query.setParameter("devicelist", devices);
        
        List<Object[]> datas = query.getResultList();
		 
        em.close();
	        
		return datas;
	}

	@Override
	public List<Object[]> queryReportSummy(List<String> devices, String isItem, String dateType) {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer("SELECT ");
		
		//项目分组
		if(isItem.equals("项目分组"))
			sql.append("c.item, ");
		//设备分组
		else
			sql.append("d.did, ");
		
		//年视图
		if(dateType.equals("年")){
			sql.append("DATE_FORMAT(t.testtime,'%Y'), ");
		}
		//年月视图
		else if (dateType.equals("月")) {
			sql.append("DATE_FORMAT(t.testtime,'%Y-%m'), ");
		}
		//年月日视图
		else {
			sql.append("DATE_FORMAT(t.testtime,'%Y-%m-%d'), ");
		}
		
		sql.append("count(t.id) ");
		
		sql.append("from TestData t left join Device d on d.did=t.did "
				+ "left join Card c on c.cid=t.cid "
        		+ "left join User u on u.account=t.account "
        		+ "where t.did in (:devicelist) Group By ");
		
		//项目分组
		if(isItem.equals("项目分组"))
			sql.append("c.item, ");
		//设备分组
		else
			sql.append("d.did, ");
				
		//年视图
		if(dateType.equals("年")){
			sql.append("DATE_FORMAT(t.testtime,'%Y')");
		}
		//年月视图
		else if (dateType.equals("月")) {
			sql.append("DATE_FORMAT(t.testtime,'%Y-%m')");
		}
		//年月日视图
		else {
			sql.append("DATE_FORMAT(t.testtime,'%Y-%m-%d')");
		}
		
		 Query query = em.createQuery(sql.toString());

		 query.setParameter("devicelist", devices);
	        
		 List<Object[]> datas = query.getResultList();
			 
		 em.close();
	        
		return datas;
	}

	@Override
	public Object[] QueryReportList(String item, java.sql.Date testDate, String testerName, String deviceId,
			List<String> devices, String sampleId, String result, int firstResultIndex, int maxResult) {
		// TODO Auto-generated method stub
		Object[] reportObject = new Object[2];
		
		StringBuffer sqlHead1 = new StringBuffer("select count(t.id) FROM TestData t ");
		StringBuffer sqlHead2 = new StringBuffer("SELECT t,d,c,u FROM TestData t ");
		//定义SQL   
        StringBuffer sql =  new StringBuffer("left join Device d on d.did=t.did "
        		+ "left join Card c on c.cid=t.cid "
        		+ "left join User u on u.account=t.account "
        		+ "where ");
        
        if(deviceId != null)
        	sql.append("t.did = :deviceid ");
        else
        	sql.append("t.did in(:devicelist) ");
        
        if(item != null)
        	sql.append("AND c.item like :item  ");
        
        if(testDate != null)
        	sql.append("AND DATE(t.testtime)=:testdate ");
        
        if(testerName != null)
        	sql.append("AND t.t_name like :testerName ");
        
        if(sampleId != null)
        	sql.append("AND t.sid like :sampleId ");
        
        if(result != null)
        	sql.append("AND t.result=:result ");
        
        //查询总数
        try {
        	sqlHead1.append(sql);

            Query query1 = em.createQuery(sqlHead1.toString());
            
            if(deviceId != null)
            	query1.setParameter("deviceid", deviceId);
            else
            	query1.setParameter("devicelist", devices);
            
            if(item != null)
            	query1.setParameter("item", "%"+item+"%");
            
            if(testDate != null)
            	query1.setParameter("testdate", testDate);
            
            if(testerName != null)
            	query1.setParameter("testerName", "%"+testerName+"%");
            
            if(sampleId != null)
            	query1.setParameter("sampleId", "%"+sampleId+"%");
            
            if(result != null)
            	query1.setParameter("result", result);
            
            Long num = (Long) query1.getSingleResult();
            reportObject[0] = (num%maxResult == 0)?(num/maxResult):(num/maxResult+1);

            sqlHead2.append(sql);

            Query query2 =  em.createQuery(sqlHead2.toString());
            
            if(deviceId != null)
            	query2.setParameter("deviceid", deviceId);
            else
            	query2.setParameter("devicelist", devices);
            
            if(item != null)
            	query2.setParameter("item", "%"+item+"%");
            
            if(testDate != null)
            	query2.setParameter("testdate", testDate);
            
            if(testerName != null)
            	query2.setParameter("testerName", "%"+testerName+"%");
            
            if(sampleId != null)
            	query2.setParameter("sampleId", "%"+sampleId+"%");
            
            if(result != null)
            	query2.setParameter("result", result);
            
            query2.setFirstResult(firstResultIndex);
            query2.setMaxResults(maxResult);
            
            List<Object[]> datas = query2.getResultList();
            reportObject[1] = datas;
            
            em.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
        
		return reportObject;
	}

}

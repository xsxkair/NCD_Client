package com.xsx.ncd.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class LabTestDataRepositoryImpl implements MyLabTestDataDao{
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Object[] QueryLabDataList(String userid, String deviceid, java.sql.Timestamp startTime, java.sql.Timestamp endTime, 
			int firstResultIndex, int maxResult) {
		// TODO Auto-generated method stub
		Object[] reportObject = new Object[2];
		
		StringBuffer sqlHead1 = new StringBuffer("select count(l.id) FROM LabTestData l ");
		StringBuffer sqlHead2 = new StringBuffer("SELECT l.id, l.tindex, l.userid, l.did, l.testtime, l.t_c, l.dsc FROM LabTestData l ");
		StringBuffer sql =  new StringBuffer();
		
		//定义SQL 
		if((userid != null)&&(userid.length() > 0))
			sql.append("AND l.userid like :userid  ");
	        
		if((deviceid != null)&&(deviceid.length() > 0))
			sql.append("AND l.did like :deviceid  ");
	        
		if(startTime != null)
			sql.append("AND l.testtime >= :startTime ");
	        
		if(endTime != null)
			sql.append("AND l.testtime <= :endTime ");
		
		if(sql.indexOf("AND") >= 0)
			sql.replace(sql.indexOf("AND"), sql.indexOf("AND")+3, "WHERE");
        
        
        //查询总数
        try {
        	sqlHead1.append(sql);

            Query query1 = em.createQuery(sqlHead1.toString());
            
            if((userid != null)&&(userid.length() > 0))
            	query1.setParameter("userid", "%"+userid+"%");
    	        
    		if((deviceid != null)&&(deviceid.length() > 0))
    			query1.setParameter("deviceid", "%"+deviceid+"%");
    	        
    		if(startTime != null)
    			query1.setParameter("startTime", startTime);
    	        
    		if(endTime != null)
    			query1.setParameter("endTime", endTime);
            
            Long num = (Long) query1.getSingleResult();
            reportObject[0] = (num%maxResult == 0)?(num/maxResult):(num/maxResult+1);

            sqlHead2.append(sql);

            Query query2 =  em.createQuery(sqlHead2.toString());

            if((userid != null)&&(userid.length() > 0))
            	query2.setParameter("userid", "%"+userid+"%");
    	        
    		if((deviceid != null)&&(deviceid.length() > 0))
    			query2.setParameter("deviceid", "%"+deviceid+"%");
    	        
    		if(startTime != null)
    			query2.setParameter("startTime", startTime);
    	        
    		if(endTime != null)
    			query2.setParameter("endTime", endTime);
            
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

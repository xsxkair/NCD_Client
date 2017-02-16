package com.xsx.ncd.repository;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class CardRecordRepositoryImpl implements MyCardRecordDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Object[] queryCardRecord(List<String> userAccount, Date startDate, Date endDate, int firstResultIndex,
			int maxResult) {
		// TODO Auto-generated method stub
		Object[] result = new Object[2];
		
		StringBuffer sqlHead1 = new StringBuffer("select count(cr.id) FROM CardRecord cr ");
		StringBuffer sqlHead2 = new StringBuffer("SELECT cr,d,c,u FROM CardRecord cr left join Device d on d.did=cr.did "
        		+ "left join Card c on c.cid=cr.cid "
        		+ "left join User u on u.account=cr.account ");
		//定义SQL   
        StringBuffer sql1 =  new StringBuffer("where cr.account in(:userlist) ");
        if(startDate == null){
        	if(endDate != null)
        		sql1.append("AND cast(cr.dotime as date) <= :endtime ");
        }
        else if (endDate == null) {
        	sql1.append("AND cast(cr.dotime as date) >= :starttime ");
		}
        else {
        	sql1.append("AND cast(cr.dotime as date) between :starttime and :endtime ");
		}

        //查询总数
        try {
        	sqlHead1.append(sql1);
            Query query1 = em.createQuery(sqlHead1.toString());
            query1.setParameter("userlist", userAccount);
            
            if(startDate != null)
            	query1.setParameter("starttime", startDate);

            if (endDate != null)
            	query1.setParameter("endtime", endDate);

            Long num = (Long) query1.getSingleResult();
            result[0] = (num%maxResult == 0)?(num/maxResult):(num/maxResult+1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
        try {
        	sql1.append("order by cr.dotime asc ");
        	sqlHead2.append(sql1);
            Query query2 =  em.createQuery(sqlHead2.toString());
            query2.setParameter("userlist", userAccount);
            if(startDate != null)
            	query2.setParameter("starttime", startDate);

            if (endDate != null)
            	query2.setParameter("endtime", endDate);
            
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

}

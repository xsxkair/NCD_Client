package com.xsx.ncd.repository;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class CardRepositoryImpl implements MyCardDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Object[] QueryCardList(String item, String pihao, java.sql.Date upDate, String maker, java.sql.Date manageDate, String manager,
			String mstatus, int firstResultIndex, int maxResult) {
		// TODO Auto-generated method stub
		Object[] reportObject = new Object[2];
		
		StringBuffer sqlHead1 = new StringBuffer("select count(c.id) FROM Card c ");
		StringBuffer sqlHead2 = new StringBuffer("SELECT c.id, c.item, c.cid, c.uptime, c.maker, c.managetime, c.manager, c.mstatus FROM Card c ");
		StringBuffer sql =  new StringBuffer();
		
		//定义SQL 
		if((item != null)&&(item.length() > 0))
			sql.append("AND c.item like :item  ");
	        
		if((pihao != null)&&(pihao.length() > 0))
			sql.append("AND c.cid like :pihao  ");
	        
		if(upDate != null)
			sql.append("AND DATE(c.uptime)=:upDate ");
	        
		if((maker != null)&&(maker.length() > 0))
			sql.append("AND c.maker like :maker ");
	        
		if(manageDate != null)
			sql.append("AND DATE(c.managetime)=:manageDate ");
	        
		if((manager != null)&&(manager.length() > 0))
			sql.append("AND c.manager like :manager ");
	        
		if(mstatus != null)
			sql.append("AND c.mstatus=:mstatus ");
		
		if(sql.indexOf("AND") >= 0)
			sql.replace(sql.indexOf("AND"), sql.indexOf("AND")+3, "WHERE");
        
        
        //查询总数
        try {
        	sqlHead1.append(sql);

            Query query1 = em.createQuery(sqlHead1.toString());
            
            if((item != null)&&(item.length() > 0))
            	query1.setParameter("item", "%"+item+"%");
            
            if((pihao != null)&&(pihao.length() > 0))
            	query1.setParameter("pihao", "%"+pihao+"%");
            
            if(upDate != null)
            	query1.setParameter("upDate", upDate);
            
            if((maker != null)&&(maker.length() > 0))
            	query1.setParameter("maker", "%"+maker+"%");
            
            if(manageDate != null)
            	query1.setParameter("manageDate", manageDate);
            
            if((manager != null)&&(manager.length() > 0))
            	query1.setParameter("manager", "%"+manager+"%");
            
            if(mstatus != null)
            	query1.setParameter("mstatus", mstatus);
            
            Long num = (Long) query1.getSingleResult();
            reportObject[0] = (num%maxResult == 0)?(num/maxResult):(num/maxResult+1);

            sqlHead2.append(sql);

            Query query2 =  em.createQuery(sqlHead2.toString());

            if((item != null)&&(item.length() > 0))
            	query2.setParameter("item", "%"+item+"%");
            
            if((pihao != null)&&(pihao.length() > 0))
            	query2.setParameter("pihao", "%"+pihao+"%");
            
            if(upDate != null)
            	query2.setParameter("upDate", upDate);
            
            if((maker != null)&&(maker.length() > 0))
            	query2.setParameter("maker", "%"+maker+"%");
            
            if(manageDate != null)
            	query2.setParameter("manageDate", manageDate);
            
            if((manager != null)&&(manager.length() > 0))
            	query2.setParameter("manager", "%"+manager+"%");
            
            if(mstatus != null)
            	query2.setParameter("mstatus", mstatus);
            
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

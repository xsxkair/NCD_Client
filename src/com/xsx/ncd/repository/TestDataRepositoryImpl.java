package com.xsx.ncd.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.xsx.ncd.entity.Device;

public class TestDataRepositoryImpl implements MyTestDataDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Object[]> QueryReportSummyChartData(Integer year, Integer month, String groupType) {
		// TODO Auto-generated method stub
		System.out.println(em);
		
		//����SQL   
        String sql = "SELECT did FROM device";   
        //����ԭ��SQL��ѯQUERYʵ��   
        Query query =  em.createNativeQuery(sql);
        //ִ�в�ѯ�����ص��Ƕ�������(Object[])�б�,   
        //ÿһ����������������Ӧ��ʵ������   
        List objecArraytList = query.getResultList();   
        for (Object object : objecArraytList) {
        	System.out.println(object);
		}  
  
        em.close(); 
        
		return null;
	}

	@Override
	public Object[] QueryTodayReport(List<Integer> deviceids, int firstResultIndex, int maxResult) {
		// TODO Auto-generated method stub
		Object[] result = new Object[2];
		
		StringBuffer sqlHead1 = new StringBuffer("select count(td.id) FROM TestData td ");
		StringBuffer sqlHead2 = new StringBuffer("SELECT td,d,c,u FROM TestData td left join Device d on d.id=td.deviceid "
        		+ "left join Card c on c.id=td.cardid "
        		+ "left join User u on u.id=td.userid ");
		//����SQL   
        StringBuffer sql1 =  new StringBuffer("where td.result='δ���' AND td.deviceid in(:devicelist) ");

        //��ѯ����
        sqlHead1.append(sql1);
        Query query1 = em.createQuery(sqlHead1.toString());
        query1.setParameter("devicelist", deviceids);
        Long num = (Long) query1.getSingleResult();
        result[0] = (num%maxResult == 0)?(num/maxResult):(num/maxResult+1);
        
        sqlHead2.append(sql1);
        Query query2 =  em.createQuery(sqlHead2.toString());
        query2.setParameter("devicelist", deviceids);
        query2.setFirstResult(firstResultIndex);
        query2.setMaxResults(maxResult);
        
        List<Object[]> datas = query2.getResultList();
        result[1] = datas;
        
        em.close();
        
		return result;
	}

}

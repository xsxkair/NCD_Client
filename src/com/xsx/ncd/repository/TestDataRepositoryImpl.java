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
        
		/*StringBuffer hql = new StringBuffer("SELECT ");
		
		String datestr1 = null;			//����select
		String datestr2 = null;			//��������
		String datestr3 = null;			//���ڷ���
		
		if(year != null){
			if(month != null){
				datestr1 = "DATE_FORMAT(TESTTIME,'��%e��')";
				datestr2 = "DATE_FORMAT(TESTTIME,'%Y%c')='"+year+month+"'";
				datestr3 = "DATE_FORMAT(TESTTIME,'%d')";
			}
			else {
				datestr1 = "DATE_FORMAT(TESTTIME,'%c��')";
				datestr2 = "DATE_FORMAT(TESTTIME,'%Y')='"+year+"'";
				datestr3 = "DATE_FORMAT(TESTTIME,'%c')";
			}
		}
		else {
			datestr1 = "DATE_FORMAT(TESTTIME,'%Y��')";
			datestr2 = null;
			datestr3 = "DATE_FORMAT(TESTTIME,'%Y')";
		}
		
		hql.append(datestr1);
		hql.append(" ,CITEM, COUNT(CID) FROM TESTDATABEAN WHERE CITEM IN (");
		for (String string : itemlist) {
			hql.append("'"+string+"'");
			if(itemlist.size() != (itemlist.indexOf(string)+1))
				hql.append(",");
		}
		hql.append(")");
		
		hql.append(" AND DID IN (");
		for (String string : deviceidlist) {
			hql.append("'"+string+"'");
			if(deviceidlist.size() != (deviceidlist.indexOf(string)+1))
				hql.append(",");
		}
		hql.append(")");
		
		if(datestr2 != null){
			hql.append(" AND ");
			hql.append(datestr2);
		}
		
		hql.append(" GROUP BY ");
		hql.append(datestr3);
		hql.append(" ,CITEM");
		
		List<Object[]> queryresult = HibernateDao.GetInstance().querysql(hql.toString(), null);
		*/
		return null;
	}

}

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
		
		//定义SQL   
        String sql = "SELECT did FROM device";   
        //创建原生SQL查询QUERY实例   
        Query query =  em.createNativeQuery(sql);
        //执行查询，返回的是对象数组(Object[])列表,   
        //每一个对象数组存的是相应的实体属性   
        List objecArraytList = query.getResultList();   
        for (Object object : objecArraytList) {
        	System.out.println(object);
		}  
  
        em.close(); 
        
		/*StringBuffer hql = new StringBuffer("SELECT ");
		
		String datestr1 = null;			//日期select
		String datestr2 = null;			//日期条件
		String datestr3 = null;			//日期分组
		
		if(year != null){
			if(month != null){
				datestr1 = "DATE_FORMAT(TESTTIME,'第%e天')";
				datestr2 = "DATE_FORMAT(TESTTIME,'%Y%c')='"+year+month+"'";
				datestr3 = "DATE_FORMAT(TESTTIME,'%d')";
			}
			else {
				datestr1 = "DATE_FORMAT(TESTTIME,'%c月')";
				datestr2 = "DATE_FORMAT(TESTTIME,'%Y')='"+year+"'";
				datestr3 = "DATE_FORMAT(TESTTIME,'%c')";
			}
		}
		else {
			datestr1 = "DATE_FORMAT(TESTTIME,'%Y年')";
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

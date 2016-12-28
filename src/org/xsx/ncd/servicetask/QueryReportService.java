package org.xsx.ncd.servicetask;

import java.util.List;

import org.com.xsx.Dao.ReportDao;
import org.com.xsx.Define.ReportTableItem;
import org.com.xsx.Domain.TestDataBean;
import org.xsx.ncd.Data.QueryReportFilterData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class QueryReportService extends Service<Object[]>{
	
	private QueryReportFilterData s_FilterData = null;
	
	public QueryReportService() {
		// TODO Auto-generated constructor stub

	}
	
	public QueryReportService(QueryReportFilterData filterData) {
		// TODO Auto-generated constructor stub
		this.setS_FilterData(filterData);
	}
	
	@Override
	protected Task<Object[]> createTask() {
		// TODO Auto-generated method stub
		return new QueryReportTask();
	}
	
	class QueryReportTask extends Task<Object[]>{

		@Override
		protected Object[] call(){
			// TODO Auto-generated method stub
			return ReadDeviceInfoFun();
		}
		
		private Object[] ReadDeviceInfoFun(){
			ObservableList<ReportTableItem> reportTableItems = FXCollections.observableArrayList();
			
			Object[] reportdatas = ReportDao.QueryReportList(s_FilterData);
			
			if(reportdatas == null)
				return null;
			
			List<TestDataBean> reportdatalist =  (List<TestDataBean>) reportdatas[0];

			for (TestDataBean report : reportdatalist) {
				
				ReportTableItem temp = new ReportTableItem();
    			
    			temp.setTestdata(report);
    			temp.setIndex(s_FilterData.getPageindex()*s_FilterData.getPagesize()+1+reportdatalist.indexOf(report));

    			reportTableItems.add(temp);
    			
    			updateProgress(reportdatalist.indexOf(report), reportdatalist.size());
			}

			return new Object[]{reportTableItems, reportdatas[1]};
		}
	}

	public QueryReportFilterData getS_FilterData() {
		return s_FilterData;
	}

	public void setS_FilterData(QueryReportFilterData s_FilterData) {
		this.s_FilterData = s_FilterData;
	}
}
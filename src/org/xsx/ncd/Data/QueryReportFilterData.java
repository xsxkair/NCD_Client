package org.xsx.ncd.Data;

import java.sql.Date;
import java.util.List;

public class QueryReportFilterData {
	private String testitem = null;
	private java.sql.Date testtime = null;
	
	private String testername = null;
	private String deviceid = null;
	private List<String> devicelist = null;
	private String simpleid = null;
	private String reportresult = null;
	
	private boolean filterisnew = true;
	
	private int pageindex = 0;													//µ±Ç°Ò³Êý
	private int pagesize = 50;
	
	public QueryReportFilterData() {
		
	}
	
	public QueryReportFilterData(String testitem, Date testtime, String testername, String deviceid,
			List<String> devicelist, String simpleid, String reportresult, boolean filterisnew, int pageindex) {
		super();
		this.testitem = testitem;
		this.testtime = testtime;
		this.testername = testername;
		this.deviceid = deviceid;
		this.devicelist = devicelist;
		this.simpleid = simpleid;
		this.reportresult = reportresult;
		this.filterisnew = filterisnew;
		this.pageindex = pageindex;
	}
	public String getTestitem() {
		return testitem;
	}
	public void setTestitem(String testitem) {
		this.testitem = testitem;
	}
	public java.sql.Date getTesttime() {
		return testtime;
	}
	public void setTesttime(java.sql.Date testtime) {
		this.testtime = testtime;
	}
	public String getTestername() {
		return testername;
	}
	public void setTestername(String testername) {
		this.testername = testername;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public List<String> getDevicelist() {
		return devicelist;
	}

	public void setDevicelist(List<String> devicelist) {
		this.devicelist = devicelist;
	}

	public String getSimpleid() {
		return simpleid;
	}
	public void setSimpleid(String simpleid) {
		this.simpleid = simpleid;
	}
	public String getReportresult() {
		return reportresult;
	}
	public void setReportresult(String reportresult) {
		this.reportresult = reportresult;
	}
	public boolean isFilterisnew() {
		return filterisnew;
	}
	public void setFilterisnew(boolean filterisnew) {
		this.filterisnew = filterisnew;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
}

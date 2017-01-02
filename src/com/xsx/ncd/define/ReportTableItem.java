package com.xsx.ncd.define;



import com.xsx.ncd.entity.TestData;

public class ReportTableItem {
	private Integer index;
	private String testitem;
	private java.sql.Timestamp testdate;
	private String testresult;
	private String tester;
	private String deviceid;
	private String simpleid;
	private String managername;
	private String reportresult;
	
	private TestData testdata;

	public ReportTableItem(Integer index, TestData testdata) {
		this.index = index;
		this.testdata = testdata;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getTestitem() {
		return testdata.getCard().getItem();
	}

	public void setTestitem(String testitem) {
		this.testitem = testitem;
	}

	public java.sql.Timestamp getTestdate() {
		return testdata.getTesttime();
	}

	public void setTestdate(java.sql.Timestamp testdate) {
		this.testdate = testdate;
	}

	public String getTestresult() {
		return testdata.getA_v()+testdata.getCard().getDanwei();
	}

	public void setTestresult(String testresult) {
		this.testresult = testresult;
	}

	public String getTester() {
		return testdata.getT_name();
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getDeviceid() {
		return testdata.getDevice().getDid();
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getSimpleid() {
		return testdata.getSid();
	}

	public void setSimpleid(String simpleid) {
		this.simpleid = simpleid;
	}

	public String getManagername() {
		return testdata.getManager().getName();
	}

	public void setManagername(String managername) {
		this.managername = managername;
	}

	public String getReportresult() {
		return testdata.getResult();
	}

	public void setReportresult(String reportresult) {
		this.reportresult = reportresult;
	}

	public TestData getTestdata() {
		return testdata;
	}

	public void setTestdata(TestData testdata) {
		this.testdata = testdata;
	}

}

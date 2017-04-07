package com.xsx.ncd.define;



import java.sql.Timestamp;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.TestData;
import com.xsx.ncd.entity.User;

public class ReportTableItem {
	private Integer index;
	private String testitem;
	private String testdate;
	private String testresult;
	private String tester;
	private String deviceid;
	private String simpleid;
	private String reportresult;
	
	private Integer dataIndex;			//数据库中数据的主键

	public ReportTableItem(Integer index, String testitem, Timestamp testdate, Float testresult, String danwei, String tester,
			String deviceid, String simpleid, String reportresult, Integer dataIndex) {
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(testresult);
		stringBuffer.append(" ");
		stringBuffer.append(danwei);
		
		this.index = index;
		this.testitem = testitem;
		this.testdate = testdate.toString();
		this.testresult = stringBuffer.toString();
		this.tester = tester;
		this.deviceid = deviceid;
		this.simpleid = simpleid;
		this.reportresult = reportresult;
		this.dataIndex = dataIndex;
	}

	public Integer getIndex() {
		return index;
	}

	public String getTestitem() {
		return this.testitem;
	}

	public String getTestdate() {
		return testdate;
	}

	public String getTestresult() {
		return testresult;
	}

	public String getTester() {
		return this.tester;
	}

	public String getDeviceid() {
		return this.deviceid;
	}

	public String getSimpleid() {
		return this.simpleid;
	}

	public String getReportresult() {
		return this.reportresult;
	}

	public Integer getDataIndex() {
		return dataIndex;
	}

}

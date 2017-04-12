package com.xsx.ncd.define;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import com.xsx.ncd.spring.SpringFacktory;

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
	private CardConstInfo cardConstInfo = null;

	public ReportTableItem(Integer index, String testitem, Timestamp testdate, Float testresult, String danwei, String tester,
			String deviceid, String simpleid, String reportresult, Integer dataIndex, String t_re) {
		
		StringBuffer stringBuffer = new StringBuffer();
		
		if("Ok".equals(t_re)){
			
			cardConstInfo = SpringFacktory.getCardConstInfoByItem(testitem);
			
			if(cardConstInfo == null)
				stringBuffer.append("Error");
			else {
				if(testresult < cardConstInfo.getLowestresult()){
					stringBuffer.append('<');
					stringBuffer.append(cardConstInfo.getLowestresult());
				}
				else if (testresult > cardConstInfo.getHighestresult()) {
					stringBuffer.append('>');
					stringBuffer.append(cardConstInfo.getHighestresult());
				}
				else {
					stringBuffer.append(testresult);
				}
				
				stringBuffer.append(" ");
				stringBuffer.append(danwei);
			}
		}
		else{
			stringBuffer.append("Error");
		}
		
		this.index = index;
		this.testitem = testitem;
		this.testdate = testdate.toString();
		this.testresult = stringBuffer.toString();
		this.tester = tester;
		this.deviceid = deviceid;
		this.simpleid = simpleid;
		this.reportresult = reportresult;
		this.dataIndex = dataIndex;
		
		stringBuffer.setLength(0);
		stringBuffer = null;
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

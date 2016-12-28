package org.xsx.ncd.servicetask;

import org.xsx.ncd.UI.LoginScene.LoginScene;
import org.xsx.ncd.UI.MainScene.ContainerPane;
import org.xsx.ncd.UI.MainScene.CardPage.CardInOutPage;
import org.xsx.ncd.UI.MainScene.CardPage.CardRecordPage;
import org.xsx.ncd.UI.MainScene.DevicePage.DevicePage;
import org.xsx.ncd.UI.MainScene.DevicePage.DeviceDetailPage.DeviceDetailPage;
import org.xsx.ncd.UI.MainScene.Manager.MyInfoPage;
import org.xsx.ncd.UI.MainScene.Report.ReportDetailPage.ReportDetailPage;
import org.xsx.ncd.UI.MainScene.Report.ReportListPage.ReportListPage;
import org.xsx.ncd.UI.MainScene.Report.ReportOverViewPage.ReportOverViewPage;
import org.xsx.ncd.UI.MainScene.WorkSpace.WorkSpacePage;

import com.xsx.ncd.tool.SpringFacktory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SystemInitService extends Service<Boolean>{
	
	private static SystemInitService GB_SystemInitService = null;
	
	private SystemInitService() {
		// TODO Auto-generated constructor stub
	}
	
	public static SystemInitService GetInstance() {
		if(GB_SystemInitService == null)
			GB_SystemInitService = new SystemInitService();
		
		return GB_SystemInitService;
	}
	
	@Override
	protected Task<Boolean> createTask() {
		// TODO Auto-generated method stub
		return new systeminittask();
	}
	
	class systeminittask extends Task<Boolean>{

		@Override
		protected Boolean call() throws Exception {
			// TODO Auto-generated method stub
			return systeminit_fun();
		}
		
		private Boolean systeminit_fun() throws InterruptedException{
        	
			Boolean temp;
			int i=0;
			
			updateMessage("正在连接服务器，请稍候。。。"); 
    		updateProgress(1, 100);
    		for (; i < 15; i++) {
    			updateProgress(i, 100);
    			Thread.sleep(50); 
			}
    		
    		try {
    			SpringFacktory.SpringFacktoryInit();
			} catch (Exception e) {
				// TODO: handle exception
				updateMessage("服务器连接失败，请检查网络！");
	    		Thread.sleep(1000); 
	    		e.printStackTrace();
	    		return false;
			}
    		
/*    		
    		updateMessage("检查更新中。。。");
    		for (; i < 30; i++) {
    			updateProgress(i, 100);
    			Thread.sleep(100); 
			}
    		temp = SoftwareDao.CheckUpdate();
    		if(null == temp)
    			updateMessage("读取失败");
    		else if (temp) {
    			updateMessage("正在启动更新程序");
    			Thread.sleep(1000); 
    			try {
					Runtime.getRuntime().exec(".\\jre\\bin\\javaw -jar UPDATE.jar "+ SoftwareInfo.version);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			System.exit(0);
			}
    		else {
    			updateMessage("已是最新版");
			}
    		Thread.sleep(1000); 
 */   		
    		//加载UI
    		//初始化登陆界面
    		updateMessage("1");
    		LoginScene.GetInstance().UI_Init();
    		updateProgress(32, 100);
  		
    		updateMessage("2");
        	ContainerPane.GetInstance().UI_Init();
        	updateProgress(34, 100);
    		
    		updateMessage("3");
    		ReportListPage.GetInstance().UI_Init();
    		updateProgress(36, 100);
      		
    		updateMessage("4");
    		ReportDetailPage.GetInstance().UI_Init();
    		updateProgress(38, 100);
     		
    		updateMessage("5");
    		DevicePage.GetInstance().UI_Init();
    		updateProgress(40, 100);
    		
    		updateMessage("7");
    		MyInfoPage.GetInstance().UI_Init();
    		updateProgress(45, 100);
    		
    		updateMessage("8");
    		ReportOverViewPage.GetInstance().UI_Init();
    		updateProgress(45, 100);
    		
    		updateMessage("9");
    		DeviceDetailPage.GetInstance().UI_Init();
    		updateProgress(49, 100);
    		
    		updateMessage("10");
    		WorkSpacePage.GetInstance().UI_Init();
    		updateProgress(49, 100);
    		
    		updateMessage("11");
    		CardInOutPage.GetInstance().UI_Init();
    		updateProgress(53, 100);
    		
    		updateMessage("12");
    		CardRecordPage.GetInstance().UI_Init();
    		updateProgress(53, 100);
    		
    		updateProgress(100, 100);
    		Thread.sleep(1000); 

			return true;
		}
	}
}

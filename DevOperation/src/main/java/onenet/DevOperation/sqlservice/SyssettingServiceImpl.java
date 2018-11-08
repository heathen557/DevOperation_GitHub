package onenet.DevOperation.sqlservice;

import onenet.DevOperation.PropertyUtilcustom;
import onenet.DevOperation.constant.OperateModule;
import onenet.DevOperation.constant.OperateType;
import onenet.DevOperation.entity.DevPara;
import onenet.DevOperation.log.MyLog;

public class SyssettingServiceImpl implements SyssettingService{

	@Override
	@MyLog(description = "修改onenet平台系统参数", module = OperateModule.SysSettings, opType = OperateType.modify,
    primaryKeyName = "rowid", primaryKeySort = 3, primaryKeyBelongClass = DevPara.class)
	public int bg36SyssettingV1(String ipadress, String masterkey, String regcode) {
		// TODO Auto-generated method stub
		  String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bg36ipadress", ipadress);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bg36masterkey", masterkey);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bg36regcode", regcode);
		  
		return 0;
	}

	@Override
	@MyLog(description = "修改onenet平台系统参数", module = OperateModule.SysSettings, opType = OperateType.modify,
    primaryKeyName = "rowid", primaryKeySort = 3, primaryKeyBelongClass = DevPara.class)
	public int bc95SyssettingV1(String ipadress, String masterkey, String regcode) {
		// TODO Auto-generated method stub
		   String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bc95ipadress", ipadress);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bc95masterkey", masterkey);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bc95regcode", regcode);
		  
		return 0;
	}

	@Override
	@MyLog(description = "修改oc平台系统参数", module = OperateModule.SysSettings, opType = OperateType.modify,
    primaryKeyName = "rowid", primaryKeySort = 3, primaryKeyBelongClass = DevPara.class)
	public int bc95b5SyssettingV1(String callbackUrl, String appID, String secert) {
		// TODO Auto-generated method stub
		 String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		   PropertyUtilcustom.updatePro(path, "oceanparasetting.bc95b5_CALLBACK_BASE_URL", callbackUrl);
		   PropertyUtilcustom.updatePro(path, "oceanparasetting.bc95b5_APPID", appID);
		   PropertyUtilcustom.updatePro(path, "oceanparasetting.bc95b5_SECRET", secert);
		   
		return 0;
	}

}

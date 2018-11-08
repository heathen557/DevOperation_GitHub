package onenet.DevOperation.sqlservice;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import onenet.DevOperation.constant.OperateModule;
import onenet.DevOperation.constant.OperateType;
import onenet.DevOperation.dao.DnldDao;
import onenet.DevOperation.entity.DevDownload;
import onenet.DevOperation.entity.DevPara;
import onenet.DevOperation.log.MyLog;

@Service
public class AppupdatingServiceImpl implements AppupdatingService{

	 @Resource
	 DnldDao dnlddao;
	 
	@Override
	@MyLog(description = "修改下载参数", module = OperateModule.AppUpdating, opType = OperateType.modify,
    primaryKeyName = "rowid", primaryKeySort = 3, primaryKeyBelongClass = DevDownload.class)
	public String updatedownloadPara(String appdownload, String appupdatetime, String verison, Long rowid) {
		// TODO Auto-generated method stub
		dnlddao.updatedownloadPara( appdownload , appupdatetime, verison, rowid);
		
		String imei = dnlddao.findImeiByRowid(rowid);
		return imei;
	}

}

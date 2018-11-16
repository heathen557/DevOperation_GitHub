package onenet.DevOperation.sqlservice;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import onenet.DevOperation.constant.OperateModule;
import onenet.DevOperation.constant.OperateType;
import onenet.DevOperation.dao.AttrDao;
import onenet.DevOperation.dao.OrgnaDao;
import onenet.DevOperation.entity.DevAttr;
import onenet.DevOperation.entity.DevPara;
import onenet.DevOperation.entity.Orgnization;
import onenet.DevOperation.log.MyLog;

@Service
public class OrgSettingServiceImpl implements OrgSettingService {

	@Resource
	AttrDao attrdao;
	@Resource
	OrgnaDao orgdao;

	@Override
	@MyLog(description = "编辑停车位属性", module = OperateModule.OrgSetting, opType = OperateType.modify, primaryKeyName = "attrid", primaryKeySort = 7, primaryKeyBelongClass = DevAttr.class)
	public int editBerth(String operator, String protocol, String regcode, String berth, String imei, String imsi,
			String devid, Long attrid) {

		attrdao.updatedevattrbyid(operator, protocol, regcode, berth, imei, imsi, devid, attrid);
		// 编辑组织
		orgdao.updateorgbynodeid(berth, attrid);
		return 0;
	}

	@Override
	 @MyLog(description = "编辑组织机构属性", module = OperateModule.OrgSetting, opType =
	 OperateType.modify,
	 primaryKeyName = "rowid", primaryKeySort = 0, primaryKeyBelongClass =
	 Orgnization.class)
	public Orgnization editOrg(String sameorgname, String curpath, String path, String orgnewname, String nodeid,
			String remark) {

		
		
//		System.out.println("sameorgname="+sameorgname+"  curpath:"+curpath+"  path:"+path +"  orgnewname:"+orgnewname +" nodeid:"+nodeid  );
		
		orgdao.updatePathBynewpath(curpath, path, curpath);  //更新path

		
		orgdao.updateorgbynodeid(orgnewname, remark, nodeid);  //更新机构名称
		// orgdao.updateorgbynodeid( orgname, path, remark , nodeid);
		
		
		
	/**********************删除函数始终会进入**********************************/	
//		if (!sameorgname.isEmpty()) { // 重复,删除
//			
//			System.out.println("删除函数已经进来了！ sameorfname=" + sameorgname );
//			orgdao.delBynodeid(nodeid);
//		}
		
		
		Orgnization orgnization = orgdao.findorgBynodeId(nodeid);
		
		System.out.println("查找的组织结构体： " + orgnization);
		
		return orgnization;
	}

	@Override
	@MyLog(description = "增加停车位", module = OperateModule.OrgSetting, opType = OperateType.create, primaryKeyName = "attrid", primaryKeySort = 0, primaryKeyBelongClass = DevAttr.class)
	public DevAttr addBerth(String berthl, String operator, String protocol, String imeil, String devicetype,
			String imsil, String regcode, String nextnodeid, String path) {
		// TODO Auto-generated method stub
		DevAttr devAttr = new DevAttr(null, null, berthl, operator, protocol, imeil, devicetype, imsil, regcode,
				nextnodeid);
		orgdao.SaveOrg(nextnodeid, berthl, path, "");
		attrdao.save(devAttr);

		return devAttr;

	}

	@Override
	@MyLog(description = "增加组织机构", module = OperateModule.OrgSetting, opType = OperateType.create, primaryKeyName = "rowid", primaryKeySort = 0, primaryKeyBelongClass = Orgnization.class)
	public Orgnization addOrg(String lastpath, Integer level, String nodename, String orgid, String remark) {
		// TODO Auto-generated method stub
		String nextnodeid = orgdao.findNextNodeid(); // 找到最大节点号 之后加一

		// String lastpath = orgdao.findPathByNodeid(lastnodeid);
		String path = lastpath + "/" + nextnodeid;

		Orgnization orgnization = new Orgnization(nextnodeid, nodename, path, remark);

		orgdao.save(orgnization);
		return orgnization;

	}

	@Override
	@MyLog(description = "删除停车位", module = OperateModule.OrgSetting, opType = OperateType.delete, primaryKeyName = "attrid", primaryKeySort = 0, primaryKeyBelongClass = DevAttr.class)
	public DevAttr delBerth(Long attrid) {
		// TODO Auto-generated method stub
		DevAttr attr = attrdao.findImeiByAttrid(attrid);
		attrdao.deleteById(attrid);
//		orgdao.deleteNodeByNodeid(attrid);  	// 删除节点  11-16
		return attr;
	}

	@Override
	@MyLog(description = "删除组织", module = OperateModule.OrgSetting, opType = OperateType.delete, primaryKeyName = "rowid", primaryKeySort = 0, primaryKeyBelongClass = Orgnization.class)
	public Orgnization delOrg(String nodeid) {
		// TODO Auto-generated method stub
		Orgnization orgnization = orgdao.findorgBynodeId(nodeid);
		orgdao.delBynodeid(nodeid);
		return orgnization;
	}

}

package onenet.DevOperation.sqlservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.PropertyUtilcustom;
import onenet.DevOperation.constant.Constant;
import onenet.DevOperation.constant.OperateModule;
import onenet.DevOperation.constant.OperateType;
import onenet.DevOperation.dao.AttrDao;
import onenet.DevOperation.dao.OrgnaDao;
import onenet.DevOperation.dao.ParaDao;
import onenet.DevOperation.entity.DevAttr;
import onenet.DevOperation.entity.DevPara;
import onenet.DevOperation.exception.OnenetApiException;
import onenet.DevOperation.http.methods.SendCmdsApi;
import onenet.DevOperation.http.response.BasicResponse;
import onenet.DevOperation.http.response.NewCmdsResponse;
import onenet.DevOperation.http.response.NewNbCmdsResponse;
import onenet.DevOperation.log.MyLog;
import onenet.DevOperation.protocols.ParaProtocol;
import onenet.DevOperation.service.AsyncTask;
import onenet.DevOperation.service.GetAccessToken;

@Slf4j
@Service
public class ParaSettingServiceImpl implements ParaSettingService{
	
	  @Resource
	  ParaDao paradao;
	  
	  @Resource
	  OrgnaDao orgdao;
	  
	  @Resource
	  AttrDao attrdao;
	  
	  @Resource
	    ParaProtocol paraprotocol;
	  
	@Override
	public DevPara findByRowid(Long rowid) {
		
		// TODO Auto-generated method stub
		DevPara devParas= paradao.findByRowid(rowid);
		return devParas;
	}

	@Override
	public String findSome(String imei, int page, int pageSize) {
		
		// TODO Auto-generated method stub
		 List<DevPara> pages = paradao.findSome(imei,(page-1)*pageSize,pageSize);
    	 Integer total = 1;
    	 JSONObject result = new JSONObject();  
         result.put("rows",pages);  
         result.put("total",total);  
         System.out.println(result.toJSONString()); 
         return result.toJSONString();  
	}

	@Override
	public String findDefault(String nodeid, int page, int pageSize) {
		
		// TODO Auto-generated method stub
		Integer nodeidInt = orgdao.findTccByNodeid(nodeid); ///找到tcc
		System.out.println(Integer.toString(nodeidInt));
		 List<DevPara> pages = paradao.findDefault(Integer.toString(nodeidInt),(page-1)*pageSize,pageSize);
	    	Integer total = paradao.totaloffindAll(Integer.toString(nodeidInt));
	    	 JSONObject result = new JSONObject();  
	         result.put("rows",pages);  
	         result.put("total",total);  
	         System.out.println(result.toJSONString()); 
	         return result.toJSONString();  
		
	}

	@Override//更新
	@MyLog(description = "修改设备参数", module = OperateModule.DevparaSetting, opType = OperateType.modify,
    primaryKeyName = "rowid", primaryKeySort = 3, primaryKeyBelongClass = DevPara.class)
	public String updatePara(String deltathup,String deltathdown,String updatinitval,Long attrid) {
		// TODO Auto-generated method stub
		paradao.updatePara(deltathup,deltathdown,updatinitval,attrid);
		String imei = paradao.findImeiByRowid(attrid);
		return imei;
	}

	@Override//更新
	@MyLog(description = "加载设备参数", module = OperateModule.DevparaSetting, opType = OperateType.load,
    primaryKeyName = "rowid", primaryKeySort = 0, primaryKeyBelongClass = DevPara.class)
	public String loadPara(String ids) {
		// TODO Auto-generated method stub
		String[]  strs=ids.split(",");
   	 String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
   	List<String> imeiList=new ArrayList<>();
   	 try {
   		for(int i=0,len=strs.length;i<len;i++){
      		 long rowid = Long.parseLong(strs[i]);
      		
      	
      		 DevPara paralist =  paradao.findByRowid(rowid);
      		 DevAttr attrlist =  attrdao.findByAttrid(rowid);
      		
      		 //协议封装、缓存
      	
      			 
      			//DevPara paraCmd = paralist;  
      			String devid = attrlist.getDevid();
      			String devicetype = attrlist.getDevicetype();
      			String cmd = paraprotocol.getContentData(paralist);
      			log.info("cmd:" + cmd);   		
      			String devId = devid;

      			
      			String imei =attrdao.findImei(devid);
      			imeiList.add(imei);
      			Integer inst_id = 2;
      			SendCmdsApi api = null;
      			if(devicetype.equals("bc95")) {
      				//log.info("enter into sendcmdapi!");
      				
      		 		api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, cmd, 
      		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bc95masterkey"),
      		 				devicetype,imei,inst_id);
      		 		log.info("sendcmdsapi end!" );
      		 	
          			BasicResponse<NewNbCmdsResponse> response = api.executeNbApi();
          			
          			NewNbCmdsResponse cmdresponse = response.getData();
      				
      				String cmduuid = cmdresponse.getuuid();
      			}
      			if(devicetype.equals("bg36")) {
      		 		api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, cmd, 
      		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bg36masterkey"),
      		 				devicetype,imei,null);
      		 		log.info("out of sendcmdsapi !" );
          			BasicResponse<NewCmdsResponse> response = api.executeApi();
          			log.info("out of response !" );
      				NewCmdsResponse cmdresponse = response.getData();
      			}
      			if(devicetype.equals("bc95b5")) {
         				String serviceId = "test";
         				String method = "downstream";
         				
         				api = new SendCmdsApi(devid, serviceId, method, cmd, GetAccessToken.accessToken);
         				
         				api.executeOcNbApi();
         			}
      					
   				//String cmduuid = cmdresponse.getCmduuid();
   				//log.info("bg36 response is :" + response.getJson());
      		 
      	 }
   	}catch(OnenetApiException e) {
   		log.info("抛出自定义异常！");
   		return "-1";
   	}
   	return imeiList.toString();
   
	}

	@Override
	public Map<String, Object> findParaAndAttr(Long rowid) {
		// TODO Auto-generated method stub
		 DevPara paralist =  paradao.findByRowid(rowid);
		 DevAttr attrlist =  attrdao.findByAttrid(rowid);
		 
		 Map<String , Object> map = new HashMap<>();
		 map.put("paralist", paralist);
		 map.put("attrlist", attrlist);
		return map;
	}

	
}

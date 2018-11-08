package onenet.DevOperation.service;


import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.PropertyUtilcustom;
import onenet.DevOperation.dao.AttrDao;
import onenet.DevOperation.entity.DevAttr;
import onenet.DevOperation.entity.FileDataPackage;
import onenet.DevOperation.http.methods.RegisterDeviceApi;

//import com.alibaba.fastjson.JSONObject;
//
//
//import lombok.extern.slf4j.Slf4j;

import onenet.DevOperation.http.methods.SendCmdsApi;
import onenet.DevOperation.http.response.BasicResponse;
import onenet.DevOperation.http.response.NewCmdsResponse;
import onenet.DevOperation.http.response.RegDeviceResponse;
import onenet.DevOperation.protocols.ProtocolsFactory;
import onenet.DevOperation.protocols.RTCProtocol;
import onenet.DevOperation.protocols.UploadProtocol;
import onenet.DevOperation.utils.ByteConvertUtils;
import onenet.DevOperation.constant.*;
import onenet.DevOperation.utils.FileOperationUtil;


@Slf4j
@Component  
public class AsyncTask {  
	
    @Resource
    AttrDao attrdao;  
	
	@Async("asyncServiceExecutor") 
    public void SendRTC(String devid,String devicetype) {
		if(devid == null) {
			return;
		}
		log.info("devicetype :"+ devicetype +" ,starting rtc timing!");
		String imei =null;
		Integer inst_id = null;
		String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		if(devicetype.equals("bc95")) {
			//访问数据库
			//DevAttr devattr = new DevAttr();
			//log.info("starting bc95 rtc timing!");
			imei = attrdao.findImei(devid);
		
			log.info("imei:"+imei +",starting bc95 rtc timing!");
			inst_id = 3;
			 RTCProtocol rtcprotocol = (RTCProtocol)ProtocolsFactory.getProtocol(RTCProtocol.class);
			    String rtccmd = rtcprotocol.getContentData(null);
		 		SendCmdsApi api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, rtccmd, 
		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bc95masterkey"),
		 				devicetype,imei,inst_id);
				BasicResponse<NewCmdsResponse> response = api.executeApi();
				NewCmdsResponse cmdresponse = response.getData();
			
		}
		if(devicetype.equals("bg36")) {
			log.info("starting bg36 rtc timing!");
		    RTCProtocol rtcprotocol = (RTCProtocol)ProtocolsFactory.getProtocol(RTCProtocol.class);
		    String rtccmd = rtcprotocol.getContentData(null);
	 		SendCmdsApi api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, rtccmd,
	 				PropertyUtilcustom.getPro(path, "onenetparasetting.bg36masterkey"),
	 				devicetype,imei,inst_id);
			BasicResponse<NewCmdsResponse> response = api.executeApi();
			NewCmdsResponse cmdresponse = response.getData();
		}


		if(devicetype.equals("bc95b5")) {
   				String serviceId = "test";
   				String method = "downstream";
   			    RTCProtocol rtcprotocol = (RTCProtocol)ProtocolsFactory.getProtocol(RTCProtocol.class);
   			    String rtccmd = rtcprotocol.getContentData(null);
   				SendCmdsApi	api = new SendCmdsApi(devid, serviceId, method, rtccmd, GetAccessToken.accessToken);
   				BasicResponse<NewCmdsResponse> response = api.executeApi();
   				NewCmdsResponse cmdresponse = response.getData();
   		}
		
		//log.info("rtc响应结果的 uuid为："+ cmdresponse.getCmduuid());
    }
	
	@Async("asyncServiceExecutor") 
	 public void UploadFile(String devid,FileDataPackage filedatapackage,String devicetype) throws IOException { 
		
		String imei =null;
		Integer inst_id = null;
		String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		if(devicetype.equals("bc95")) {
			//访问数据库
			//DevAttr devattr = new DevAttr();
			imei = attrdao.findImei(devid);
			
			inst_id = 11;
			UploadProtocol uploadprotocol = (UploadProtocol)ProtocolsFactory.getProtocol(UploadProtocol.class);		
			String rtccmd = uploadprotocol.getContentData(filedatapackage);
		 		SendCmdsApi api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, rtccmd, 
		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bc95masterkey"),
		 				devicetype,imei,inst_id);
		 		BasicResponse<NewCmdsResponse> response = api.executeApi();
				NewCmdsResponse cmdresponse = response.getData();
			
		}
		if(devicetype.equals("bg36")) {
			UploadProtocol uploadprotocol = (UploadProtocol)ProtocolsFactory.getProtocol(UploadProtocol.class);		
			String rtccmd = uploadprotocol.getContentData(filedatapackage);
	 		SendCmdsApi api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, rtccmd,
	 				PropertyUtilcustom.getPro(path, "onenetparasetting.bg36masterkey"),
	 				devicetype,imei,inst_id);
	 		BasicResponse<NewCmdsResponse> response = api.executeApi();
			NewCmdsResponse cmdresponse = response.getData();
		}
		if(devicetype.equals("bc95b5")) {
				String serviceId = "test";
				String method = "downstream";
				UploadProtocol uploadprotocol = (UploadProtocol)ProtocolsFactory.getProtocol(UploadProtocol.class);		
				String rtccmd = uploadprotocol.getContentData(filedatapackage);
				SendCmdsApi	api = new SendCmdsApi(devid, serviceId, method, rtccmd, GetAccessToken.accessToken);
				BasicResponse<NewCmdsResponse> response = api.executeApi();
				NewCmdsResponse cmdresponse = response.getData();
		}
	
		
	 }
	
	@Async("asyncServiceExecutor") 
	public void RegDevices(String sn,String devicetype,String imsi) {
		RegisterDeviceApi api = null;
		String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		//String authinfo = null;
		
    	if(devicetype.equals("bg36")) { 
    		api = new RegisterDeviceApi( PropertyUtilcustom.getPro(path, "onenetparasetting.bg36regcode"), 
    				null, sn, null, 
    				PropertyUtilcustom.getPro(path, "onenetparasetting.bg36masterkey"),devicetype,null);	
    	}
    	if(devicetype.equals("bc95")) {
    		//authinfo = "{\""+sn+"\":\""+imsi+"\"}";
    		JSONObject auth = new JSONObject();
    		auth.put(sn, imsi);
    		api = new RegisterDeviceApi(PropertyUtilcustom.getPro(path, "onenetparasetting.bc95regcode")
    				, null, sn, null,
    				PropertyUtilcustom.getPro(path, "onenetparasetting.bc95masterkey"),devicetype,auth);
    	}
    	if(devicetype.equals("bc95b5")) {
    		api = new RegisterDeviceApi( PropertyUtilcustom.getPro(path, "oceanparasetting.bc95b5_APPID"), GetAccessToken.accessToken,  sn);
    	}
    	BasicResponse<RegDeviceResponse> response = api.executeApi();
    	log.info("errno:"+response.errno+" error:"+response.error);
		log.info(response.getData().key);
		
		log.info("imei:"+sn);
		if(response.errno == 0) {
			//设备号存入数据库
			attrdao.updatedevidbyimei( response.getData().devid, sn);
			
			 //return "{\"msg\":\"register device seccess!\"}"; 
			try {
				WebSocketServer.sendInfo("{\"regmsg\":\"0\"}");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				WebSocketServer.sendInfo("{\"regmsg\":\"1\"}");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

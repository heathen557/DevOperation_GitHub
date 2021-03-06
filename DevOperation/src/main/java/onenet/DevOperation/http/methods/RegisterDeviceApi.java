package onenet.DevOperation.http.methods;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import onenet.DevOperation.exception.OnenetApiException;
import onenet.DevOperation.http.AbstractAPI;
import onenet.DevOperation.http.methods.RequestInfo.Method;
import onenet.DevOperation.http.response.BasicResponse;
import onenet.DevOperation.http.response.RegDeviceResponse;
import onenet.DevOperation.utils.Config;
import onenet.DevOperation.constant.*;
import onenet.DevOperation.utils.JsonUtil;



public class RegisterDeviceApi extends AbstractAPI{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String code;
	private HttpPostMethod HttpMethod;
	private String sn;
	private String mac;
	private String title;
	
	/**
	 * 
	 * @param code：设备注册码（必填）,String
	 * @param mac：设备唯一mac标识，最长32字符
	 * @param sn：设备唯一标识String类型，最长512字符
	 * @param title:设备名（可选） 最长32个字符
	 * @param key:设备注册码（必填）
	 */
	public RegisterDeviceApi(String code,String mac, String sn, String title,String key,String devtype,Object authinfo) {
		this.code = code;
		this.key = mac;
		this.sn=sn;
		this.title=title;
		this.key=key;
		this.mac=mac;
		this.method = Method.POST;
		
        Map<String, Object> headmap = new HashMap<String, Object>();
        Map<String, Object> urlmap = new HashMap<String, Object>();
        HttpMethod=  new HttpPostMethod(method);
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        
        Map<String, Object> bodymap = new HashMap<String, Object>();
        if(devtype.equals("bg36")){
        	url=Config.getString("test.url")+"/register_de";
        	
	        if(sn!=null){
	            bodymap.put("sn", sn);
	        }
	        if(mac!=null){
	            bodymap.put("mac", mac);
	        }
	        if(title!=null){
	            bodymap.put("title", title);
	        }
	        
	        String json=null;
	        ObjectMapper remapper = new ObjectMapper();
	        try {
	            json = remapper.writeValueAsString(bodymap);
	        } catch (Exception e) {
	            logger.error("json error:{}", e.getMessage());
	            throw new OnenetApiException(e.getMessage());
	        }
	        if(code!=null){
	            urlmap.put("register_code", code);
	        }
	        ((HttpPostMethod)HttpMethod).setEntity(json);
	        HttpMethod.setcompleteUrl(url,urlmap);
        }else if(devtype.equals("bc95")){
        	url=Config.getString("test.url")+"/devices";
        	
	        if(sn!=null){
	            bodymap.put("title", sn);
	        }
	        if(authinfo!=null){
	            bodymap.put("auth_info", authinfo);
	        }
	        
	        String json=null;
	        ObjectMapper remapper = new ObjectMapper();
	        try {
	            json = remapper.writeValueAsString(bodymap);
	        } catch (Exception e) {
	            logger.error("json error:{}", e.getMessage());
	            throw new OnenetApiException(e.getMessage());
	        }
	      
	            urlmap = null;
	        
	        ((HttpPostMethod)HttpMethod).setEntity(json);
	        HttpMethod.setcompleteUrl(url,urlmap);
        }
        
	}
	//电信平台注册
	public RegisterDeviceApi(String appKey,String accessToken, String imei) {
		String urlPostAsynCmd = Constant.REGISTER_DEVICE;
		 
		method = Method.POST;
        Map<String, Object> headmap = new HashMap<String, Object>();
        //Map<String, Object> urlmap = new HashMap<String, Object>(); 
        HttpMethod=  new HttpPostMethod(method);
        headmap.put(Constant.HEADER_APP_KEY, appKey);
        headmap.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        //headmap.put("Content-Type", "application/json");
        HttpMethod.setHeader(headmap);
        //url=Config.getString("test.url")+"/register_de";

        Map<String, Object> bodymap = new HashMap<String, Object>();
        if(imei!=null){
            bodymap.put("nodeId", imei);
        }
       
        String jsonRequest = JsonUtil.jsonObj2Sting(bodymap);
      
        ((HttpPostMethod)HttpMethod).setEntity(jsonRequest);
        HttpMethod.setcompleteUrl(urlPostAsynCmd,null);
	}

	public BasicResponse<RegDeviceResponse> executeApi(){
		BasicResponse response=null;
		HttpResponse httpResponse=HttpMethod.execute();
		try {
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), RegDeviceResponse.class);
			response.setData(newData);
			 
		} catch (Exception e) {
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}
		try{
			HttpMethod.httpClient.close();
		}catch (Exception e) {
			logger.error("http close error: {}" , e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}
		return response;
	}
}

package onenet.DevOperation.http.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import onenet.DevOperation.PropertyUtilcustom;
import onenet.DevOperation.exception.OnenetApiException;
import onenet.DevOperation.http.AbstractAPI;
import onenet.DevOperation.http.methods.RequestInfo.Method;
import onenet.DevOperation.http.response.BasicResponse;
import onenet.DevOperation.http.response.NewCmdsResponse;
import onenet.DevOperation.http.response.NewNbCmdsResponse;
import onenet.DevOperation.utils.Config;
import onenet.DevOperation.constant.*;
import onenet.DevOperation.utils.JsonUtil;
import onenet.DevOperation.utils.PropertyUtil;




public class SendCmdsApi  extends AbstractAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String devId;
	private Integer qos;
	private Integer timeOut;
	private Integer type;
	private Object contents;//用户自定义数据
	private String devicetype;
	private String imei;
	private Integer inst_id;
	
	private HttpPostMethod HttpMethod;
	/**
	 * @param devId：接收该数据的设备ID（必选），String
	 * @param qos:是否需要响应，默认为0,Integer
	 * 0：不需要响应，即最多发送一次，不关心设备是否响应；
	 * 1：需要响应，如果设备收到命令后没有响应，则会在下一次设备登陆时若命令在有效期内(有效期定义参见timeout参数）则会继续发送。
	 * 对响应时间无限制，多次响应以最后一次为准。
	 * 本参数仅当type=0时有效；
	 * @param timeOut:命令有效时间，默认0,Integer
	 * 0：在线命令，若设备在线,下发给设备，若设备离线，直接丢弃；
	 *  >0： 离线命令，若设备在线，下发给设备，若设备离线，在当前时间加timeout时间内为有效期，有效期内，若设备上线，则下发给设备。单位：秒，有效围：0~2678400。
	 *  本参数仅当type=0时有效；
	 * @param type://默认0。0：发送CMD_REQ包，1：发送PUSH_DATA包
	 * @param contents:用户自定义数据：json、string、二进制数据（小于64K）
	 * @param key:masterkey或者设备apikey
	 */
	public SendCmdsApi(String devId, Integer qos, Integer timeOut, Integer type, Object contents,String key,String devicetype,String imei,Integer inst_id) {
		this.devId = devId;
		this.qos = qos;
		this.timeOut = timeOut;
		this.type = type;
		this.contents=contents;
		this.key = key;
		this.method = Method.POST;
		this.devicetype = devicetype;
		this.imei = imei;
		this.inst_id = inst_id;
		
        Map<String, Object> headmap = new HashMap<String, Object>();
        Map<String, Object> urlmap = new HashMap<String, Object>();
        HttpMethod=  new HttpPostMethod(method);
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        
        String path = PropertyUtil.class.getResource("/syssettings.properties").getPath();
        
        if(devicetype.equals("bg36")) {
	        this.url=PropertyUtil.getPro(path, "test.url")+"/cmds";
	        if(devId!=null){
	            urlmap.put("device_id", devId);
	        }
	        if(qos!=null){
	            urlmap.put("qos", qos);
	        }
	        if(timeOut!=null){
	            urlmap.put("timeout", timeOut);
	        }
	        if(type!=null){
	            urlmap.put("type", type);
	        }
	        //body参数处理
	        if(contents instanceof byte[]){
	        	((HttpPostMethod)HttpMethod).setEntity((byte[])contents);
	        }
	        if(contents instanceof String){
	            ((HttpPostMethod)HttpMethod).setEntity((String)contents);
	        }
	        HttpMethod.setcompleteUrl(url,urlmap);
        }
        if(devicetype.equals("bc95")) {
        	 JSONObject jsonObject  = new JSONObject();
        	if(inst_id == 2||inst_id == 6) { //离线
        		this.url=PropertyUtil.getPro(path, "test.url")+"/nbiot/execute/offline";
        		//DateFormat df = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        		String valid_time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis() + 3 * 60 * 1000));
        		String expired_time =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000));
        		//long expired_time = (long)((System.currentTimeMillis() + 8 * 60 * 60 * 1000)/1000);
        		 urlmap.put("valid_time", valid_time);
        		 urlmap.put("expired_time", expired_time);       		
        		 urlmap.put("retry", 3);
        		
        		 
        	}else  {
            	this.url=PropertyUtil.getPro(path, "test.url")+"/nbiot/execute";
            
            	}
        	
        	jsonObject.put("args", contents);
   		 String cmd = jsonObject.toJSONString();
   		 if(imei!=null){
   	            urlmap.put("imei", imei);
   	        }
   	        
   	            urlmap.put("obj_id", 3311);  	
   	            if(inst_id!=null) {
   	            urlmap.put("obj_inst_id", inst_id);
   	            }
   	            urlmap.put("res_id", 5706);
   	       
   	         if(contents instanceof String){
   	             ((HttpPostMethod)HttpMethod).setEntity((String)cmd);
   	         }
   	         HttpMethod.setcompleteUrl(url,urlmap);
        }
     
	}

	public SendCmdsApi(String deviceId,String serviceId,String method, String contents,String accessToken) {
		//Please make sure that the following parameter values have been modified in the Constant file.
        String urlPostAsynCmd = Constant.POST_ASYN_CMD;
        String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
        String appId = PropertyUtilcustom.getPro(path, "oceanparasetting.bc95b5_APPID");
       
        //please replace the deviceId, when you use the demo.
        
        //String deviceId = "8c23b6b4-ea68-48fb-9c2f-90452a81ebb1";
        String callbackUrl = Constant.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;

        //please replace the following parameter values, when you use the demo.
        //And those parameter values must be consistent with the content of profile that have been preset to IoT platform.
        //The following parameter values of this demo are use the watermeter profile that already initialized to IoT platform.
        
        //String serviceId = "WaterMeter";
        //String method = "SET_TEMPERATURE_READ_PERIOD";
        //String paras = "{\"message\":\"" + contents + "\"}";
       
        ObjectNode paras = null;
		try {
			paras = JsonUtil.convertObject2ObjectNode("{\"message\":\"" + contents + "\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //ObjectNode paras = null;
		
      
        Map<String, Object> paramCommand = new HashMap<>();
        paramCommand.put("serviceId", serviceId);
        paramCommand.put("method", method);
        paramCommand.put("paras", (ObjectNode)paras);      
        
        Map<String, Object> paramPostAsynCmd = new HashMap<>();
        paramPostAsynCmd.put("deviceId", deviceId);
        paramPostAsynCmd.put("command", paramCommand);
        paramPostAsynCmd.put("callbackUrl", callbackUrl);
        
        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostAsynCmd);
   
        
        //HttpResponse responsePostAsynCmd = httpsUtil.doPostJson(urlPostAsynCmd, header, jsonRequest);

        
        Map<String, Object> headmap = new HashMap<String, Object>();
        //Map<String, Object> urlmap = new HashMap<String, Object>();
        HttpMethod=  new HttpPostMethod( this.method);
        headmap.put(Constant.HEADER_APP_KEY, appId);
        headmap.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        HttpMethod.setHeader(headmap); 
       
        //body参数处理

        if(jsonRequest instanceof String) {
        	 ((HttpPostMethod)HttpMethod).setEntity((String)jsonRequest);
        }
        HttpMethod.setcompleteUrl(urlPostAsynCmd,null);
	}
	
	public BasicResponse<NewCmdsResponse> executeApi() {
		BasicResponse response=null;
		HttpResponse httpResponse=HttpMethod.execute();
	
		try {
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), NewNbCmdsResponse.class);
			response.setData(newData);

		}catch(Exception e) {
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}

		
			try {
				HttpMethod.httpClient.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("http close error: {}" , e.getMessage());
				throw new OnenetApiException(e.getMessage());
			}
		return response;
	}
	public BasicResponse<NewNbCmdsResponse> executeNbApi(){
		BasicResponse response=null;
		HttpResponse httpResponse=HttpMethod.execute();
		try {
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), NewNbCmdsResponse.class);
			response.setData(newData);

		}catch(Exception e) {
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}

			try {
				HttpMethod.httpClient.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("http close error: {}" , e.getMessage());
				throw new OnenetApiException(e.getMessage());
			}
		
		
		
		return response;
	}
	
	public void executeOcNbApi(){
		HttpResponse httpResponse=HttpMethod.execute();
		
			try {
				HttpMethod.httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			// TODO Auto-generated catch block
			
	}
}

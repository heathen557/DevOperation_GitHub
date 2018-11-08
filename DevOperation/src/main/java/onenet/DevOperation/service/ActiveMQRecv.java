package onenet.DevOperation.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.dao.geoDao;
import onenet.DevOperation.entity.FileDataPackage;
import onenet.DevOperation.utils.ByteConvertUtils;
import onenet.DevOperation.constant.*;
import onenet.DevOperation.utils.FileOperationUtil;

@Slf4j
@Component
public class ActiveMQRecv {
	
//	@Resource
//	geoDao geodao;
//	
//	   @Autowired  
//	    private AsyncTask asyncTask; 
//	   
//	 @JmsListener(destination = "topic.baseinfo")
//	    public void receiveTopic(String message) throws IOException {
//	        log.info("监听topic=============监听topic");
//	       log.info(message);
//	       try {
//				 JSONObject jsonobject = JSONObject.parseObject(message);
//				 
//				   if(jsonobject.containsKey("rtc")) {//RTC校时
//			        	//String rtcStr = jsonobject.getString("rtc");
//			        	String devid = jsonobject.getString("dev_id");
//			        	 asyncTask.SendRTC(devid);
//			        	 log.info("sendRTC :" + message);
//			        	 
//			        }else if(jsonobject.containsKey("battery")) {//电量警告(上传到运营平台、存入数据库log表)
//			        	//放到数据库中
//			        	log.info("power warning :" + message);
//			        	 
//			        }else if(jsonobject.containsKey("Para_cmd")) {//参数回表
//			        	log.info("para_response :" + message);
//			        	 
//			        }else if(jsonobject.containsKey("operation_type")) {//下载回表
//			        	if(jsonobject.getString("operation_type").equals("01")) {//准备下载
//			        		FileDataPackage filepackage = new FileDataPackage();
//			        		
//			        		byte[] fileContent = FileOperationUtil.getContent(Constant.UplaodFilePath,0);
//			        		String filecontent = ByteConvertUtils.bytesToHexFun1(fileContent);
//							filepackage.setAppversion("00000000");
//			        		filepackage.setCurrentpackageindex(0);
//			        		filepackage.setMaxpackageindex(FileOperationUtil.MaxpackageIndex);
//			        		filepackage.setFilecontent(filecontent);
//			        		
//			        		asyncTask.UploadFile(jsonobject.getString("dev_id"),filepackage); //发送第0个包
//			        		
//			        	}else if(jsonobject.getString("operation_type").equals("02")) {//下载完成
//			        		 
//			        	}else if(jsonobject.getString("operation_type").equals("03")) {//更新完成
//			        		
//			        	}
//			        	log.info("download_response :" + message);
//			        	 
//			        }else if(jsonobject.containsKey("currentPackage")) {//分包回表
//			        	int currentPackage = Integer.valueOf(jsonobject.getString("currentPackage"));
//			        	FileDataPackage filepackage = new FileDataPackage();
//			        	
//			        	byte[] fileContent = FileOperationUtil.getContent(Constant.UplaodFilePath,currentPackage);
//		        		String filecontent = ByteConvertUtils.bytesToHexFun1(fileContent);
//		        		filepackage.setAppversion("00000000");
//		        		filepackage.setCurrentpackageindex(0);
//		        		filepackage.setMaxpackageindex(FileOperationUtil.MaxpackageIndex);
//		        		filepackage.setFilecontent(filecontent);
//			        	asyncTask.UploadFile(jsonobject.getString("dev_id"),filepackage); //发送第0个包
//			        }
//			   } catch (Exception e) {
//				   log.info("test content is :" + message);
//				   
//			  }
//	       		WebSocketServer.sendInfo(message); 
//			
//
//	    }
	
		
}

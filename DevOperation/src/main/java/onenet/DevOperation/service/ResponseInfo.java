package onenet.DevOperation.service;


import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.dao.DnldDao;
import onenet.DevOperation.dao.ParaDao;
import onenet.DevOperation.entity.DevDownload;
import onenet.DevOperation.entity.FileDataPackage;
import onenet.DevOperation.utils.ByteConvertUtils;
import onenet.DevOperation.constant.*;
import onenet.DevOperation.utils.FileOperationUtil;

@Slf4j
@Component
public class ResponseInfo {
//	@Resource
//	geoDao geodao;
//	
	   @Autowired  
	    private AsyncTask asyncTask;  
	   
	   @Resource
	    DnldDao dnldDao;
	   
	   @Resource
	    ParaDao paraDao;
	   private static Logger logger = LoggerFactory.getLogger(ResponseInfo.class); 
	   
	   @RabbitListener(queues = "topic.respinfo.#")
	 @RabbitHandler
	    public void process(String message) throws IOException {
		   
	     //log.info("监听topic=============responseinfo================监听topic");
		   if(message == null) {
			   logger.info("接收到的字符串为空！" );
			   return;
		   }
		   logger.info("====responseinfo process(String message)====:" + message);
		  
	             if(ByteConvertUtils.isJSONValid2(message)) {
	            	 JSONObject jsonobject = JSONObject.parseObject(message);
	            	 String devicetype = jsonobject.getString("devicetype");
	            	 //Constant.DEVICETYPE = devicetype;
	            	 String devid = jsonobject.getString("dev_id");
					   if(jsonobject.containsKey("rtc")) {//RTC校时
				        	//String rtcStr = jsonobject.getString("rtc");
				        	//String devid = jsonobject.getString("dev_id");
				        	
				        	 asyncTask.SendRTC(devid,devicetype);
				        	 log.info("RTC 响应数据为  :" + message);
				        	 
				        }else if(jsonobject.containsKey("battery")) {//电量警告(上传到运营平台、存入数据库log表)
				        	//放到数据库中
				        	log.info("power warning :" + message);
				        	 
				        }else if(jsonobject.containsKey("deltathup")) {//参数回表
				        	log.info("para_response :" + message);
				        	//保存到数据库
				        	paraDao.updatePara( jsonobject.getString("deltathup") , jsonobject.getString("deltathdown"), jsonobject.getString("initenvval"),  devid);
				        }else if(jsonobject.containsKey("appdownload")) {//下载参数回表
				        	log.info("downloadpara_response :" + message);
				        	//保存到数据库
				        	String appdownload = jsonobject.getString("appdownload");
				        	String updatetime = jsonobject.getString("updatetime");
				        	String temp = Integer.toString(Integer.valueOf(updatetime,16)/3600)
				        			+":"
				        			+Integer.toString((Integer.valueOf(updatetime,16)%3600)/60);
//				        	DevDownload devDownload = new DevDownload();
//				        	devDownload.setMode(jsonobject.getString("appdownload"));
//				        	devDownload.setUpdatetime(jsonobject.getString("updatetime"));
				        	dnldDao.updatednldParaByDevid(appdownload, temp, devid);
				        }else if(jsonobject.containsKey("operation_type")) {//下载回表
				        	if(jsonobject.getString("operation_type").equals("01")) {//准备下载
				        	String ver = dnldDao.findVerByDevid(devid);
				        		FileDataPackage filepackage = new FileDataPackage();
				        		
				        		log.info("下发第一个数据包！");
				        		String filecontent;
				        		String uploadfilepath = FileOperationUtil.UploadFilepath;
				        		byte [] contentbyte = FileOperationUtil.getContent(uploadfilepath,0);
				        		if(contentbyte == null ) {
				        			log.info("上传内容为空！");
				        			return;
				        		}
				        		log.info("开始上传内容");
								filecontent = ByteConvertUtils.bytesToHexFun1(contentbyte);
									
								filepackage.setAppversion(ver); 
				        		filepackage.setCurrentpackageindex(0);
				        		filepackage.setMaxpackageindex(FileOperationUtil.MaxpackageIndex);
				        		filepackage.setFilecontent(filecontent);
				        		
				        		
							asyncTask.UploadFile(jsonobject.getString("dev_id"),filepackage,devicetype);
								 //发送第0个包
				        	//保存到数据库
							dnldDao.updateStatusBydevid("5", devid);
				        	}else if(jsonobject.getString("operation_type").equals("02")) {//下载完成
				        		 log.info("文件下载完成！");
				        	dnldDao.updateStatusVerPgBydevid( "4" , jsonobject.getString("DeviceVer"),"100", devid);	 
				        	}else if(jsonobject.getString("operation_type").equals("03")) {//更新完成
				        		 log.info("文件更新完成！");
				        	dnldDao.updateStatusVerPgBydevid( "3" , jsonobject.getString("DeviceVer"),"", devid);
				        	dnldDao.updateProgressBydevid("", devid);
				        	}
				        	log.info("download_response :" + message);
				        	 
				        }else if(jsonobject.containsKey("currentPackage")) {//分包回表
				        	int currentPackage = Integer.valueOf(jsonobject.getString("currentPackage"),16);
				        	FileDataPackage filepackage = new FileDataPackage();
				        	log.info("第二包以及之后的包回表！！");
				        	String filecontent;
						
				        	String uploadfilepath = FileOperationUtil.UploadFilepath;
				        	byte[] contentbyte = FileOperationUtil.getContent(uploadfilepath,currentPackage);
				        	if(contentbyte == null) {
				        		return;
				        	}
							filecontent = ByteConvertUtils.bytesToHexFun1(contentbyte);
							filepackage.setAppversion(jsonobject.getString("DeviceVer"));
			        		filepackage.setCurrentpackageindex(currentPackage);
			        		filepackage.setMaxpackageindex(FileOperationUtil.MaxpackageIndex);
			        		filepackage.setFilecontent(filecontent);
				      
						asyncTask.UploadFile(devid,filepackage,devicetype);						
							//保存进度
							//String progress = Integer.toString(currentPackage*100/FileOperationUtil.MaxpackageIndex);
							dnldDao.updateProgressBydevid(Integer.toString(currentPackage*100/FileOperationUtil.MaxpackageIndex), devid);
				        }
	             }else {
	            	 logger.info("消息队列出错！！" );
	  			   return;
	             }
		   
				 
			   
	       		
				
						WebSocketServer.sendInfo(message);
					
				

	    }
	

}

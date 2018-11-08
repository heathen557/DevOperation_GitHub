package onenet.DevOperation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.constant.Constant;

@Slf4j
public class FileOperationUtil {
	
	public static int MaxpackageIndex ; 
	public static long fileSize;
	public static String UploadFilepath;
	public static String fileName;
	   public static byte[] getContent(String filePath,int currentpackageindex) throws IOException {
		   
	        File file = new File(filePath);
	        if(FileExists(file) == 0) {
	        	return null;
	        }
	        if(currentpackageindex == 0) { //计算数据包数
	        	MaxpackageIndex = 0;
		        if(CalMaxpackageIndex(file) == 0) {
		        	 log.info("file too big...");  
		        	 return null;
		        }
	        }
	        if(currentpackageindex + 1 > MaxpackageIndex) 
	        	return null;
	        FileInputStream fi = new FileInputStream(file);
	        fi.skip((long)(currentpackageindex*Constant.PackageSize));
	        
	        
	        if(MaxpackageIndex == (currentpackageindex + 1)) {// 最后一个数据包
	        	int restsize = (int) (fileSize - currentpackageindex*Constant.PackageSize);
	        	byte[] buffer = new byte[restsize];    
	  	        fi.read(buffer,0, restsize);
	  	        fi.close();  
	  	        log.info("restSize  : " + String.valueOf(restsize) + " and  filesize :" + String.valueOf(fileSize));
	  	      return buffer; 
		  	    
	        }else{
	        	byte[] buffer = new byte[(int) Constant.PackageSize];    
	  	        fi.read(buffer,0, Constant.PackageSize);
	  	        fi.close();  
		  	     return buffer; 
	        }
	       
	        
	      
	     
	        
	           
	    }   
	   
	   // 判断文件是否存在
	    public static int FileExists(File file) {

	        if (file.exists()) {
	            log.info("file exists");
	            return 1;
	        } else {
	            log.info("file not exists, create it ...");
	           return 0;
	        }

	    }
	    
	    public static int CalMaxpackageIndex(File file) {
	    	fileSize = file.length(); 
	    	if (fileSize > Long.MAX_VALUE) {    
	             
	            return 0;    
	        } 
	    	MaxpackageIndex = (int) (((fileSize%Constant.PackageSize)==0)?(fileSize/Constant.PackageSize):(fileSize/Constant.PackageSize +1));
	    	return 1;
	    }
}


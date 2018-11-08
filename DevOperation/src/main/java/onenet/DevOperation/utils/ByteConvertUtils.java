package onenet.DevOperation.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;



public class ByteConvertUtils {
	
	 private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', 
	            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	 public static String bytesToHexFun1(byte[] bytes) {
		
	        // 一个byte为8位，可用两个十六进制位标识
	        char[] buf = new char[bytes.length * 2];
	        int a = 0;
	        int index = 0;
	        for(byte b : bytes) { // 使用除与取余进行转换
	            if(b < 0) {
	                a = 256 + b;
	            } else {
	                a = b;
	            }

	            buf[index++] = HEX_CHAR[a / 16];
	            buf[index++] = HEX_CHAR[a % 16];
	        }

	        return new String(buf);
	    }
	 
	 /**
	  * 16进制的字符串表示转成字节数组
	  * 
	  * @param hexString
	  *            16进制格式的字符串
	  * @return 转换后的字节数组
	  **/
	 public static byte[] toByteArray(String hexString) {
	  if (StringUtils.isEmpty(hexString))
	   throw new IllegalArgumentException("this hexString must not be empty");
	 
	  hexString = hexString.toLowerCase();
	  final byte[] byteArray = new byte[hexString.length() / 2];
	  int k = 0;
	  for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
	   byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
	   byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
	   byteArray[i] = (byte) (high << 4 | low);
	   k += 2;
	  }
	  return byteArray;
	 }
	 
    public static byte[] intTo2Bytes(int i){
    	byte[] bytes = new byte[2]; 
    	bytes[1] = (byte) (i & 0xff); 
    	bytes[0] = (byte) ((i >> 8) & 0xff); 
    	return bytes; 

    }
    
    public static byte[] intTo4Bytes(int i){
    	byte[] bytes = new byte[4]; 
    	bytes[3] = (byte) (i & 0xff); 
    	bytes[2] = (byte) ((i >> 8) & 0xff); 
    	bytes[1] = (byte) ((i >> 16) & 0xff); 
    	bytes[0] = (byte) ((i >> 24) & 0xff); 
    	return bytes; 

    }
    public static byte[] intTo4Bytes(long i){
    	byte[] bytes = new byte[4]; 
    	bytes[3] = (byte) (i & 0xff); 
    	bytes[2] = (byte) ((i >> 8) & 0xff); 
    	bytes[1] = (byte) ((i >> 16) & 0xff); 
    	bytes[0] = (byte) ((i >> 24) & 0xff); 
    	return bytes; 

    }
    public static byte getXor(byte[] datas){  
    	  
        byte temp=datas[0];  
              
        for (int i = 1; i <datas.length; i++) {  
            temp ^=datas[i];  
        }  
      
        return temp;  
    } 
	public static int byteToInt(byte b) {  
	    //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值  
	    return b & 0xFF;  
	}  
	public static String convertStringToHex(String str){  
		  
	      byte[] chars = str.getBytes();  
	  
	      StringBuffer hex = new StringBuffer();  
	      for(int i = 0; i < chars.length; i++){  
	        hex.append(Integer.toHexString(byteToInt(chars[i])));  
	      }  
	      System.out.println("hex:"+hex);
	      return hex.toString();  
	      } 
	public static String StrD2StrH(String str1) {
		int  str = Integer.parseInt(str1);
		String re = Integer.toHexString(str); 
		String ddd= StringUtils.leftPad(re, 4, '0');
		
		return ddd;
	}
	
	  /**
     *  Jackson library
     * @param jsonInString
     * @return
     */
    public final static boolean isJSONValid2(String jsonInString ) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static String sysFormatStrTime() {
    	Date now = new Date(); 
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");//可以方便地修改日期格式
    	String hehe = dateFormat.format( now ); 
    	//System.out.println(hehe); 
    	return hehe;
    }
}

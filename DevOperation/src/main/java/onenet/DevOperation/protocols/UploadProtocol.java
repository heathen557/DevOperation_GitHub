package onenet.DevOperation.protocols;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.entity.FileDataPackage;
import onenet.DevOperation.utils.ByteConvertUtils;


@Slf4j
public class UploadProtocol  implements CustomProtocols{

    private byte[]   TABNO = {0x22} ;
    private byte[] 	 Cmd;
    private String 	response;

	
	@Override
	public String paraseCommend(byte[] data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getCommend() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentData(Object obj) {
		// TODO Auto-generated method stub
		
		FileDataPackage filepackage = (FileDataPackage)obj;
		String appversion = filepackage.getAppversion();
		String filecontent = filepackage.getFilecontent();
		int maxpackageindex = filepackage.getMaxpackageindex();
		int currentpackageindex = filepackage.getCurrentpackageindex();
		
		byte[] fileBytecontent = ByteConvertUtils.toByteArray(filecontent);		
		byte[] appversionByte = ByteConvertUtils.toByteArray(appversion);
		byte[] maxpackageAndcurrentpackage = {(byte)currentpackageindex,(byte)maxpackageindex};
		
		//表长
		int tablelength = TABNO.length + 2 + appversionByte.length + maxpackageAndcurrentpackage.length + 2
		 + fileBytecontent.length + RESERVE.length;
		byte[] tablelengthByte  = ByteConvertUtils.intTo2Bytes(tablelength);
		byte[] packagelengthByte  = ByteConvertUtils.intTo2Bytes(fileBytecontent.length);
		
		byte[] both1 = (byte[]) ArrayUtils.addAll(TABNO, tablelengthByte);
		byte[] both3 = (byte[]) ArrayUtils.addAll(both1, appversionByte);
		byte[] both7 = (byte[]) ArrayUtils.addAll(both3, maxpackageAndcurrentpackage);
		byte[] both2 =(byte[]) ArrayUtils.addAll(packagelengthByte, fileBytecontent);
		byte[] both6 =(byte[]) ArrayUtils.addAll(both7, both2);
		byte[] both5 =(byte[]) ArrayUtils.addAll(both6, RESERVE);
		
		//byte[] data = (byte[]) ArrayUtils.addAll(both3, both5);
		
		byte crc = ByteConvertUtils.getXor(both5);
		byte[] crcByte = {crc};
		byte[] both4 = (byte[]) ArrayUtils.addAll(both5, crcByte);
		
		byte[] both =  (byte[]) ArrayUtils.addAll(HEADER, both4);
		
		
		String cmdStr = ByteConvertUtils.bytesToHexFun1(both);
		log.info("both7:" +  Arrays.toString(both));
		log.info("maxpackageAndcurrentpackage is :" + Arrays.toString(maxpackageAndcurrentpackage));
		log.info("tablelengthByte is :" + String.valueOf(tablelength));
		log.info("package download cmd:" + cmdStr);
		
		return cmdStr.toUpperCase();
	}

	@Override
	public int parseResponse(String response) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package onenet.DevOperation.protocols;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.controller.AttrController;
import onenet.DevOperation.utils.ByteConvertUtils;


@Slf4j
public class RTCProtocol implements CustomProtocols {
 

    private byte[]   TABNO = {0x03} ;
    private byte[] 	 Cmd;
    private String 	response;
    
	public byte[] getTABNO() {
		return TABNO;
	}

	public void setTABNO(byte[] tABNO) {
		TABNO = tABNO;
	}

	public byte[] getCmd() {
		return Cmd;
	}

	public void setCmd(byte[] cmd) {
		Cmd = cmd;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

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
		String devid = (String)obj;
		
	
		long RTC = (long) (System.currentTimeMillis()/1000);
		
		byte[] AppupdatetimeByte = ByteConvertUtils.intTo4Bytes(RTC);
		byte[] cloudCommand = {0x00};
		//表长
		int tablelength = TABNO.length + 2 + AppupdatetimeByte.length + cloudCommand.length + RESERVE.length;
		byte[] tablelengthByte  = ByteConvertUtils.intTo2Bytes(tablelength);
		
		byte[] both1 = (byte[]) ArrayUtils.addAll(TABNO, tablelengthByte);
		byte[] both3 = (byte[]) ArrayUtils.addAll(both1, AppupdatetimeByte);
		byte[] both2 =(byte[]) ArrayUtils.addAll(cloudCommand, RESERVE);
		byte[] data = (byte[]) ArrayUtils.addAll(both3, both2);
		
		byte crc = ByteConvertUtils.getXor(data);
		byte[] crcByte = {crc};
		byte[] both4 = (byte[]) ArrayUtils.addAll(data, crcByte);
		
		byte[] both =  (byte[]) ArrayUtils.addAll(HEADER, both4);
		
		setCmd(both);
		String cmdStr = ByteConvertUtils.bytesToHexFun1(both);
		log.info("rtc cmd:" + cmdStr);
		
		return cmdStr.toUpperCase();
	}

	@Override
	public int parseResponse(String response) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

package onenet.DevOperation.protocols;


import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import onenet.DevOperation.entity.DevDownload;
import onenet.DevOperation.utils.ByteConvertUtils;


@Component
public class DnldProtocol  implements CustomProtocols{

//	private Object   data;
    private byte[]   TABNO = {0x06} ;
    private byte[] 	 Cmd;
    private String 	response;
    
//	public void setData(Object data) {
//		this.data = data;
//	}
//
//	public Object getData() {
//		return data;
//	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public byte[] getCmd() {
		return Cmd;
	}

	public void setCmd(byte[] cmd) {
		this.Cmd = cmd;
	}

//	public ParaProtocol(Object data) {
//		//super();
//		this.data = data;			
//	}
//	
	@Override
	public byte[] getCommend() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getContentData(Object obj) {
		
		// TODO Auto-generated method stub
		
		
		DevDownload devDownload = (DevDownload)obj;
		//int mode = Integer.parseInt(devpara.getMode());
		//int deltathup =Integer.parseInt( devpara.getVersion());
		
		//int Appupdatetime = Integer.parseInt(devpara.getupdatetime().replace(":", ""));
		//int Updatinitval = Integer.parseInt(devpara.getUpdatinitval());
		
		String[] b = devDownload.getUpdatetime().split(":");
		int Appupdatetime = Integer.valueOf(b[0])*60*60 + Integer.valueOf(b[1])*60 ;
		//System.out.println(Integer.valueOf(b[0])*60*60 + Integer.valueOf(b[1])*60 );
		byte[] modeByte = {(byte)Integer.parseInt(devDownload.getMode())};
		byte[] AppupdatetimeByte = ByteConvertUtils.intTo4Bytes(Appupdatetime);
		//表长
		int tablelength = TABNO.length + 2 + AppupdatetimeByte.length +modeByte.length + RESERVE.length;
		byte[] tablelengthByte  = ByteConvertUtils.intTo2Bytes(tablelength);
		
		byte[] both1 = (byte[]) ArrayUtils.addAll(TABNO, tablelengthByte) ;
		byte[] both2 = (byte[]) ArrayUtils.addAll(modeByte, AppupdatetimeByte);
		byte[] data = (byte[]) ArrayUtils.addAll(both1, both2);
		
		byte crc = ByteConvertUtils.getXor(data);
		byte[] crcByte = {crc};
		byte[] both3 = (byte[]) ArrayUtils.addAll(data, crcByte);
		
		byte[] both =  (byte[]) ArrayUtils.addAll(HEADER, both3);
		
		setCmd(both);
		String cmdStr = ByteConvertUtils.bytesToHexFun1(both);
		System.out.println("cmd:" + cmdStr);
		return cmdStr.toUpperCase();
	}

	@Override
	public String paraseCommend(byte[] data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int parseResponse(String response) {
		
		// TODO Auto-generated method stub
		 
		
		return 0;
	}



}

package onenet.DevOperation.protocols;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import onenet.DevOperation.entity.DevPara;
import onenet.DevOperation.utils.ByteConvertUtils;



@Component
public class ParaProtocol  implements CustomProtocols{

//	private Object   data;
    private byte[]   TABNO = {0x02} ;
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
		
		
		DevPara devpara = (DevPara)obj;
		
		if(devpara.getDeltathdown()==null||devpara.getDeltathdown().equals("")) {
			devpara.setDeltathdown("0");
		}
		if(devpara.getDeltathup()==null||devpara.getDeltathup().equals("")) {
			devpara.setDeltathup("0");
		}
		if(devpara.getUpdatinitval()==null||devpara.getUpdatinitval().equals("")) {
			devpara.setUpdatinitval("0");
		}
		
		int deltathdown = Integer.parseInt(devpara.getDeltathdown());
		int deltathup =Integer.parseInt( devpara.getDeltathup());
		
		//int appdownload = Integer.parseInt(devpara.getAppdownload());		
		//int Appupdatetime = Integer.parseInt(devpara.getAppupdatetime().replace(":", ""));
		int Updatinitval = Integer.parseInt(devpara.getUpdatinitval());
		
		byte[] deltathdownByte = ByteConvertUtils.intTo2Bytes(deltathdown);
		byte[] deltathupByte = ByteConvertUtils.intTo2Bytes(deltathup);
		//byte[] appdownloadByte = {(byte)appdownload};
		byte[] UpdatinitvalByte = {(byte)Updatinitval};
		//byte[] AppupdatetimeByte = ByteConvertUtils.intTo4Bytes(Appupdatetime);
		//表长
		int tablelength = TABNO.length + 2 + deltathdownByte.length +deltathupByte.length + UpdatinitvalByte.length  + RESERVE.length;
		byte[] tablelengthByte  = ByteConvertUtils.intTo2Bytes(tablelength);
		
		byte[] both1 = (byte[]) ArrayUtils.addAll(TABNO, tablelengthByte) ;
		byte[] both2 = (byte[]) ArrayUtils.addAll((byte[]) ArrayUtils.addAll(deltathupByte, deltathdownByte), (byte[]) ArrayUtils.addAll(UpdatinitvalByte, RESERVE));
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

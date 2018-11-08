package onenet.DevOperation.sqlservice;

public interface SyssettingService {
	
	public int bg36SyssettingV1(String  ipadress,String  masterkey,String  regcode);
	
	public int bc95SyssettingV1(String  ipadress,String  masterkey,String  regcode);
	
	public int bc95b5SyssettingV1(String  callbackUrl,String  appID,String  secert);


}

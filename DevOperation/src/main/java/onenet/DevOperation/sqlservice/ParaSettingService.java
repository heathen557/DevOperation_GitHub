package onenet.DevOperation.sqlservice;


import java.util.Map;

import onenet.DevOperation.entity.DevPara;


public interface ParaSettingService {
    
  	public DevPara findByRowid(Long rowid);
  	
	public String findSome(String devid,int page,int pageSize);
	    
	public String findDefault(String nodeid,int page,int pageSize);
	
	//public void updatePara(String deltaup ,String deltadown,String initvalue, String IMEI);
	    
	public String updatePara(String deltathup,String deltathdown,String updatinitval,Long attrid);  
	
	public Map<String, Object> findParaAndAttr(Long rowid);

	String loadPara(String ids);
    
}

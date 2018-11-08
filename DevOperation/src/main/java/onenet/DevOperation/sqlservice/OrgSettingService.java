package onenet.DevOperation.sqlservice;

import org.apache.coyote.http11.filters.VoidInputFilter;

import onenet.DevOperation.entity.DevAttr;
import onenet.DevOperation.entity.Orgnization;

public interface OrgSettingService {
	
	public DevAttr addBerth(String berthl,String operator,String protocol,String imeil,String devicetype,String imsil,String regcode,String nextnodeid,String path);
	
	public int editBerth(String operator, String protocol,String  regcode,String  berth,String  imei, String imsi, String devid, Long attrid);
	
	public DevAttr delBerth(Long attrid);

	public Orgnization addOrg(String lastpath, Integer level, String nodename, String orgid, String remark);

	public Orgnization delOrg(String attrid);

	public Orgnization editOrg(String sameorgname, String curpath, String path, String orgnewname, String nodeid,
			String remark);
}

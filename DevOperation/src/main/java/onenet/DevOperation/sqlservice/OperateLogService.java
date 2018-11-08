package onenet.DevOperation.sqlservice;

import onenet.DevOperation.entity.OperationLog;

public interface OperateLogService {

	public String findOperationItemNumbers(String starttime,String stoptime,String nodeId,Integer pagesize,Integer  pagebnumber);
	
	public int saveOperateLog(OperationLog oper);
}

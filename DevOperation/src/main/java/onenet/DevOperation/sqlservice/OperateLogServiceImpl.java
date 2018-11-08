package onenet.DevOperation.sqlservice;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import onenet.DevOperation.dao.OperationLogDao;
import onenet.DevOperation.entity.OperationLog;

@Service
public class OperateLogServiceImpl implements OperateLogService{

	@Resource
	OperationLogDao operationDao;
	
	@Override
	public String findOperationItemNumbers(String starttime, String stoptime, String nodeId,Integer  pagesize,Integer  pagebnumber) {
		int total = operationDao.findOperationItemNumbers(starttime,stoptime,nodeId);
		
		List<OperationLog> warn_beans =  operationDao.findOperationItems(starttime,stoptime,(pagebnumber-1)*pagesize, pagesize);
	   	JSONObject result = new JSONObject(); 
	   	result.put("total",total);  
	   	result.put("rows", warn_beans);
	    System.out.println(result.toJSONString());  
	    
	    return result.toJSONString();
	}

	@Override
	public int saveOperateLog(OperationLog oper) {
		// TODO Auto-generated method stub
		operationDao.saveOperateLog( oper.getCZLX(),oper.getCZNR() ,oper.getCZR() ,oper.getCZSJ() ,oper.getBZ() );
		
		return 0;
	}

}

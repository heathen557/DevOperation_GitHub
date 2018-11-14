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
		
		System.out.println("保存数据库时的内容为oper.getCZLX()："+oper.getCZLX() +"oper.getCZNR() :"+oper.getCZNR() 
						+ "oper.getCZR():" + oper.getCZR() +"oper.getCZSJ():" + oper.getCZSJ() +"oper.getBZ():" + oper.getBZ());
	
		
		// error ：could not extract ResultSet     11/14
//		operationDao.saveOperateLog( oper.getCZLX(),oper.getCZNR() ,oper.getCZR() ,oper.getCZSJ() ,oper.getBZ() );
		
		//       11/14 
		operationDao.save(oper);
		
		
		return 0;
	}

}

package onenet.DevOperation.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import onenet.DevOperation.dao.OperationLogDao;
import onenet.DevOperation.dao.WarnLogDao;
import onenet.DevOperation.entity.OperationLog;
import onenet.DevOperation.entity.TreeviewNode;
import onenet.DevOperation.entity.warn_log;

@Controller
public class RequestLogController {

	@Resource
	WarnLogDao warnDao;
	
	
	
	@Resource
	OperationLogDao operationDao;
	
	@ResponseBody
	@RequestMapping("/WarnLogServlet")
	public String requestWarnLog(@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "0") Integer  pagebnumber,  		
    		@RequestParam(value = "nodeid", defaultValue = "7") String nodeid, 
    		@RequestParam(value = "devid", defaultValue = "0") String devid,
    		@RequestParam(value = "starttime", defaultValue = "2000-00-00 00:00") String starttime,
    		@RequestParam(value = "stoptime", defaultValue = "3000-00-00 00:00") String stoptime
    		
			) {
	
		
		System.out.println("每页显示个数："+pagesize +
				" 页数:"+pagebnumber + "节点号："+nodeid +"设备号"+devid+" 开始时间"+starttime +" 结束时间"+stoptime
				);
		
		
		int total = warnDao.findWarnItemNumbers(starttime,stoptime,nodeid);
		List<warn_log> warn_beans =  warnDao.findWarnItems(starttime,stoptime,nodeid,(pagebnumber-1)*pagesize, pagesize);
	   	JSONObject result = new JSONObject(); 
	   	result.put("total",total);  
	   	result.put("rows", warn_beans);
	    System.out.println(result.toJSONString()); 
	    
	    return result.toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping("/OperationLogServlet")
	public String requestOperationLog(@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "0") Integer  pagebnumber,  		
    		@RequestParam(value = "nodeid", defaultValue = "7") String nodeid, 
    		@RequestParam(value = "devid", defaultValue = "0") String devid,
    		@RequestParam(value = "starttime", defaultValue = "2000-00-00 00:00") String starttime,
    		@RequestParam(value = "stoptime", defaultValue = "3000-00-00 00:00") String stoptime
			
			) {
	
		System.out.println("操作日志的开始时间："+starttime +"  结束时间："+stoptime);
		
		
		int total = operationDao.findOperationItemNumbers(starttime,stoptime,nodeid);
		
		List<OperationLog> warn_beans =  operationDao.findOperationItems(starttime,stoptime,(pagebnumber-1)*pagesize, pagesize);
	   	JSONObject result = new JSONObject(); 
	   	result.put("total",total);  
	   	result.put("rows", warn_beans);
	    System.out.println(result.toJSONString()); 
	    
	    return result.toJSONString();
	}
}

package onenet.DevOperation.controller;




import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.PropertyUtilcustom;
import onenet.DevOperation.dao.AttrDao;
import onenet.DevOperation.dao.DnldDao;
import onenet.DevOperation.dao.InfoDao;
import onenet.DevOperation.dao.OrgnaDao;
import onenet.DevOperation.dao.ParaDao;
import onenet.DevOperation.entity.DevAttr;
import onenet.DevOperation.entity.DevDownload;
import onenet.DevOperation.entity.DevInfo;
import onenet.DevOperation.entity.DevPara;
import onenet.DevOperation.entity.Orgnization;
import onenet.DevOperation.entity.TreeviewNode;
import onenet.DevOperation.http.methods.RegisterDeviceApi;
import onenet.DevOperation.http.methods.SendCmdsApi;
import onenet.DevOperation.http.response.BasicResponse;
import onenet.DevOperation.http.response.NewCmdsResponse;
import onenet.DevOperation.http.response.NewNbCmdsResponse;
import onenet.DevOperation.http.response.RegDeviceResponse;
import onenet.DevOperation.protocols.DnldProtocol;
import onenet.DevOperation.protocols.ParaProtocol;
import onenet.DevOperation.service.AsyncTask;
import onenet.DevOperation.service.GetAccessToken;
import onenet.DevOperation.sqlservice.AppupdatingService;
import onenet.DevOperation.sqlservice.OrgSettingService;
import onenet.DevOperation.sqlservice.ParaSettingService;
import onenet.DevOperation.constant.*;
import onenet.DevOperation.utils.FileOperationUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zhouhs on 2016/12/30.
 * @param <NewCmdsResponse>
 */
@Slf4j
@RestController
@RequestMapping(value = "/")
public class AttrController  {
	
    @Resource
    AttrDao attrDao;
    
    @Resource
    ParaDao paradao;

    @Resource
    InfoDao infodao;
    
    @Resource
    OrgnaDao orgdao;
    
    @Resource
    DnldDao dnlddao;
    
    @Resource
    ParaProtocol paraprotocol;
    
    @Resource
    DnldProtocol dnldprotocol;
    
    @Resource
    AsyncTask asyncTask;
    
    @Resource
    OrgSettingService orgSettingService;
    
    @Resource
    ParaSettingService paraSettingService;
    
    @Resource
    AppupdatingService appupdatingService;
    
    @RequestMapping(value = "/orgnization/searchnode" , method = RequestMethod.GET)
    @ResponseBody
    public Orgnization searchnode(@RequestParam(value = "devId", defaultValue = "0") String IMEI){ //已修改为按设备IMEI查找
    	
    	String path = orgdao.findpathBydevId(IMEI); 
    	String[] strlist = path.split("/");
    	Orgnization orgnization = orgdao.findorgBynodeId(strlist[5]);
         return orgnization; 
    }
    
    @RequestMapping(value = "/orgnization/inittree" , method = RequestMethod.POST)
    @ResponseBody
    public String initTree(){
    	log.info("orgniztion init tree!");
    	List<Orgnization> orglist = orgdao.findByPathLength(Constant.ORGNIZATIONSIZE);
    	List<Orgnization> orglist1 = new ArrayList<Orgnization>();
    	for (int i = 0 ; i < orglist.size(); i++) {
    		Orgnization orgnization = new Orgnization(orglist.get(i).getNodeid(),orglist.get(i).getNodename(),orglist.get(i).getPath(),null);
    		
    		//log.info(orgnization.toString());
    		orglist1.add(orgnization);
    	}
    	//System.out.println(orglist1);
    	TreeviewNode treeviewnode = new TreeviewNode();
		
		String result = treeviewnode.ToResultJson(orglist1); 
	
         //return "userHandler({\"msg\":\"ok\"})"; 
         return result;
    }
    
   
    @RequestMapping(value = "/orgnization/deletenode" , method = RequestMethod.GET)
    @ResponseBody
    public String OrgnDel(
    		@RequestParam(value = "id") Long  attrid
    		
    		){

    	 System.out.println("ids:"+attrid);   
    	 //删除
    	 try {
//	    	 orgdao.deleteNodeByNodeid(attrid);		//删除节点
//	    	 attrDao.deleteById(attrid);	//删除停车位
    		 orgSettingService.delBerth(attrid);
    	 }catch (Exception e) {
			// TODO: handle exception
    		 e.printStackTrace();       
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执
             return "{\"deldevmsg\":\"nok\"}";
    	 }
    	 return "{\"deldevmsg\":\"ok\"}"; 
    }
    
    
    @RequestMapping(value = "/orgnization/addorg" , method = RequestMethod.GET)
    @ResponseBody
    public String Orgnadd(
    		@RequestParam(value = "lastpath") String   lastpath,
    		@RequestParam(value = "level") Integer  level,
    		@RequestParam(value = "nodename") String  nodename,
    		@RequestParam(value = "orgid") String  orgid,
    		@RequestParam(value = "remark") String  remark
    		
    		){
    	
//    	String nextnodeid = orgdao.findNextNodeid();
//    	log.info("nextnodeid :"+ nextnodeid);
//    	//String lastpath = orgdao.findPathByNodeid(lastnodeid);
//    	String path = lastpath + "/" + nextnodeid;
//    	log.info("path :"+ path);
//    	Orgnization orgnization = new Orgnization(nextnodeid,nodename,path,remark);
//    	orgdao.save(orgnization);
    	
    	System.out.println("接收到的参数lastpath="+lastpath + "  level="+level + " nodename="+nodename + " orgid="+orgid +"  remark="+remark);
    	
    	try {
    		
    		orgSettingService.addOrg(lastpath, level,  nodename,  orgid, remark);
        	
    	}catch(Exception e) {
    		 e.printStackTrace();       
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执
             return "{\"addorgmsg\":\"nok\"}";
    	}
    	 return "{\"addorgmsg\":\"ok\"}"; 
    }
   
    @RequestMapping(value = "/orgnization/findorgBylevel" , method = RequestMethod.GET)
    @ResponseBody
    public String findorgBylevel(
    		@RequestParam(value = "level") Long  level
  
    		){
		    	JSONObject result = new JSONObject();  
		    	//Orgnization orgnization = new Orgnization();
		    	List<String> orglist = orgdao.findorgByLevel(level);
		        result.put("rows",orglist);   
		        System.out.println(result.toJSONString()); 
		        return result.toJSONString(); 
    	 
    }
    
    @RequestMapping(value = "/orgnization/deleteorg" , method = RequestMethod.GET)
    @ResponseBody
    public String deleteorg(
    		@RequestParam(value = "deletenodeid") String  nodeid
    		){
    	try {
    		orgSettingService.delOrg(nodeid);
    	}catch(Exception e) {
    		 e.printStackTrace();       
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执
             return "{\"deleteorg\":\"nok\"}";
    	}
    	// orgdao.delBynodeid(nodeid);
    	 return "{\"deleteorg\":\"ok\"}"; 
    }
    
    @RequestMapping(value = "/orgnization/editorg" , method = RequestMethod.GET)
    @ResponseBody
    public String editorg(
    		@RequestParam(value = "nodeid") String  nodeid,
    		@RequestParam(value = "sameorgname") String  sameorgname,
    		@RequestParam(value = "level") Integer  level,
    		@RequestParam(value = "orgname") String  orgnewname,
    		@RequestParam(value = "lastlevel") Integer  lastlevel,
    		@RequestParam(value = "lastorgname") String  lastorgname,
    		@RequestParam(value = "orgid") String  orgid,
    		@RequestParam(value = "remark") String  remark
    		
    		){
//    	Orgnization orgnization = new Orgnization();
    	
	    	String lastpath = orgdao.findlastPathByNodename(lastorgname);
	    	String curpath = orgdao.findPathByNodeid(nodeid);
	    	String temp="";
	    	for(int i=0;i < level-lastlevel ; i++) {
	    		temp+="/";
	    	}
	    	String path = lastpath + temp + nodeid;
	    	try {
		    orgSettingService.editOrg(sameorgname, curpath, path, orgnewname, nodeid, remark);
	    	}catch (Exception e) {
				// TODO: handle exception
	            e.printStackTrace();       
	            
	          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执行      
	          return "{\"editorg\":\"nok\"}"; 
	    	}
	    	return "{\"editorg\":\"ok\"}"; 
   
    	
    }
    
    @RequestMapping(value = "/orgnization/GetParentNode" , method = RequestMethod.GET)
    @ResponseBody
    public String GetParentNode(
    		@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "1") Integer  pagebnumber,
    		@RequestParam(value = "parentid", defaultValue = "1") String  parentid){
    	//Orgnization orgnization = new Orgnization();
    	
    
    	
    	
    	JSONObject result = new JSONObject();  
    	String path = orgdao.findPathByNodeid(parentid);
    	
    	System.out.println("查询的路径为：path："+ path);
    	
   		 List<Orgnization> pages = orgdao.findnodeByParentId(path,(pagebnumber-1)*pagesize,pagesize);
   		 
   	    	Integer total = orgdao.findnodeCountByParentId(path);
   	    	
   	         result.put("rows",pages);  
   	         result.put("total",total);
   	         //result.put("curtpage", pagebnumber);
   	         System.out.println(result.toJSONString()); 
   	         return result.toJSONString(); 
 
    }
    
    @RequestMapping(value = "/orgnization/addberth" , method = RequestMethod.GET)
    @ResponseBody
    public String addBerth(
    		@RequestParam(value = "operator") String operator,
			@RequestParam(value = "protocol") String protocol,
    		@RequestParam(value = "tcc") String tcc,
    		@RequestParam(value = "regcode") String regcode,
    		@RequestParam(value = "berthl") String berthl,  		
    		
    		@RequestParam(value = "imeil") String imeil,
    		@RequestParam(value = "imsil") String imsil,
    		@RequestParam(value = "berthh") String berthh,  		
    		
    		@RequestParam(value = "imeih") String imeih,
    		@RequestParam(value = "imsih") String imsih)
    		
    		{
    	if(berthh.equals("")||berthh == null ){
    		//添加组织机构属性
    		
    		String nextnodeid = orgdao.findNextNodeid();
    		String path = orgdao.findPathByNodename(tcc);
    		String devicetype = "";
    		log.info("nodename:" + tcc);
    		log.info("nextnodeid:"+nextnodeid);
    		log.info("path:" + path);
    		path += "/" ;
    		path +=nextnodeid;
    		Orgnization orgnization = new Orgnization(nextnodeid,berthl,path,"");
    		log.info(orgnization.toString());
    		
    		//orgdao.save(orgnization); 
    		
    		//添加停车位属性
    		if(protocol.equals("01")) {
    			devicetype = "bg36";
    		}else if(protocol.equals("02")) {
    			devicetype = "bc95";
    		}else if(protocol.equals("03")) {
    			devicetype = "bc95b5";
    		}
    		//DevAttr devAttr = new DevAttr(null,null,berthl,operator,protocol,imeil,devicetype,imsil,regcode,nextnodeid);
    		try {
    			orgSettingService.addBerth(berthl, operator, protocol, imeil, devicetype, imsil, regcode, nextnodeid, path);
    		}catch (Exception e) {
    	          e.printStackTrace();       
    	          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   
    	          return "{\"addberth\":\"nok\"}";
    		}
    		//异步任务
    		asyncTask.RegDevices(imeil, devicetype,imsil);
    		 return "{\"addberth\":\"ok\"}"; 	
    	}
    	
    	long imeilLong = Long.valueOf(imeil);
    	long imeihLong = Long.valueOf(imeih);
    	long berthlLong = Long.valueOf(berthl);   	
    	long imsilLong = Long.valueOf(imsil);
    	//long devidlLong = Long.valueOf(devidl);
    	String devicetype = "";
    	
		//添加停车位属性
		if(protocol.equals("01")) {
			devicetype = "bg36";
		}else if(protocol.equals("02")) {
			devicetype = "bc95";
		}else if(protocol.equals("03")) {
		
			devicetype = "bc95b5";
		}
		String path = orgdao.findPathByNodename(tcc);
		try {
			
			
	    	for(long i = imeilLong;i < (imeihLong-imeilLong + 1); i++ ) {
	        	String nextnodeid = orgdao.findNextNodeid();
	        	path += "/" ;
	    		path +=nextnodeid;
	    		//Orgnization orgnization = new Orgnization(nextnodeid,Long.toString(berthlLong),path,"");
	    		
		    	//DevAttr devAttr = new DevAttr(null,null,Long.toString(berthlLong),operator,protocol,Long.toString(imeilLong),devicetype,Long.toString(imsilLong),regcode,nextnodeid);
		    	
		    	orgSettingService.addBerth(Long.toString(berthlLong),operator,protocol,Long.toString(imeilLong),devicetype,Long.toString(imsilLong),regcode,nextnodeid,path);
		    	asyncTask.RegDevices(Long.toString(imeilLong), devicetype,Long.toString(imsilLong));
				imeilLong++;	
				berthlLong++;
				imsilLong++;
				//devidlLong++;
				
	    	}
		}catch (Exception e) {
			// TODO: handle exception
		    e.printStackTrace();       
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执行              
	        return "{\"addberth\":\"nok\"}";
		}
    	
    	
    	 //增加停车位
    	
    	   return "{\"addberth\":\"ok\"}";
    }
    
    @RequestMapping(value = "/orgnization/editberth" , method = RequestMethod.GET)
    @ResponseBody
    public String editBerth(
    				@RequestParam(value = "attrid") Long attrid,
    				@RequestParam(value = "operator") String operator,
    				@RequestParam(value = "protocol") String protocol,
    	    		@RequestParam(value = "tcc") String tcc,
    	    		@RequestParam(value = "regcode") String regcode,
    	    		@RequestParam(value = "berth") String berth,
    	    		@RequestParam(value = "devid") String devid,
    	    		@RequestParam(value = "imei") String imei,
    	    		@RequestParam(value = "imsi") String imsi)
    		
    		{

    	log.info("enter into editberth");
    	try {
    	 //编辑属性
    		orgSettingService.editBerth(operator, protocol, regcode, berth, imei, imsi, devid, attrid);
    	}catch (Exception e) {
			// TODO: handle exception
    		   e.printStackTrace();       
    	          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    	          return "{\"editberth\":\"nok\"}";
    	}
    	 return "{\"editberth\":\"ok\"}"; 
    }
    
    @RequestMapping(value = "/orgnization/findtcc" , method = RequestMethod.GET)
    @ResponseBody
    public String findtcc()
    		
    		{
    	
    	 //增加停车位
    	 JSONObject result = new JSONObject(); 
    	 List<String> tcclist = orgdao.findalltcc();
    	 result.put("tcclist", tcclist);
         System.out.println(result.toJSONString()); 
         return result.toJSONString(); 
    	  	 
    }
    
    @RequestMapping(value = "/orgnization/findberth" , method = RequestMethod.GET)
    @ResponseBody
    public String findBerth(
    				@RequestParam(value = "id") Long attrid
    				)
    		
    		{

    	 System.out.println("ids:"+attrid);   
    	 //增加停车位
    	 DevAttr attr = attrDao.findByAttrid(attrid);
    	 List<DevAttr> attrlist = new ArrayList<DevAttr>();
    	 attrlist.add(attr);
    	 List<String> tcclist = orgdao.findalltcc();
    	 String tcc = orgdao.findtccByattrid(attrid);
    	 JSONObject result = new JSONObject(); 
    	 result.put("rows",attrlist);  
    	 result.put("tcc", tcc);
    	 result.put("tcclist", tcclist);
         System.out.println(result.toJSONString()); 
         return result.toJSONString(); 
    	 
    }
    @RequestMapping(value = "/info/findInfo" , method = RequestMethod.GET)
    @ResponseBody
    public String findDevInfo(
    		@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "1") Integer  pagebnumber,  				
    		@RequestParam(value = "devid", defaultValue = "0") String imei,
    		@RequestParam(value = "nodeid",defaultValue = "1") String nodeid

    		){
    	//当第一次默认为某停车场
    	System.out.println("查詢設備時的條件為：" + "nodeid:" + nodeid + ", devid :" + imei +"页数大小："+pagesize + "页码：" + pagebnumber); 
    	 JSONObject result = new JSONObject();  
    	 //设备号为0
    	if(imei.equals("0")) {
    		Integer nodeidInt = orgdao.findTccByNodeid(nodeid); ///找到tcc
    		System.out.println("找到的TCC的編號為："+Integer.toString(nodeidInt));
    		 List<DevInfo> pages = infodao.findDefault(Integer.toString(nodeidInt),(pagebnumber-1)*pagesize,pagesize);
    		 
    	    	Integer total = infodao.totaloffindAll(Integer.toString(nodeidInt));
    	    	
    	    	System.out.println("查询到数据的个数为：" + pages.size());
    	    	
    	         result.put("rows",pages);  
    	         result.put("total",total);
    	         result.put("curtpage", pagebnumber);
    	         System.out.println(result.toJSONString()); 
    	         return result.toJSONString(); 
    	         
    	}
    	List<DevInfo> pages = infodao.findByDevId(imei);
		 
	    	Integer total = 1;
	    	Integer curtpage = 1;
	         result.put("rows",pages);  
	         result.put("total",total);   
	         result.put("curtpage", curtpage);
	         System.out.println(result.toJSONString()); 
	         return result.toJSONString(); 

    	
    }
 
    @RequestMapping(value = "/params/findPara" , method = RequestMethod.GET)
    @ResponseBody
    public String findDevPara(@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "1") Integer  pagebnumber,  		
    		@RequestParam(value = "nodeid") String nodeid, 
    		@RequestParam(value = "devid") String imei

    		){
    	
    	if(imei.equals("0")) {
    		
    		Integer nodeidInt = orgdao.findTccByNodeid(nodeid); ///找到tcc
    		System.out.println(Integer.toString(nodeidInt));
    		 List<DevPara> pages = paradao.findDefault(Integer.toString(nodeidInt),(pagebnumber-1)*pagesize,pagesize);
    	    	Integer total = paradao.totaloffindAll(Integer.toString(nodeidInt));
    	    	 JSONObject result = new JSONObject();  
    	         result.put("rows",pages);  
    	         result.put("total",total);  
    	         System.out.println(result.toJSONString()); 
    	         return result.toJSONString();  
    	}
    	 System.out.println("nodeid:"+nodeid+",devid:"+imei); 
//    	 System.out.println("pagenumber:"+pagebnumber+"pagesize:"+pagesize); 
    	 List<DevPara> pages = paradao.findSome(imei,(pagebnumber-1)*pagesize,pagesize);
    	 Integer total = 1;
    	 JSONObject result = new JSONObject();  
         result.put("rows",pages);  
         result.put("total",total);  
         System.out.println(result.toJSONString()); 
         return result.toJSONString();  
    }
    
    //下载参数表显示
    @RequestMapping(value = "/upload/findDownPara" , method = RequestMethod.GET)
    @ResponseBody
    public String findDevDown(@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "1") Integer  pagebnumber,  		
    		@RequestParam(value = "nodeid") String nodeid, 
    		@RequestParam(value = "devId") String imei

    		){
    	
    	if(imei.equals("0")) {
    		Integer nodeidInt = orgdao.findTccByNodeid(nodeid); ///找到tcc
    		System.out.println(Integer.toString(nodeidInt));
    		 List<DevDownload> pages = dnlddao.findDefault(Integer.toString(nodeidInt),(pagebnumber-1)*pagesize,pagesize);
    	    	Integer total = dnlddao.totaloffindAll(Integer.toString(nodeidInt));
    	    	 JSONObject result = new JSONObject();    
    	         result.put("rows",pages);  
    	         result.put("total",total);  
    	         System.out.println(result.toJSONString());  
    	         return result.toJSONString();  
    	}
    	 System.out.println("nodeid:"+nodeid+",devid:"+imei); 
//    	 System.out.println("pagenumber:"+pagebnumber+"pagesize:"+pagesize); 
    	 List<DevDownload> pages = dnlddao.findSome(imei,(pagebnumber-1)*pagesize,pagesize);
    	 Integer total = 1;
    	 JSONObject result = new JSONObject();  
         result.put("rows",pages);  
         result.put("total",total);  
         System.out.println(result.toJSONString()); 
         return result.toJSONString();  
    }
    
    
   	/**
		 * 设备注册
		 * 参数顺序与构造函数顺序一致
		 * @param code：设备注册码（必填）,String
		 * @param mac：设备唯一mac标识，最长32字符
		 * @param imei：设备唯一标识String类型，最长512字符
		 * @param title:设备名（可选） 最长32个字符
		 * @param key:设备注册码（必填）
		 */
 

    @RequestMapping(value = "/attribute/findAll" , method = RequestMethod.GET)
    @ResponseBody
    public String findAll(@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "0") Integer  pagebnumber,  		
    		@RequestParam(value = "nodeid", defaultValue = "1") String nodeid, 
    		@RequestParam(value = "devid", defaultValue = "0") String imei
    		

    		){
    	
    	if(imei.equals("0")) {
    		
    		Integer nodeidInt = orgdao.findTccByNodeid(nodeid); ///找到tcc	
    		List<DevAttr> pages = attrDao.findallBytcc(Integer.toString(nodeidInt),(pagebnumber-1)*pagesize,pagesize); 		
    	    Integer total = attrDao.totaloffindAll(Integer.toString(nodeidInt));
    	    	 JSONObject result = new JSONObject();  
    	         result.put("rows",pages);  
    	         result.put("total",total);  
    	         System.out.println(result.toJSONString()); 
    	         return result.toJSONString();  
    	}
    	// System.out.println("tcc:"+tcc+",areaid:"+areaid+",berth:"+berth); 
//    	 System.out.println("pagenumber:"+pagebnumber+"pagesize:"+pagesize); 
    	 List<DevAttr> pages = attrDao.findallBydevid(imei);
    	Integer total = 1;
    	 JSONObject result = new JSONObject();  
         result.put("rows",pages);  
         result.put("total",total);  
         System.out.println(result.toJSONString()); 
         return result.toJSONString();  
    }


 
    @RequestMapping(value = "/attribute/deleteById" , method = RequestMethod.GET)
    public String deleteById(@RequestParam(value = "ids") String id){
    	 System.out.println("ids:"+id); 
    	 String[]  strs=id.split(",");
    	 try {
	    	 for(int i=0,len=strs.length;i<len;i++){
	    		 attrDao.deleteById(Long.parseLong(strs[i]));
	    	 }
    	 }catch (Exception e) {
			// TODO: handle exception
    		   e.printStackTrace();       
    	          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
    	 return "userHandler({\"msg\":\"ok\"})"; 
       

    }


 
    @RequestMapping(value = "/params/editPara" , method = RequestMethod.GET)
    @ResponseBody
    public String editPara(@RequestParam(value = "rowid") Long attrid,
    		@RequestParam(value = "deltathup" ) String deltathup,
    		@RequestParam(value = "deltathdown") String deltathdown,
    		@RequestParam(value = "updatinitval") String updatinitval,
    		@RequestParam(value = "state") String state
    		
    		
    		){
    	log.info("执行编辑参数设置功能");
    	DevPara para = new DevPara();
 
    	if(deltathdown == null) {
    		deltathdown ="0";
    	}
    	if(deltathup == null) {
    		deltathup ="0";
    	}
    	if(updatinitval == null) {
    		updatinitval ="0";
    	}
    	para.setDeltathdown(deltathdown);
    	para.setDeltathup(deltathup);
    	para.setRowid(attrid);
    	para.setUpdatinitval(updatinitval);
    	try {
        	paraSettingService.updatePara(deltathup,deltathdown,updatinitval,attrid);

    	}catch(Exception e) {
   		   e.printStackTrace();       
	          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	          //return "{\"editpara\":\"nok\"}";
    	}
//    	List<DevPara> devpara =  paradao.findByRowid(attrid);
//    	//attrDao.save(attr);
//    	
    	  
    	   JSONObject jo = new JSONObject();
           
        	   jo.put("rowid", attrid);
               jo.put("deltathup",deltathup);
               jo.put("deltathdown", deltathdown);
               jo.put("updatinitval", updatinitval);
             
              // json.put(jo);
          
         return  jo.toString() ;
        // return "userHandler("+jo.toString()+")";
    }
  
    //
    @RequestMapping(value = "/upload/editDownPara" , method = RequestMethod.GET)
    @ResponseBody
    public String editUploadPara(@RequestParam(value = "rowid") Long rowid,
    		@RequestParam(value = "status") String status,
    		@RequestParam(value = "progress") String progress,
    		@RequestParam(value = "mode") String mode,
    		@RequestParam(value = "version") String version,
    		@RequestParam(value = "updatetime") String updatetime
    		
    		
    		){
    	log.info("执行编辑参数设置功能");
    	DevDownload devDownload = new DevDownload();
    	devDownload.setMode(mode);
    	devDownload.setProgress(progress);
    	devDownload.setRowid(rowid);
    	devDownload.setStatus(status);
    	
    	devDownload.setVersion(version);
    	
    	dnlddao.save(devDownload);
//    	List<DevPara> devpara =  paradao.findByRowid(attrid);
//    	//attrDao.save(attr);
//    	
    	  
    	   JSONObject jo = new JSONObject();
           
        	   jo.put("rowid", rowid);
               jo.put("status",status);
               jo.put("mode", mode);
               jo.put("progress", progress);
               jo.put("version", version);
               jo.put("updatetime", updatetime);
              // json.put(jo);
          
         return  jo.toString() ;
        // return "userHandler("+jo.toString()+")";
    }
    
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "devid" , value = "devid" , paramType = "query" , required = true ),
//            @ApiImplicitParam(name = "devpostion" , value = "devpostion" , paramType = "query" , required = true ),
//            @ApiImplicitParam(name = "devarea" , value = "devarea" , paramType = "query" , required = true ),
//            @ApiImplicitParam(name = "tcc" , value = "tcc" , paramType = "query" , required = true )
//    })
    @RequestMapping(value = "/params/savePara" , method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String savePara(
    		@RequestParam(value = "appdownload",defaultValue = "0") String appdownload,
    		@RequestParam(value = "version",defaultValue = "0000") String version,
    		@RequestParam(value = "appupdatetime",defaultValue = "0000") String appupdatetime){

    	
    	//paradao.updatedownloadPara(appdownload,appupdatetime,version);
         return "{\"msg\":\"ok\"}"; 
    }
    
    //文件上传后台处理
    @RequestMapping(value = "/upload" , method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file){
    	if (file.isEmpty()) {
           
            return "{\"error\":\"file is null!\"}";
        }
    	 try {
    		 File directory = new File("");
    		 
    	        // Get the file and save it somewhere
    	        byte[] bytes = file.getBytes();
    	        Path path = Paths.get(directory.getAbsolutePath() + System.getProperty("file.separator")+ file.getOriginalFilename());
    	        FileOperationUtil.UploadFilepath = path.toString();
    	        FileOperationUtil.fileName = file.getName();
    	        log.info("file path is :" + path.toString());
    	        Files.write(path, bytes);

    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	
         return "{\"msg\":\"ok\"}"; 
    }
    
    //(未完成)
    @RequestMapping(value = "/upload/uploadcancel" , method = RequestMethod.GET)
    @ResponseBody
    public String uploadcancel(){
    	
    	//发送指令
    	
         return "{\"msg\":\"ok\"}";  
    }
    
    @RequestMapping(value = "/upload/savednldPara" , method = RequestMethod.GET)
    @ResponseBody
    public String savednldpara(
    		@RequestParam(value = "appdownload") String mode,
    		@RequestParam(value = "version") String version,
    		@RequestParam(value = "appupdatetime") String updatetime,
    		@RequestParam(value = "ids") String ids,
    		@RequestParam(value = "rowid") String rowids
    		){
    	
    	//发送指令
    	DevDownload devDownload = new DevDownload();
    	System.out.println("ids:"+ids); 
   	 String[]  strs=ids.split(",");
   	 try {
   		for(int i=0,len=strs.length;i<len;i++){
      		 long rowid = Long.parseLong(strs[i]);
      	//保存到数据库
      		//dnlddao.updatedownloadPara( mode , updatetime,  FileOperationUtil.fileName, rowid);
      		appupdatingService.updatedownloadPara(mode, updatetime, FileOperationUtil.fileName, rowid);
      		 devDownload.setMode(mode);
      		devDownload.setRowid(rowid);
      		devDownload.setUpdatetime(updatetime);
      		//devDownload.setVersion(version);
      		 DevAttr attrlist =  attrDao.findByAttrid(rowid);
      		 String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
      		 //协议封装、缓存
   	
      			String devid = attrlist.getDevid();
      			String devicetype = attrlist.getDevicetype();
      			String cmd = dnldprotocol.getContentData(devDownload);
      			//缓存redis或hashmap
      			//stringRedisTemplate.opsForValue().set(devid, cmd);
      			//httpclient 请求
      			//String devId = devid;
      			String imei =attrlist.getImei();
      			Integer inst_id = 6;
      			SendCmdsApi api = null;
      			if(devicetype.equals("bc95")) {
      				imei = attrlist.getImei();
      		 		api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, cmd, 
      		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bc95masterkey"),
      		 				devicetype,imei,inst_id);
	      		 	BasicResponse<NewNbCmdsResponse> response = api.executeNbApi();
	      		    NewNbCmdsResponse cmdresponse = response.getData();
	      		    cmdresponse.getuuid();
   			log.info("bc95 response is :" + response.getJson());
   			
      			}
      			if(devicetype.equals("bg36")) {
      				
      		 		api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, cmd, 
      		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bg36masterkey")
      		 				,devicetype,null,null);
      		 	
      		 	BasicResponse<NewCmdsResponse> response = api.executeApi();
   			NewCmdsResponse cmdresponse = response.getData();
   			String cmduuid = cmdresponse.getCmduuid();
   			log.info("bg36 response is :" + response.getJson());
      			}
      			if(devicetype.equals("bc95b5")) {
      				String serviceId = "test";
      				String method = "downstream";
      				
      				api = new SendCmdsApi(devid, serviceId, method, cmd, GetAccessToken.accessToken);
      			}	
      		 
      	 }
   	 }catch(Exception e) {
   	  e.printStackTrace();       
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
   		 return "{\"downpara\":\"nok\"}";
   	 }
   	 
         return "{\"downpara\":\"ok\"}";  
    }
    
    
    //发送平台cmd命令(查询数据库)
    /**
	 * 发送命令
	 * @param devId：接收该数据的设备ID（必选），String
	 * @param qos:是否需要响应，默认为0,Integer
	 * 0：不需要响应，即最多发送一次，不关心设备是否响应；
	 * 1：需要响应，如果设备收到命令后没有响应，则会在下一次设备登陆时若命令在有效期内(有效期定义参见timeout参数）则会继续发送。
	 * 对响应时间无限制，多次响应以最后一次为准。
	 * 本参数仅当type=0时有效；
	 * @param timeOut:命令有效时间，默认0,Integer
	 * 0：在线命令，若设备在线,下发给设备，若设备离线，直接丢弃； 
	 *  >0： 离线命令，若设备在线，下发给设备，若设备离线，在当前时间加timeout时间内为有效期，有效期内，若设备上线，则下发给设备。单位：秒，有效围：0~2678400。
	 *  本参数仅当type=0时有效；
	 * @param type://默认0。0：发送CMD_REQ包，1：发送PUSH_DATA包
	 * @param contents:用户自定义数据：json、string、二进制数据（小于64K）
	 * @param key:masterkey或者设备apikey
	 */
    @RequestMapping(value = "/params/ParaAppSetting" , method = RequestMethod.GET)
    @ResponseBody
    public String saveAppPara(@RequestParam(value = "id") String ids){

    	//DevPara para = new DevPara();
    	//log.info("enter into /para/paraappsetting!");
//    	 String[]  strs=ids.split(",");
//    	 String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
//    	
//    	 for(int i=0,len=strs.length;i<len;i++){
//    		 long rowid = Long.parseLong(strs[i]);
//    		
//    	
//    		 DevPara paralist =  paradao.findByRowid(rowid);
//    		 DevAttr attrlist =  attrDao.findByAttrid(rowid);
//    		
//    		 //协议封装、缓存
//    	
//    			 
//    			//DevPara paraCmd = paralist;  
//    			String devid = attrlist.getDevid();
//    			String devicetype = attrlist.getDevicetype();
//    			String cmd = paraprotocol.getContentData(paralist);
//    			log.info("cmd:" + cmd);   		
//    			String devId = devid;
//
//    			
//    			String imei =null;
//    			Integer inst_id = 2;
//    			SendCmdsApi api = null;
//    			if(devicetype.equals("bc95")) {
//    				//log.info("enter into sendcmdapi!");
//    				imei = attrDao.findImei(devid);
//    		 		api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, cmd, 
//    		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bc95masterkey"),
//    		 				devicetype,imei,inst_id);
//    		 		log.info("sendcmdsapi end!" );
//    		 	
//        			BasicResponse<NewNbCmdsResponse> response = api.executeNbApi();
//        			
//        			NewNbCmdsResponse cmdresponse = response.getData();
//    				
//    				String cmduuid = cmdresponse.getuuid();
//    			}
//    			if(devicetype.equals("bg36")) {
//    		 		api = new SendCmdsApi(devid, null,Constant.TIMEOUT, null, cmd, 
//    		 				PropertyUtilcustom.getPro(path, "onenetparasetting.bg36masterkey"),
//    		 				devicetype,imei,null);
//    		 		log.info("out of sendcmdsapi !" );
//        			BasicResponse<NewCmdsResponse> response = api.executeApi();
//        			log.info("out of response !" );
//    				NewCmdsResponse cmdresponse = response.getData();
//    			}
//    			if(devicetype.equals("bc95b5")) {
//       				String serviceId = "test";
//       				String method = "downstream";
//       				
//       				api = new SendCmdsApi(devid, serviceId, method, cmd, GetAccessToken.accessToken);
//       				
//       				api.executeOcNbApi();
//       			}
//    		
//				
//				//String cmduuid = cmdresponse.getCmduuid();
//				//log.info("bg36 response is :" + response.getJson());
	 
//    	 }
    
    	String re = paraSettingService.loadPara(ids);
    	if(re.equals("-1")) {
    		  return "{\"paramsg\":\"nok\"}";  
    	}else {
    		  return "{\"paramsg\":\"ok\"}"; 
    	}
       
    }
    
    
}

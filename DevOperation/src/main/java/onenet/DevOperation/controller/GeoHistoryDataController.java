package onenet.DevOperation.controller;


import com.alibaba.fastjson.JSONObject;

import onenet.DevOperation.dao.OrgnaDao;
import onenet.DevOperation.dao.geoDao;
import onenet.DevOperation.entity.Geo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zhouhs on 2016/12/30.
 */
@RestController
@RequestMapping(value = "/geohistorydata")

public class GeoHistoryDataController {

    @Resource
    geoDao geodao;
    @Resource
    OrgnaDao orgdao;

    @RequestMapping(value = "/findByDevid" , method = RequestMethod.GET)
    public String findById(@RequestParam(value = "starttime") String starttime,
    		@RequestParam(value = "stoptime") String stoptime,
    		@RequestParam(value = "areaid") String areaid,
    		@RequestParam(value = "tcc") String tcc,
    		@RequestParam(value = "berth") String berth
    		){

    	Geo geo = geodao.findByDevid(starttime);

        if(geo == null){
            return "error";
        }else{
        	
            return "succ";
        } 
    }



    @RequestMapping(value = "/findAll" , method = RequestMethod.GET)
    @ResponseBody
    public String findAll(@RequestParam(value = "pageSize", defaultValue = "20") Integer  pagesize,
    		@RequestParam(value = "pageNumber", defaultValue = "1") Integer  pagebnumber,
    		@RequestParam(value = "starttime") String starttime,
    		@RequestParam(value = "stoptime") String stoptime,
    		@RequestParam(value = "devid", defaultValue = "0") String imei,
    		@RequestParam(value = "nodeid", defaultValue = "1") String nodeid){
    	
    	System.out.println("starttime:"+starttime+"stoptime:"+stoptime+"devid:"+imei);  
    	System.out.println("pagenumber:"+pagebnumber+"pagesize:"+pagesize);  
    	//Pageable pageable = new PageRequest(pagebnumber, pagesize);  
    	
//    	List<Geo> pages = geodao.findByRtc(areaid ,tcc,starttime,stoptime,pageable);
//       // Page<Geo> pages= geodao.findAll(pageable);
//        JSONObject result = new JSONObject();  
//        result.put("rows",pages);  
//        result.put("total",pages.size());  
//        System.out.println(result.toJSONString());  
        
        //List<Geo> pages = geodao.findByRtc(starttime,stoptime,pagebnumber,pagesize);
    	starttime += ":00";
    	stoptime += ":00";
    	if(imei.equals("0")) {
    		Integer nodeidInt = orgdao.findTccByNodeid(nodeid); ///找到tcc
    		System.out.println("nodeidInt:"+Integer.toString(nodeidInt));  
    		List<Geo> pages = geodao.findByNodeid(starttime, stoptime, Integer.toString(nodeidInt), (pagebnumber-1)*pagesize, pagesize);
            Integer total =geodao.totaloffindAll1(starttime, stoptime, Integer.toString(nodeidInt));
            System.out.println("total:"+total);  
            JSONObject result = new JSONObject();  
            result.put("rows",pages);  
            result.put("total",total);  
            System.out.println(result.toJSONString()); 
    		return result.toJSONString();
    	}
        List<Geo> pages = geodao.findByRtc(starttime,stoptime,imei,(pagebnumber-1)*pagesize,pagesize);
        Integer total =geodao.totaloffindAll(starttime,stoptime,imei);
        System.out.println("total:"+total);  
        JSONObject result = new JSONObject();  
        result.put("rows",pages);  
        result.put("total",total);  
        System.out.println(result.toJSONString()); 
        return result.toJSONString();  
       

    }


}
package onenet.DevOperation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.entity.DevInfo;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class HtmlController {
	
	@RequestMapping(value = "/history")
    public ModelAndView test1(ModelAndView mv) {
        mv.setViewName("dev_his");
        //mv.addObject("title","欢迎使用Thymeleaf!");
        return mv;
    }
    
    @RequestMapping(value = "/syslog")
    public ModelAndView test2(ModelAndView mv) {
        mv.setViewName("syslog");
        //mv.addObject("title","欢迎使用Thymeleaf!");
        return mv;
    }

    @RequestMapping(value = "/devpara")
    public ModelAndView test4(ModelAndView mv) {
        mv.setViewName("dev_para");
        //mv.addObject("title","欢迎使用Thymeleaf!");
        return mv;
    }
    
    @RequestMapping(value = "/starter")
    public ModelAndView test(ModelAndView mv) {
    	List<DevInfo > testVos = null;
    	//List<DevInfo > testVos =new ArrayList<>();			
        mv.setViewName("starter");
        mv.addObject("devinfo",testVos);
        
        return mv;
    }
    
    @RequestMapping(value = "/orgnization")
    public ModelAndView test5(ModelAndView mv) {
    	//List<DevInfo > testVos = null;
    	//List<DevInfo > testVos =new ArrayList<>();			
        mv.setViewName("dev_org");
       
        return mv;
    }
    
    @RequestMapping(value = "/berth")
    public ModelAndView test6(ModelAndView mv) {
    	//List<DevInfo > testVos = null;
    	//List<DevInfo > testVos =new ArrayList<>();			
        mv.setViewName("dev_berth");
       
        return mv;
    }
    
    @RequestMapping(value = "/syssetting")
    public ModelAndView syssetting(ModelAndView mv) {
    	//List<DevInfo > testVos = null;
    	//List<DevInfo > testVos =new ArrayList<>();			
        mv.setViewName("sysParaSetting");
       
        return mv;
    }
    
    @RequestMapping(value = "/upload")
    public ModelAndView upload(ModelAndView mv) {
    	//List<DevInfo > testVos = null;
    	//List<DevInfo > testVos =new ArrayList<>();			
        mv.setViewName("dev_download");
       
        return mv;
    }
}

package onenet.DevOperation.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;
import onenet.DevOperation.PropertyUtilcustom;


@Slf4j
@RestController
@RequestMapping(value = "/")
public class SyssettingController {

	  
	   
	   @RequestMapping(value = "/syssetting/onenetparasetting" , method = RequestMethod.GET)
	    @ResponseBody
	    public String onenetparasetting(
	    		@RequestParam(value = "bg36ipadress") String  ipadress,
	    		@RequestParam(value = "bg36masterkey") String  masterkey,
	    		@RequestParam(value = "bg36regcode") String  regcode
	  
	    		){
		   log.info("enter into bg36syssetting!");
		   String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bg36ipadress", ipadress);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bg36masterkey", masterkey);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bg36regcode", regcode);
		   
		   return "{\"msg\":\"ok\"}"; 
	    }
	   @RequestMapping(value = "/syssetting/onenetparasetting1" , method = RequestMethod.GET)
	    @ResponseBody
	    public String onenetparasetting1(
	    		@RequestParam(value = "bc95ipadress") String  ipadress,
	    		@RequestParam(value = "bc95masterkey") String  masterkey,
	    		@RequestParam(value = "bc95regcode") String  regcode
	  
	    		){
		   String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bc95ipadress", ipadress);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bc95masterkey", masterkey);
		   PropertyUtilcustom.updatePro(path, "onenetparasetting.bc95regcode", regcode);
		   
		   return "{\"msg\":\"ok\"}"; 
	    }
	   
	  
	   @RequestMapping(value = "/syssetting/oceanparasetting" , method = RequestMethod.GET)
	    @ResponseBody
	    public String oceanparasetting(
	    		@RequestParam(value = "callbackUrl") String  callbackUrl,
	    		@RequestParam(value = "appID") String  appID,
	    		@RequestParam(value = "secert") String  secert
	  
	    		){
		   String path = PropertyUtilcustom.class.getResource("/syssettings.properties").getPath();
		   PropertyUtilcustom.updatePro(path, "oceanparasetting.bc95b5_CALLBACK_BASE_URL", callbackUrl);
		   PropertyUtilcustom.updatePro(path, "oceanparasetting.bc95b5_APPID", appID);
		   PropertyUtilcustom.updatePro(path, "oceanparasetting.bc95b5_SECRET", secert);
		   
		   return "{\"msg\":\"ok\"}"; 
	    }
}

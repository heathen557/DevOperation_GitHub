package onenet.DevOperation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GetAccessToken {

	public static String accessToken;
	  private static Logger logger = LoggerFactory.getLogger(GetAccessToken.class); 
	   @RabbitListener(queues = "accesstoken")
	 @RabbitHandler
	    public void process(String message) {
		   logger.info("accesstoken is :" +message);
		   accessToken = message;
	   }
}

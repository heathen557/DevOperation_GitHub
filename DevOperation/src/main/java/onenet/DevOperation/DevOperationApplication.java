package onenet.DevOperation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration  
@ComponentScan  
@EnableAutoConfiguration 
@EnableAsync  
@SpringBootApplication
@EnableTransactionManagement
public class DevOperationApplication extends SpringBootServletInitializer{
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder Recvapp) {
        return Recvapp.sources(DevOperationApplication.class);
    }
	public static void main(String[] args) {
		SpringApplication.run(DevOperationApplication.class, args);
	}
}

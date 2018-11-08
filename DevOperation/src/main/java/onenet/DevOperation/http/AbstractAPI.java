package onenet.DevOperation.http;



import com.fasterxml.jackson.databind.ObjectMapper;

import onenet.DevOperation.http.methods.RequestInfo.Method;


public abstract class AbstractAPI <T>{
	public String key;
	public String url;
	public Method method;
    public ObjectMapper mapper = new ObjectMapper();
}

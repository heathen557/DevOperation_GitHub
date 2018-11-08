package onenet.DevOperation.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewNbCmdsResponse {
	  @JsonProperty(value="uuid")
	    public String uuid;

	public String getuuid() {
		return uuid;
	}

	public void setuuid(String uuid) {
		this.uuid = uuid;
	}
}

package onenet.DevOperation.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Created by zhouhs on 2016/12/30.
 */
@Entity
@Table(name = "devattr")
public class DevAttr implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attrid;
    
	private String devid;
	private String devpostion;
	private String operator;
	private String protocol;
	private String imei;
	private String devicetype;
	private String imsi;
	private String regcode;
	public DevAttr(){}
	public DevAttr(Long attrid, String devid, String devpostion, String operator, String protocol, String imei,
			String devicetype, String imsi, String regcode, String nodeid) {
		
		this.attrid = attrid;
		this.devid = devid;
		this.devpostion = devpostion;
		this.operator = operator;
		this.protocol = protocol;
		this.imei = imei;
		this.devicetype = devicetype;
		this.imsi = imsi;
		this.regcode = regcode;
		this.nodeid = nodeid;
	}
	
	public String getRegcode() {
		return regcode;
	}
	public void setRegcode(String regcode) {
		this.regcode = regcode;
	}
	private String nodeid;
	
    public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Long getAttrid() {
		return attrid;
	}
	public void setAttrid(Long attrid) {
		this.attrid = attrid;
	}
	public String getDevid() {
        return devid;
    }
    public void setDevid(String devid) {
        this.devid = devid;
    }
    
    public void setDevpostion(String devpostion) {
        this.devpostion = devpostion;
    }
    public String getDevpostion() {
        return devpostion;
    }
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
    
   
}

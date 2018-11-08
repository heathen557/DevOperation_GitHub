package onenet.DevOperation.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhouhs on 2016/12/30.
 */
@Entity
@Table(name = "devinfo")
public class DevInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rowid;
    
	private String carinfo;
	private String rtc;
	private String power;
	private String pairid;
	private String onlinestat;
	private String imei;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	private String devpostion;
	private String remark;

	public String getDevpostion() {
		return devpostion;
	}
	public void setDevpostion(String devpostion) {
		this.devpostion = devpostion;
	}
	

	
	public String getOnlinestat() {
		return onlinestat;
	}
	public void setOnlinestat(String onlinestat) {
		this.onlinestat = onlinestat;
	}
	public String getPairid() {
		return pairid;
	}
	public void setPairid(String pairid) {
		this.pairid = pairid;
	}
	public Long getRowid() {
		return rowid;
	}
	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}
	public String getCarinfo() {
		return carinfo;
	}
	public void setCarinfo(String carinfo) {
		this.carinfo = carinfo;
	}
	public String getRtc() {
		return rtc;
	}
	public void setRtc(String rtc) {
		this.rtc = rtc;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


  
}


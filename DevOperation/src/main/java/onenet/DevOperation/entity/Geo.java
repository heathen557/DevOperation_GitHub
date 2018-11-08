package onenet.DevOperation.entity;

import javax.persistence.*;
/**
 * Created by zhouhs on 2016/12/30.
 */
@Entity
@Table(name = "geoval")
public class Geo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	private String imei;
	private String devid;
	private String initx;
	private String inity;
	private String initz;
	
	private String curx;
	private String cury;
	private String curz;
	
	private String carinfo;
	private String rtc;
	private String pairid;
	
//	private String devarea;
//	private String tcc;
//	private String devpostion;
//	
//	public String getDevpostion() {
//		return devpostion;
//	}
//	public void setDevpostion(String devpostion) {
//		this.devpostion = devpostion;
//	}
//	public String getTcc() {
//		return tcc;
//	}
//	public void setTcc(String tcc) {
//		this.tcc = tcc;
//	}
//	public String getDevarea() {
//		return devarea;
//	}
//	public void setDevarea(String devarea) {
//		this.devarea = devarea;
//	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	
	public String getDevid() {
		return devid;
	}
	public void setDevid(String devid) {
		this.devid = devid;
	}
	public String getInitx() {
		return initx;
	}
	public void setInitx(String initx) {
		this.initx = initx;
	}
	public String getInity() {
		return inity;
	}
	public void setInity(String inity) {
		this.inity = inity;
	}
	public String getInitz() {
		return initz;
	}
	public void setInitz(String initz) {
		this.initz = initz;
	}
	public String getCurx() {
		return curx;
	}
	public void setCurx(String curx) {
		this.curx = curx;
	}
	public String getCury() {
		return cury;
	}
	public void setCury(String cury) {
		this.cury = cury;
	}
	public String getCurz() {
		return curz;
	}
	public void setCurz(String curz) {
		this.curz = curz;
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
	public String getPairid() {
		return pairid;
	}
	public void setPairid(String pairid) {
		this.pairid = pairid;
	}

	
 
    
}


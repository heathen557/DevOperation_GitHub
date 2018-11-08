package onenet.DevOperation.entity;





import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "warn_log")
public class warn_log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer LOGGID;
    
   
    private Integer TYPE;
	
	@Column(length=10)
	private String CZR;
	@Column(length=50)
	private String GJNR;

	@Column(length=30)
	private String GJSJ;
	
	@Column(length=100)
	private String BZ;
	
	
	public String getGJNR() {
		return GJNR;
	}
	public void setGJNR(String gJNR) {
		GJNR = gJNR;
	}
	public Integer getLoggid() {
		return LOGGID;
	}
	public void setLoggid(Integer loggid) {
		this.LOGGID = loggid;
	}
	

	public Integer getTYPE() {
		return TYPE;
	}
	public void setTYPE(Integer tYPE) {
		TYPE = tYPE;
	}
	public String getCzr() {
		return CZR;
	}
	public void setCzr(String czr) {
		this.CZR = czr;
	}
	
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public String getGjsj() {
		return GJSJ;
	}
	public void setGjsj(String gjsj) {
		this.GJSJ = gjsj;
	}
	public String getBz() {
		return BZ;
	}
	public void setBz(String bz) {
		this.BZ = bz;
	}
	
	
	
	
	


	
	
	
}

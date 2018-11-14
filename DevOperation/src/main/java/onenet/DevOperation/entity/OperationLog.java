package onenet.DevOperation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "operation_log")
public class OperationLog {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer LOGGID;

	@Column(length=10)
	private Integer CZLX;
	
	private String CZNR;
	
	@Column(length=10)
	private String CZR;
	
	@Column(length=30)
	private String CZSJ;
	
	
	public OperationLog(Integer cZLX, String cZNR, String cZR, String cZSJ, String bZ) {
		super();
		
		CZLX = cZLX;
		CZNR = cZNR;
		CZR = cZR;
		CZSJ = cZSJ;
		BZ = bZ;
	}
	public Integer getLOGGID() {
		return LOGGID;
	}
	public void setLOGGID(Integer lOGGID) {
		LOGGID = lOGGID;
	}

	public Integer getCZLX() {
		return CZLX;
	}
	public void setCZLX(Integer cZLX) {
		CZLX = cZLX;
	}
	public String getCZNR() {
		return CZNR;
	}
	public void setCZNR(String cZNR) {
		CZNR = cZNR;
	}
	public String getCZR() {
		return CZR;
	}
	public void setCZR(String cZR) {
		CZR = cZR;
	}
	public String getCZSJ() {
		return CZSJ;
	}
	public void setCZSJ(String cZSJ) {
		CZSJ = cZSJ;
	}
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
	private String BZ;


	@Override
	public String toString() {
		return "OperationLog [LOGGID=" + LOGGID + ", CZLX=" + CZLX + ", CZNR=" + CZNR + ", CZR=" + CZR + ", CZSJ="
				+ CZSJ + ", BZ=" + BZ + "]";
	}


	
	
	
	
	
}

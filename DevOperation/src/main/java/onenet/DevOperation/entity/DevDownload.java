package onenet.DevOperation.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "devdownload")
public class DevDownload {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rowid;
	
	private String status;
	private String mode;
	private String updatetime;
	private String version;
	private String progress;
	private String devpostion;
	private String imei;
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Long getRowid() {
		return rowid;
	}
	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}
	public String getDevpostion() {
		return devpostion;
	}
	public void setDevpostion(String devpostion) {
		this.devpostion = devpostion;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	@Override
	public String toString() {
		return "DevDownload [rowid=" + rowid + ", status=" + status + ", mode=" + mode + ", updatetime=" + updatetime
				+ ", version=" + version + ", progress=" + progress + ", devpostion=" + devpostion + ", imei=" + imei
				+ "]";
	}
	
	
}

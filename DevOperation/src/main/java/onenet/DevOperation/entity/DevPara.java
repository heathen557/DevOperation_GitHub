package onenet.DevOperation.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "devpara")
public class DevPara {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)	   
	    private Long rowid;	
	   
	    private String devpostion; 
		private String deltathup;
		private String deltathdown;
		private String updatinitval;
		private String imei;

		
		public String getImei() {
			return imei;
		}
		public void setImei(String imei) {
			this.imei = imei;
		}
	
		public String getDevpostion() {
			return devpostion;
		}
		public void setDevpostion(String devpostion) {
			this.devpostion = devpostion;
		}
		public Long getRowid() {
			return rowid;
		}
		public void setRowid(Long rowid) {
			this.rowid = rowid;
		}
		public String getDeltathup() {
			return deltathup;
		}
		public void setDeltathup(String deltathup) {
			this.deltathup = deltathup;
		}
		public String getDeltathdown() {
			return deltathdown;
		}
		public void setDeltathdown(String deltathdown) {
			this.deltathdown = deltathdown;
		}
		public String getUpdatinitval() {
			return updatinitval;
		}
		public void setUpdatinitval(String updatinitval) {
			this.updatinitval = updatinitval;
		}

		

}

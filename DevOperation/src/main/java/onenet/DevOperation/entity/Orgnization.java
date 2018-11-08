package onenet.DevOperation.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "orgnization")
public class Orgnization {


	@Id
    private String nodeid;

	private String nodename;
	private String path;
	private String remark;
	public Orgnization(){}
	public Orgnization(String nodeid,String nodename,String path,String remark) {

		this.nodeid = nodeid;
		this.nodename = nodename;
		this.path = path;
		this.remark = remark;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Orgnization [nodeid=" + nodeid + ", nodename=" + nodename + ", path=" + path + ", remark=" + remark
				+ "]";
	}


}

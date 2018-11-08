package onenet.DevOperation.entity;

public class FileDataPackage {

	private int currentpackageindex;
	private int maxpackageindex;
	private String appversion;
	private String filecontent;
	
	
	public int getCurrentpackageindex() {
		return currentpackageindex;
	}
	public void setCurrentpackageindex(int currentpackageindex) {
		this.currentpackageindex = currentpackageindex;
	}
	public int getMaxpackageindex() {
		return maxpackageindex;
	}
	public void setMaxpackageindex(int maxpackageindex) {
		this.maxpackageindex = maxpackageindex;
	}
	public String getAppversion() {
		return appversion;
	}
	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}
	public String getFilecontent() {
		return filecontent;
	}
	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	

}

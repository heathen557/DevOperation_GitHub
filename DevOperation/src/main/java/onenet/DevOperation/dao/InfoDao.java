package onenet.DevOperation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import onenet.DevOperation.entity.DevInfo;



public interface InfoDao  extends CrudRepository<DevInfo, Long>{
	
//	 Page<DevPara> findByDevarea(String areaid,Pageable pageable);
//	  
	  	public List<DevInfo> findByRowid(Long rowid);
//	  
	    @Query(value ="update devinfo set carinfo=?1 , rtc=?2 , pairid=?3 , power=?4 , onlinestat='1' where rowid=(select attrid from devattr where devid = ?5) "
	    		,nativeQuery = true)
	    @Modifying
	    public void updateInfo(String carinfo ,String rtc,String pairid,String power,String devid);
	    
	    @Query(value ="update devinfo set onlinestat=?1 where rowid=(select attrid from devattr where devid = ?2) "
	    		,nativeQuery = true)
	    @Modifying
	    public void updateInfoOnline(String onlinestat ,String devid);
	    
	    @Query(value ="update devinfo set power=?1 where rowid=(select attrid from devattr where devid = ?2)  "
	    		,nativeQuery = true)
	    @Modifying
	    public void updateInfoPower(String power ,String devid);
	    
//	    @Query(value ="SELECT i.carinfo,i.rowid,i.rtc,i.power,i.pairid,i.remark,i.onlinestat,a.imei ,a.devpostion FROM devattr as a LEFT JOIN devinfo as i ON a.ATTRID = i.ROWID WHERE a.NODEID IN(SELECT c.NODEID  FROM orgnization as c WHERE c.PATH  LIKE CONCAT('%/',?1,'%'))" +" limit ?2,?3"
//	    		,nativeQuery = true) 
//	    public List<DevInfo> findDefault(String nodeid,int page,int pageSize);
	    
	    
	    @Query(value ="SELECT i.carinfo,i.rowid,i.rtc,i.power,i.pairid,i.remark,i.onlinestat,a.imei ,a.devpostion FROM devattr as a LEFT JOIN devinfo as i ON a.ATTRID = i.ROWID WHERE a.NODEID like ?1" +" limit ?2,?3"
	    		,nativeQuery = true) 
	    public List<DevInfo> findDefault(String nodeid,int page,int pageSize);
	    
	    
//	    @Query(value ="SELECT count(*)  FROM orgnization WHERE path LIKE CONCAT('%/',?1,'%')"
//	    		,nativeQuery = true) 
//	    public Integer totaloffindAll(String nodeid);
	    
	    @Query(value ="SELECT count(*)  FROM devattr WHERE NODEID LIKE ?1"
	    		,nativeQuery = true) 
	    public Integer totaloffindAll(String nodeid);
	    
	    @Query(value ="SELECT i.carinfo,i.rowid,i.rtc,i.power,i.pairid,i.remark,i.onlinestat,a.imei ,a.devpostion FROM devattr as a LEFT JOIN devinfo as i ON a.ATTRID = i.ROWID WHERE a.IMEI = ?1"
	    		,nativeQuery = true) 
	    public List<DevInfo> findByDevId(String imei);
	    
	    
	    
}
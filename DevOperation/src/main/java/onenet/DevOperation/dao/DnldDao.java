package onenet.DevOperation.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import onenet.DevOperation.entity.DevDownload;



public interface DnldDao  extends CrudRepository<DevDownload, Long>{
	
//	 Page<DevPara> findByDevarea(String areaid,Pageable pageable);
//	  
	  	public List<DevDownload> findByRowid(Long rowid);
//	  
	    @Query(value ="SELECT i.rowid,a.IMEI,a.DEVPOSTION,i.STATUS,i.MODE,i.UPDATETIME,i.VERSION,i.PROGRESS FROM (SELECT * from devattr WHERE IMEI=?1) a LEFT JOIN devdownload i ON a.ATTRID = i.ROWID WHERE a.DEVID IS NOT NULL"+" limit ?2,?3"
	    		,nativeQuery = true) 
	    public List<DevDownload> findSome(String imei,int page,int pageSize);
	    
	    @Query(value ="SELECT version FROM devdownload WHERE rowid = (select attrid from devattr where devid=?1)"
	    		,nativeQuery = true) 
	    public String findVerByDevid(String devid);
	    
	    @Query(value ="SELECT imei FROM devattr WHERE attrid = ?1"
	    		,nativeQuery = true) 
	    public String findImeiByRowid(Long rowid);
	    
//	    @Query(value ="SELECT count(*) FROM devattr WHERE devarea = ?1 and  tcc = ?2 and devpostion = ?3"
//	    		,nativeQuery = true) 
//	    public Integer totaloffindAll(String areaid ,String tcc,String berth);
	    
	    @Query(value ="SELECT i.rowid,a.IMEI ,a.DEVPOSTION,i.STATUS,i.MODE,i.UPDATETIME,i.VERSION,i.PROGRESS FROM (SELECT * from devattr WHERE DEVID IN(SELECT DEVID from devattr WHERE NODEID IN(SELECT NODEID from orgnization WHERE path LIKE CONCAT('%/',?1,'%')))) a LEFT JOIN devdownload i ON a.ATTRID = i.ROWID WHERE a.DEVID IS NOT NULL"+" limit ?2,?3"
	    		,nativeQuery = true) 
	    public List<DevDownload> findDefault(String nodeid,int page,int pageSize);
	    
	    @Query(value ="SELECT count(*) from devattr WHERE DEVID IN(SELECT DEVID from devattr WHERE NODEID IN(SELECT NODEID from orgnization WHERE path LIKE CONCAT('%/',?1,'%')))"
	    		,nativeQuery = true) 
	    public Integer totaloffindAll(String nodeid);
	    
	    @Query(value ="update devdownload set mode=?1 , updatetime=?2,version = ?3 where rowid = ?4"
	    		,nativeQuery = true)
	    @Modifying
	    @Transactional
	    public void updatedownloadPara(String appdownload ,String appupdatetime,String verison,Long rowid);
	    
	    @Query(value ="update devdownload set mode=?1, updatetime=?2 where rowid = (select attrid from devattr where devid=?3)"
	    		,nativeQuery = true)
	    @Modifying	
	    @Transactional
	    public void updatednldParaByDevid(String appdownload ,String appupdatetime,String devid);
	    
	    @Query(value ="update devdownload set status=?1, version=?2,progress=?3 where rowid = (select attrid from devattr where devid=?4)"
	    		,nativeQuery = true)
	    @Modifying	
	    @Transactional
	    public void updateStatusVerPgBydevid(String status ,String version,String progress,String devid);
	    
	    @Query(value ="update devdownload set status=?1 where rowid = (select attrid from devattr where devid=?2)"
	    		,nativeQuery = true)
	    @Modifying
	    @Transactional
	    public void updateStatusBydevid(String status,String devid); 
	    
	    @Query(value ="update devdownload set progress=?1 where rowid = (select attrid from devattr where devid=?2)"
	    		,nativeQuery = true)
	    @Modifying
	    @Transactional
	    public void updateProgressBydevid(String progress,String devid);
	    
}
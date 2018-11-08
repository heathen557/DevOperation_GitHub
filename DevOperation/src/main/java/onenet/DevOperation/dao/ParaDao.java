package onenet.DevOperation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import onenet.DevOperation.entity.DevPara;



public interface ParaDao  extends CrudRepository<DevPara, Long>{
	
//	 Page<DevPara> findByDevarea(String areaid,Pageable pageable);
//	  
	  	public DevPara findByRowid(Long rowid);
//	  
	    @Query(value ="SELECT i.rowid,a.IMEI ,a.DEVPOSTION,i.DELTATHUP,i.DELTATHDOWN,i.UPDATINITVAL FROM (SELECT * from devattr WHERE IMEI=?1) a LEFT JOIN devpara i ON a.ATTRID = i.ROWID WHERE a.DEVID IS NOT NULL"+" limit ?2,?3"
	    		,nativeQuery = true) 
	    public List<DevPara> findSome(String devid,int page,int pageSize);
	    
//	    @Query(value ="SELECT count(*) FROM devattr WHERE devarea = ?1 and  tcc = ?2 and devpostion = ?3"
//	    		,nativeQuery = true) 
//	    public Integer totaloffindAll(String areaid ,String tcc,String berth);
	    
	    @Query(value ="SELECT i.rowid,a.IMEI ,a.DEVPOSTION,i.DELTATHUP,i.DELTATHDOWN,i.UPDATINITVAL FROM (SELECT * from devattr WHERE DEVID IN(SELECT DEVID from devattr WHERE NODEID IN(SELECT NODEID from orgnization WHERE path LIKE CONCAT('%/',?1,'%')))) a LEFT JOIN devpara i ON a.ATTRID = i.ROWID WHERE a.DEVID IS NOT NULL"+" limit ?2,?3"
	    		,nativeQuery = true) 
	    public List<DevPara> findDefault(String nodeid,int page,int pageSize);
	    
	    @Query(value ="SELECT count(*) from devattr WHERE DEVID IN(SELECT DEVID from devattr WHERE NODEID IN(SELECT NODEID from orgnization WHERE path LIKE CONCAT('%/',?1,'%')))"
	    		,nativeQuery = true) 
	    public Integer totaloffindAll(String nodeid);
	    
	    @Query(value ="SELECT imei FROM devattr where attrid=?1"
	    		,nativeQuery = true) 
	    public String findImeiByRowid(Long rowid);
	 
	    @Query(value ="update devpara set DELTATHUP=?1 , DELTATHDOWN=?2,UPDATINITVAL = ?3 where rowid=?4"
	    		,nativeQuery = true)
	    @Modifying		  
	    @Transactional
	    public void updatePara(String deltaup ,String deltadown,String initvalue, Long rowid);
	    
	    @Query(value ="update devpara set DELTATHUP=?1 , DELTATHDOWN=?2,UPDATINITVAL = ?3 where rowid=(select attrid from devattr where devid=?4)"
	    		,nativeQuery = true)
	    @Modifying		  
	    @Transactional
	    public void updatePara(String deltaup ,String deltadown,String initvalue, String devid);
	    
	    
}

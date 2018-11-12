package onenet.DevOperation.dao;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import onenet.DevOperation.entity.Geo;


public interface geoDao  extends CrudRepository<Geo, Long>{
	
    public Geo findByDevid(String devid);
    
    //Page<Geo> findAll(Pageable pageable); 
  
//    @Query(value ="SELECT * FROM geoval WHERE rtc between DATE_FORMAT(?1,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DEVID=?3"+" limit ?4,?5"
//    		,nativeQuery = true)    
//    public List<Geo> findByRtc(String starttime,String stoptime,String devid,int page,int pageSize); 
//    
//    @Query(value ="SELECT count(*) FROM geoval WHERE rtc between DATE_FORMAT(?1,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DEVID=?3"
//    		,nativeQuery = true)   
//    public Integer totaloffindAll(String starttime,String stoptime,String devid);
    
    //add the sql 18/11/12
//    @Query(value = "SELECT i.id,i.devid ,a.IMEI,i.CARINFO,i.CURX,i.CURY,i.CURZ,i.INITX,i.INITY,i.INITZ,i.RTC ,i.PAIRID FROM (SELECT * FROM  geoval WHERE rtc between DATE_FORMAT(?1,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DEVID IN(SELECT DEVID from devattr WHERE NODEID = ？3 and IMEI = ?4 ))) i LEFT JOIN devattr a on  (i.DEVID = a.DEVID) limit ?5,?6"
//    		,nativeQuery = true)
//    public List<Geo> findByImei(String starttime,String stoptime,String nodeid,String imei ,int page,int pageSize);
    
    @Query(value ="SELECT i.id,i.devid ,a.imei,i.CARINFO,i.CURX,i.CURY,i.CURZ,i.INITX,i.INITY,i.INITZ,i.RTC ,i.PAIRID FROM (SELECT * FROM  geoval WHERE rtc between DATE_FORMAT(?1,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DEVID IN(SELECT DEVID from devattr WHERE NODEID IN(SELECT NODEID from orgnization WHERE path LIKE CONCAT('%/',?3,'_%')))) i LEFT JOIN devattr a on  (i.DEVID = a.DEVID) limit ?4,?5"
    		,nativeQuery = true)    
    public List<Geo> findByNodeid(String starttime,String stoptime,String nodeid,int page,int pageSize);
    
    @Query(value ="SELECT count(*) FROM geoval WHERE rtc between DATE_FORMAT(?1,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DEVID IN(SELECT DEVID from devattr WHERE NODEID IN(SELECT NODEID from orgnization WHERE path LIKE CONCAT('%/',?3,'_%')))"
    		,nativeQuery = true)   
    public Integer totaloffindAll1(String starttime,String stoptime,String nodeid);
    
    @Query(value ="SELECT i.id ,i.devid,a.IMEI,i.CARINFO,i.CURX,i.CURY,i.CURZ,i.INITX,i.INITY,i.INITZ,i.RTC ,i.PAIRID FROM geoval i LEFT JOIN devattr a on  (i.DEVID = a.DEVID) AND a.IMEI=?1  WHERE  i.DEVID = a.DEVID and i.rtc between DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?3,'%Y-%m-%d %H:%i:%S') limit ?4,?5"
    		,nativeQuery = true)    
    public List<Geo> findByRtc(String imei,String starttime,String stoptime,int page,int pageSize);
    
    @Query(value ="SELECT count(*) FROM geoval i LEFT JOIN devattr a on  (i.DEVID = a.DEVID) AND a.IMEI=?1 WHERE i.DEVID IS NOT NULL and i.DEVID = a.DEVID and i.rtc between DATE_FORMAT(?2,'%Y-%m-%d %H:%i:%S') and DATE_FORMAT(?3,'%Y-%m-%d %H:%i:%S')"
    		,nativeQuery = true)   
    public Integer totaloffindAll(String imei,String starttime,String stoptime);
    
}


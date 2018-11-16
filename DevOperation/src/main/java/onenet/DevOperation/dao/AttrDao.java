package onenet.DevOperation.dao;



import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import onenet.DevOperation.entity.DevAttr;

public interface AttrDao  extends CrudRepository<DevAttr, Long>{
    //public DevAttr findByAttrid(Long id);
    
	//public List<DevAttr> findByDevarea(String areaid,Pageable pageable);
  
  	public DevAttr findByAttrid(Long attrid);
    
//    @Query(value ="SELECT a.* FROM devattr a WHERE nodeid  in (SELECT nodeid from orgnization where (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/',''))=6 and PATH like CONCAT('%',?1,'%')))"+" limit ?2,?3"
//    		,nativeQuery = true) 
//    public List<DevAttr> findallBytcc(String tcc,int page,int pageSize);
    
    
    @Query(value ="SELECT a.* FROM devattr a WHERE nodeid = ?1 "+" limit ?2,?3"
    		,nativeQuery = true) 
    public List<DevAttr> findallBytcc(String tcc,int page,int pageSize);
    
    @Query(value ="SELECT a.IMEI FROM devattr a WHERE a.devid = ?1"
    		,nativeQuery = true) 
    public String findImei(String devid);

//    @Query(value ="SELECT count(*) from orgnization where (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))=6 and PATH like CONCAT('%',?1,'%')"
//    		,nativeQuery = true) 
//    public Integer totaloffindAll(String tcc);
    
    @Query(value ="SELECT count(*) from devattr where NODEID = ?1"
    		,nativeQuery = true) 
    public Integer totaloffindAll(String tcc);
    
    @Query(value ="SELECT a.* FROM devattr a WHERE a.imei = ?1"
    		,nativeQuery = true) 
    public List<DevAttr> findallBydevid(String devid);
    
    @Query(value ="update devattr set OPERATOR=?1,PROTOCOL=?2,REGCODE=?3,DEVPOSTION=?4,IMEI=?5,IMSI=?6,DEVID=?7 where attrid=?8 "
    		,nativeQuery = true)  
    @Modifying
    @Transactional
    public void updatedevattrbyid(String oper,String protocol,String regcode,String berth,String imei,String imsi,String devid,Long attrid);
    
    @Query(value ="update devattr set DEVID=?1 where IMEI=?2 "
    		,nativeQuery = true)  
    @Modifying
    @Transactional
    public void updatedevidbyimei(String devid,String imei);

    @Query(value ="SELECT a.* FROM devattr a WHERE a.attrid = ?1"
    		,nativeQuery = true) 
	public DevAttr findImeiByAttrid(Long attrid);
   
    //更新或添加属性
//    @Query(value ="replace into devattr(devid,devpostion,devarea.tcc) values(?1, ?2,?3,?4)"
//    		,nativeQuery = true) 
//    public Integer replaceAll(String devid,String devpostion,String areaid ,String tcc);
}


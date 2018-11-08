package onenet.DevOperation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import onenet.DevOperation.entity.warn_log;

public interface WarnLogDao extends CrudRepository<warn_log,Integer>{

//    @Query(value ="SELECT count(*) FROM warn_log "
//    		,nativeQuery = true) 
//	public int findWarnItemNumbers();
	
    @Query(value ="SELECT COUNT(*) FROM warn_log where gjsj>=?1 and gjsj<=?2 and devid IN (SELECT devid FROM devattr WHERE NODEID =?3)"
    		,nativeQuery = true) 
	public int findWarnItemNumbers(String starttime,String stoptime,String nodeId);


	
    @Query(value ="SELECT a.* FROM warn_log a where gjsj>=?1 and gjsj<=?2 and devid IN (SELECT devid FROM devattr WHERE NODEID = ?3)limit ?4,?5"
    		,nativeQuery = true) 
    public List<warn_log> findWarnItems(String starttime,String stoptime,String nodeId,int page,int pageSize);

	
}

package onenet.DevOperation.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import onenet.DevOperation.entity.OperationLog;


public interface OperationLogDao extends CrudRepository<OperationLog,Integer> {
	
    @Query(value ="SELECT count(*) FROM operation_log where czsj>=?1 and czsj<=?2  "
    		,nativeQuery = true) 
	public int findOperationItemNumbers(String starttime,String stoptime,String nodeId);
	
	
    @Query(value ="SELECT a.* FROM operation_log a where czsj>=?1 and czsj<=?2 limit ?3,?4"
    		,nativeQuery = true) 
    public List<OperationLog> findOperationItems(String starttime,String stoptime,int page,int pageSize);

    
    @Query(value ="INSERT INTO operation_log (CZLX,CZNR,CZR,CZSJ,BZ) VALUES(?1,?2,?3,?4,?5)"
    		,nativeQuery = true) 
    public void saveOperateLog(Integer CZLX,String czlr,String czr,String czsj,String bz);
} 

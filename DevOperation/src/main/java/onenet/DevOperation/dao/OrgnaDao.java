package onenet.DevOperation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import onenet.DevOperation.entity.Orgnization;

public interface OrgnaDao extends CrudRepository<Orgnization, Long> {

	@Query(value = "SELECT * from orgnization where (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))<=?1", nativeQuery = true)
	public List<Orgnization> findByPathLength(int length);

	@Query(value = "SELECT path from orgnization where NODEID = (select NODEID FROM devattr WHERE IMEI=?1)", nativeQuery = true)
	public String findpathBydevId(String devid);

	@Query(value = "SELECT NODENAME FROM orgnization WHERE (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))=5", nativeQuery = true)
	public List<String> findalltcc();

	@Query(value = "SELECT NODENAME FROM orgnization WHERE NODEID = (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(PATH,'/',-2),'/',1) FROM orgnization WHERE NODEID = (SELECT NODEID FROM devattr WHERE attrid=?1))", nativeQuery = true)
	public String findtccByattrid(Long attrid);

	@Query(value = "DELETE FROM orgnization WHERE nodeid IN(SELECT nodeid from devattr WHERE ATTRID=?1 )", nativeQuery = true)
	@Modifying
	public void deleteNodeByNodeid(Long attrid);

	@Query(value = "update orgnization set NODENAME=?1 where NODEID=(select NODEID from devattr where ATTRID=?2)", nativeQuery = true)
	@Modifying
	@Transactional
	public void updateorgbynodeid(String nodename, Long attrid);

	@Query(value = "SELECT CONCAT(MAX(CONVERT(NODEID,SIGNED))+1,'') FROM orgnization", nativeQuery = true)
	public String findNextNodeid();

	@Query(value = "SELECT PATH from orgnization WHERE nodename=?1", nativeQuery = true)
	public String findPathByNodename(String tcc);

	@Query(value = " SELECT * FROM orgnization WHERE PATH REGEXP CONCAT(?1,'/[0-9]{1,}$') AND (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))<=5"
			+ " limit ?2,?3", nativeQuery = true)
	public List<Orgnization> findnodeByParentId(String parentpath, int page, int pageSize);

	@Query(value = " SELECT * FROM orgnization WHERE nodeid=?1", nativeQuery = true)
	public Orgnization findorgBynodeId(String nodeid);

	@Query(value = "  SELECT count(*) FROM orgnization WHERE PATH REGEXP CONCAT(?1,'/[0-9]{1,}$') AND (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))<=5", nativeQuery = true)
	public Integer findnodeCountByParentId(String parentpath);

	@Query(value = "update orgnization set NODENAME=?1,PATH=?2,REMARK=?3 where NODEID=?4", nativeQuery = true)
	@Modifying
	@Transactional
	public void updateorgbynodeid(String nodename, String path, String remark, String nodeid);

	@Query(value = "update orgnization set NODENAME=?1,REMARK=?2 where NODEID=?3", nativeQuery = true)
	@Modifying
	@Transactional
	public void updateorgbynodeid(String nodename, String remark, String nodeid);

	@Query(value = "SELECT NODENAME FROM orgnization WHERE (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))=?1", nativeQuery = true)
	public List<String> findorgByLevel(Long level);

	@Query(value = "SELECT PATH from orgnization where nodename=?1", nativeQuery = true)
	public String findlastPathByNodename(String nodename);

	@Query(value = "SELECT PATH from orgnization where nodeid=?1", nativeQuery = true)
	public String findPathByNodeid(String nodeid);

	@Query(value = " UPDATE  orgnization set path = REPLACE(path,?1,?2) WHERE PATH LIKE CONCAT(?3,'%')", nativeQuery = true)
	@Modifying
	@Transactional
	public void updatePathBynewpath(String curpath, String newpath, String curpath2);

	@Query(value = " delete from orgnization where nodeid=?1", nativeQuery = true)
	@Modifying
	@Transactional
	public void delBynodeid(String nodeid);

	@Query(value = "insert into orgnization (nodeid,nodename, path, remark) values (?1, ?2, ?3, ?4)", nativeQuery = true)
	@Modifying
	@Transactional
	public void SaveOrg(String nodeid, String nodename, String path, String remark);

	@Query(value = "SELECT MIN(CONVERT(NODEID ,signed)) FROM orgnization WHERE (LENGTH(PATH) - LENGTH(REPLACE(PATH,'/','')))=5 and PATH LIKE CONCAT('%',?1,'%')", nativeQuery = true)
	public Integer findTccByNodeid(String nodeid);
	
	@Query(value = "SELECT NODEID FROM orgnization WHERE NODENAME = ?1  ",nativeQuery = true)
	public String findNodeidByTcc(String tcc);

}
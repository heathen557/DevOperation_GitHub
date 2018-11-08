package onenet.DevOperation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import onenet.DevOperation.entity.Orgnization;
import onenet.DevOperation.entity.TreeviewNode;
import onenet.DevOperation.utils.PropertyUtil;

public class HanziString {

	@Test
	public void testString() {
		String devId = "汉字显示";

		System.out.println("devId:" + devId);

	}
	
	@Test
	public void path2parentid() {
		List<Orgnization> orglist = new ArrayList();
		String[][] str = {{"1","平台","/1"},{"2","南京市","/1/2"},{"3","重庆市","/1/3"},{"4","江宁区","/1/2/4"},{"5","秦淮区","/1/2/5"}
		,{"6","大光路街道","/1/2/5/6"},{"7","莱斯停车场","/1/2/5/6/7"}};
		for(int i = 0 ; i < 7 ; i++) {
			Orgnization orgna = new Orgnization();
			orgna.setNodeid(str[i][0]);
			orgna.setNodename(str[i][1]);
			orgna.setPath(str[i][2]);
			orglist.add(orgna);
			System.out.println(orgna.toString());
		}
		//System.out.println(orglist);
		
		TreeviewNode treeviewnode = new TreeviewNode();
		
		treeviewnode.ToResultJson(orglist);
		//System.out.println(s);
	}
	
	@Test
	public void test1() {
	SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	Date date;
	try {
		date = format.parse("12:00");
		System.out.println(date);
		System.out.println(date.getTime());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	@Test
	public void test2() {
		String a = "12:00";
		String[] b = a.split(":");
		System.out.println(Integer.valueOf(b[0])*60*60 + Integer.valueOf(b[1])*60 );
		Long.parseLong(a);
	}
	
	@Test
	public void test3() { 
		String path = PropertyUtil.class.getResource("/syssettings.properties").getPath();
		System.out.println(path);
		PropertyUtil.getPro(path, "spring.datasource.url");
		
		PropertyUtil.updatePro(path, "spring.datasource.url", "123");
	}
}


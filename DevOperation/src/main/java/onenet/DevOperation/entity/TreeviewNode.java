package onenet.DevOperation.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressWarnings({ "unchecked", "rawtypes" })  
public class TreeviewNode {

	public List initList(List  specTableList){  
	    List dataList = new ArrayList();    
	     
	    for(int i = 0; i < specTableList.size(); i++) {  
	    	Orgnization VO = (Orgnization)specTableList.get(i);  
	        HashMap dataRecord = new HashMap(); 
	        String path = VO.getPath();

	        dataRecord.put("id",VO.getNodeid());    
	        dataRecord.put("text", VO.getNodename()); 
	        String parentID;
	        if (path.length() == 2) {  
	            parentID = "0";  
	          
	        }else {
	        	parentID = Path2Parentid(path);  
	        }
	        
	        dataRecord.put("parentId",parentID);    
	        dataList.add(dataRecord);  
	        
	    }  
	    return dataList;  
	} 
	
	class  Node {  
	    private String id;  
	    private String text;  
	    private String parentId;  
	      
	    /**  
	      * 孩子节点列表  
	      */    
	     private Children children = new Children();    
	         
	     // 先序遍历，拼接JSON字符串    
	     public String toString() {      
	      String result = //"["  
	      "{"   
	      + "\"text\":\"" + text + "\","  
	      + "\"href\":\"" + id +"\"";    
	      if (children != null && children.getSize() != 0) {  
	        if (result.contains("nodes")) {  
	            result += ",";  
	        }else{  
	            result += ",\"nodes\":" + children.toString();    
	        }  
	      }    
	      return result + "}";     
	     }    
	         
	     // 兄弟节点横向排序    
	     public void sortChildren() {    
	      if (children != null && children.getSize() != 0) {    
	       children.sortChildren();    
	      }    
	     }    
	         
	     // 添加孩子节点    
	     public void addChild(Node node) {    
	      this.children.addChild(node);    
	     }    
	}  
	  
	 class  Children {  
	    private List list = new ArrayList();  
	      
	    public int getSize(){  
	        return list.size();  
	    }  
	    public void addChild(Node node){  
	        list.add(node);  
	    }  
	      
	     // 拼接孩子节点的JSON字符串    
	     public String toString() {    
	      String result = "[";      
	      for (Iterator it = list.iterator(); it.hasNext();) {    
	       result += ((Node) it.next()).toString();    
	       result += ",";    
	      }    
	      result = result.substring(0, result.length() - 1);    
	      result += "]";    
	      return result;    
	     }    
	         
	     // 孩子节点排序    
	     public void sortChildren() {    
	      // 对本层节点进行排序    
	      // 可根据不同的排序属性，传入不同的比较器，这里传入ID比较器    
	      Collections.sort(list, new NodeIDComparator());    
	      // 对每个节点的下一层节点进行排序    
	      for (Iterator it = list.iterator(); it.hasNext();) {    
	       ((Node) it.next()).sortChildren();    
	      }    
	     }    
	         
	    /**  
	     * 节点比较器  
	     */    
	    class NodeIDComparator implements Comparator {    
	     // 按照节点编号比较    
	     public int compare(Object o1, Object o2) {    
	      int j1 = Integer.parseInt(((Node)o1).id);    
	         int j2 = Integer.parseInt(((Node)o2).id);    
	         return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));    
	     }     
	    }     
	}  
	 private String Path2Parentid(String path) {
		 
		 String[] nodeid = path.split("/") ;
		 String parentid = nodeid[nodeid.length-2];
		 return parentid;
	 }
	 
	 public String ToResultJson(List  specTableList) {
 
			List dataList = initList(specTableList);
			System.out.println("dataList:" + dataList);
			// 节点列表（散列表，用于临时存储节点对象）  
			HashMap nodeList = new HashMap();  
			  // 根节点  
			Node root = new  Node();
			root.id = "0";
			root.text= "中心";
			root.parentId = "";		
			nodeList.put(root.id, root);
			  // 根据结果集构造节点列表（存入散列表）  
			for (Iterator it = dataList.iterator(); it.hasNext();) {  
				HashMap dataRecord = (HashMap) it.next(); 
				//Orgnization orgnization = ((Orgnization)it.next());
			   Node node = new Node();  
			   node.id = (String) dataRecord.get("id");  
			   node.text = (String) dataRecord.get("text");  
	
			   node.parentId = (String) dataRecord.get("parentId");
			   nodeList.put(node.id, node);  
			}  
			  // 构造无序的多叉树  
			Set entrySet = nodeList.entrySet();  
			for (Iterator it = entrySet.iterator(); it.hasNext();) {  
				Node node = (Node) ((Map.Entry) it.next()).getValue();  
				if (node.parentId == null || node.parentId.equals("")) {  
				    root = node;  
				} else {  
				     ((Node) nodeList.get(node.parentId)).addChild(node);  
				}  
		    }  
			  // 输出无序的树形菜单的JSON字符串  
			String re = "["+root.toString()+"]";
			//System.out.println("re:" + re);     
			return "["+root.toString()+"]";

	 }
}


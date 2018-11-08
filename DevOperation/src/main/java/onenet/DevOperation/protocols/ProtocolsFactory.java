package onenet.DevOperation.protocols;

public class ProtocolsFactory {
	
	public static CustomProtocols getProtocol(Class c) {
		
		CustomProtocols customprotocol = null;
		try {
			customprotocol = (CustomProtocols) Class.forName(c.getName()).newInstance();
		}catch (InstantiationException e) {
            // TODO Auto-generated catch block
            System.out.println("不支持抽象类或接口");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("没有足够权限，即不能访问私有对象");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("类不存在");
            e.printStackTrace();
        }    
		return customprotocol;
	}
}

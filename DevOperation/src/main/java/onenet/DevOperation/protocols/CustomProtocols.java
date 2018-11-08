package onenet.DevOperation.protocols;



public interface CustomProtocols {
	
    
    public static byte[] HEADER = {(byte)0XFE,(byte)0XFE,(byte)0XFE,(byte)0XFE,(byte)0XFE};
    public static byte[] RESERVE = {0X00,0X00};


    public String paraseCommend(byte[] data);
    

    public abstract byte[] getCommend();
    public abstract String getContentData(Object obj);
    //编码
    
    //解码,重写
    public int parseResponse(String response);
    
}

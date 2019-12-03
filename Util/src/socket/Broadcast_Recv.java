package socket;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Broadcast_Recv {
    public static void main(String[] args) {
        int port = 9999; // 开启监听的端口
        DatagramSocket ds = null;
        DatagramPacket dp = null;
        byte[] buf = new byte[1024];// 存储发来的消息
        try {
            // 绑定端口ra
        	InetAddress address=InetAddress.getByName("192.168.43.54");  
        	ds = new DatagramSocket(port, address);
            dp = new DatagramPacket(buf, buf.length);
            System.out.println(" 192.168.43.54  监听广播端口打开：");
            
            while (true) {
                ds.receive(dp);
                System.out.println("收到广播消息：" + new String(buf));
            }
            
//            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

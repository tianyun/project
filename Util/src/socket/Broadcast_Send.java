package socket;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Broadcast_Send {
	public static void main(String[] args) throws IOException {
        //1.创建对象
        //构造数据报套接字并将其绑定到本地主机上任何可用的端口。
    	InetAddress address=InetAddress.getByName("192.168.43.82");  

        DatagramSocket socket = new DatagramSocket(7676, address);
        //2.打包
        byte[] arr = "客户端：哈哈。。。。".getBytes();
        //四个参数: 包的数据  包的长度  主机对象  端口号                      
        DatagramPacket packet = new DatagramPacket
                (arr, arr.length,InetAddress.getByName("255.255.255.255") , 9999);
        
        //3.发送
        socket.send(packet);
        
        //4.关闭资源
        socket.close();
    }
}

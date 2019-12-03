package socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//接收端
/**
 * 代码来源 https://blog.csdn.net/qq_38341596/article/details/78779955
 * https://blog.csdn.net/weixin_44718300/article/details/88659360
 * 
 * 
 * 接收端的使用步骤 1.建立udp的服务 2. 准备空 的数据 包接收数据。 3. 调用udp的服务接收数据。 4. 关闭资源
 * 
 */
public class Receiver {
	static int kk=1;
	public static void main(String[] args) throws IOException {
		System.out.println(kk);
		udpCommunication();
	}

	public static void udpCommunication() throws IOException {
		// 建立udp的服务 ，并且要监听一个端口。
		DatagramSocket socket = new DatagramSocket(9090);

		// 准备空的数据包用于存放数据。
		byte[] buf = new byte[1024];
		DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length); // 1024
		// 调用udp的服务接收数据
		socket.receive(datagramPacket); // receive是一个阻塞型的方法，没有接收到数据包之前会一直等待。 数据实际上就是存储到了byte的自己数组中了。
		System.out.println("接收端接收到的数据：" + new String(buf, 0, datagramPacket.getLength())); // getLength() 获取数据包存储了几个字节。
		System.out.println("receive阻塞了我，哈哈");
		// 关闭资源
		socket.close();
	}

	public static void tcpCommunication() throws IOException {
		// 创建Socket对象
		ServerSocket ss = new ServerSocket(10086);
		// 监听（阻塞）
		Socket s = ss.accept();
		// 获取输入流对象
		InputStream is = s.getInputStream();
		// 获取数据
		byte[] bys = new byte[1024];
		int len;
		len = is.read(bys);
		// 输出数据
		InetAddress address = s.getInetAddress();
		System.out.println("sender:" + address);
		System.out.println(new String(bys, 0, len));
		// 释放
		s.close();
		

	}

}
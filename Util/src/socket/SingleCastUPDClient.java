package socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class SingleCastUPDClient {
	public static void main(String[] args) throws Exception {
		{
			// 构造DatagramSocket实例，指定本地端口8888
			try (DatagramSocket datagramSocket = new DatagramSocket(8888)) {
				for (int i = 0; i < 5; i++) {
					String data = "当前循环：" + i;
					// 构造数据报包，用来将data发送到指定主机上的指定端口号。
					DatagramPacket sendPacket = new DatagramPacket(data.getBytes("UTF-8"),
							data.getBytes("UTF-8").length, new InetSocketAddress("192.168.1.4", 9999));
					// 发送报文
					datagramSocket.send(sendPacket);
				}
			}
		}
	}
}
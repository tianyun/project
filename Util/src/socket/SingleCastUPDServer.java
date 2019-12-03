package socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SingleCastUPDServer {
	public static void main(String[] args) throws Exception {
		System.out.println("启动UDP单播服务端");
		// 构造DatagramSocket实例，指定本地端口6666
		try (DatagramSocket datagramSocket = new DatagramSocket(6666)) {
			// 设置超时时间为10秒
			//datagramSocket.setSoTimeout(10000);
			while (true) {
				// 应用层交给UDP多长的报文，UDP就照样发送，一次发送一个报文。报文最大长度有限制，否则会导致IP层分片，最大值最好小于548字节
				// 构造DatagramPacket实例，用来接收最大长度为512字节的数据包
				byte[] data = new byte[512];
				DatagramPacket receivePacket = new DatagramPacket(data, 512);
				// 接收报文，此方法在接收到数据报前一直阻塞
				datagramSocket.receive(receivePacket);
				System.out.println("客户端地址：" + receivePacket.getAddress().getHostAddress());
				System.out.println("客户端端口：" + receivePacket.getPort());
				System.out.println("接收到的数据长度：" + receivePacket.getLength());
				System.out.println(
						"接收到的数据：" + new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8"));
			}
		}
	}
}
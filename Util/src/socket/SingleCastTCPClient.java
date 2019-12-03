package socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SingleCastTCPClient {
	public static void main(String[] args) throws Exception {
		// 构造Socket实例
		try (Socket socket = new Socket("127.0.0.1", 6666);
				BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
				BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			System.out.println("服务端的ip和端口：" + inetSocketAddress.getAddress() + " " + inetSocketAddress.getPort());
			InetSocketAddress inetSocketAddressL = (InetSocketAddress) socket.getLocalSocketAddress();
			System.out.println("本地绑定的ip和端口：" + inetSocketAddressL.getAddress() + " " + inetSocketAddressL.getPort());
			outputStream.write("abcdefg测试数据1234567890".getBytes("UTF-8"));
			outputStream.flush();
			// 关闭此socket输出流
			socket.shutdownOutput();
			byte[] bytes = new byte[1024];
			int count;
			if ((count = inputStream.read(bytes)) != -1) {
				System.out.println(new String(bytes, 0, count, "UTF-8"));
			}
			// 关闭此socket输入流
			socket.shutdownInput();
		}
	}
}
